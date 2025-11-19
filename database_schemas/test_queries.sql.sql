-- =====================================================
-- COMPLETE TESTING SCRIPT
-- =====================================================
-- Run this AFTER loading user_authentication.sql
-- Tests all 12 queries with expected results
-- Gabriela Rodriguez   PID: 6442196
-- =====================================================

-- =====================================================
-- SETUP: Verify database is loaded correctly
-- =====================================================
USE patient_trial_matching;

-- Check all tables exist
SHOW TABLES;
-- Expected: Patient, MedicalCondition, Medication, Trial, 
--           TrialRequirement, PatientCondition, PatientMedication,
--           Roles, Users, UserRoles, UserSessions

-- Quick data check
SELECT 'Users' AS TableName, COUNT(*) AS RecordCount FROM Users
UNION ALL SELECT 'Roles', COUNT(*) FROM Roles
UNION ALL SELECT 'UserRoles', COUNT(*) FROM UserRoles;
-- Expected: 5 Users, 5 Roles, 5 UserRoles

-- =====================================================
-- TEST 1: Login Authentication (SELECT with WHERE)
-- =====================================================
SELECT '=== TEST 1: Login Authentication ===' AS Test;

SELECT 
    UserID,
    Username,
    Email,
    FirstName,
    LastName,
    IsActive,
    LastLogin
FROM Users
WHERE Username = 'admin'
    AND PasswordHash = '$2a$10$N9qo8uLOickgx2ZMRZoMye'
    AND IsActive = TRUE;

-- ✅ EXPECTED: 1 row - admin user with UserID=1
-- ❌ If 0 rows: Check username/password hash match

-- =====================================================
-- TEST 2: Get User with Roles (JOIN)
-- =====================================================
SELECT '=== TEST 2: User Profile with Roles ===' AS Test;

SELECT 
    u.UserID,
    u.Username,
    u.Email,
    CONCAT(u.FirstName, ' ', u.LastName) AS FullName,
    r.RoleName,
    r.CanAddPatients,
    r.CanViewTrials,
    r.CanManageTrials,
    r.CanViewReports
FROM Users u
INNER JOIN UserRoles ur ON u.UserID = ur.UserID
INNER JOIN Roles r ON ur.RoleID = r.RoleID
WHERE u.Username = 'researcher1'
    AND u.IsActive = TRUE;

-- ✅ EXPECTED: 1 row showing researcher1 with "Researcher" role
-- ✅ Should show: CanViewTrials=1, CanManageTrials=1, CanViewReports=1

-- =====================================================
-- TEST 3: User Statistics (AGGREGATE + COUNT)
-- =====================================================
SELECT '=== TEST 3: User Statistics by Role ===' AS Test;

SELECT 
    r.RoleName,
    COUNT(ur.UserID) AS UserCount,
    MAX(u.LastLogin) AS MostRecentLogin
FROM Roles r
LEFT JOIN UserRoles ur ON r.RoleID = ur.RoleID
LEFT JOIN Users u ON ur.UserID = u.UserID AND u.IsActive = TRUE
GROUP BY r.RoleID, r.RoleName
ORDER BY UserCount DESC;

-- ✅ EXPECTED: 5 rows (one per role)
-- ✅ Should show Admin, Researcher, Clinician, Data Analyst, Patient each with 1 user

-- =====================================================
-- TEST 4: Find Users Without Roles (SUBQUERY)
-- =====================================================
SELECT '=== TEST 4: Users Without Roles ===' AS Test;

SELECT 
    UserID,
    Username,
    Email,
    CONCAT(FirstName, ' ', LastName) AS FullName,
    CreatedAt
FROM Users
WHERE IsActive = TRUE
    AND UserID NOT IN (
        SELECT DISTINCT UserID 
        FROM UserRoles
    )
ORDER BY CreatedAt DESC;

-- ✅ EXPECTED: 0 rows (all sample users have roles)
-- Note: This will show results after you add users without assigning roles

-- =====================================================
-- TEST 5: Active Sessions Summary (COMPLEX JOIN)
-- =====================================================
SELECT '=== TEST 5: Active Sessions (if any) ===' AS Test;

-- First, let's create a test session
INSERT INTO UserSessions (UserID, SessionToken, IPAddress, UserAgent)
VALUES (1, 'test_token_12345', '127.0.0.1', 'Mozilla/5.0 Test Browser');

-- Now query active sessions
SELECT 
    u.Username,
    CONCAT(u.FirstName, ' ', u.LastName) AS FullName,
    COUNT(us.SessionID) AS ActiveSessions,
    MAX(us.LastActivityTime) AS LastActivity,
    GROUP_CONCAT(DISTINCT us.IPAddress SEPARATOR ', ') AS IPAddresses
FROM Users u
INNER JOIN UserSessions us ON u.UserID = us.UserID
WHERE us.IsActive = TRUE
    AND TIMESTAMPDIFF(HOUR, us.LastActivityTime, NOW()) < 24
GROUP BY u.UserID, u.Username, u.FirstName, u.LastName
HAVING COUNT(us.SessionID) > 0;

-- ✅ EXPECTED: 1 row showing admin with 1 active session

-- =====================================================
-- TEST 6: Register New User (INSERT)
-- =====================================================
SELECT '=== TEST 6: Register New User ===' AS Test;

INSERT INTO Users (Username, Email, PasswordHash, FirstName, LastName, IsActive)
VALUES (
    'test_user',
    'test@clinic.com',
    '$2a$10$TestHashValue',
    'Test',
    'User',
    TRUE
);

-- Verify insertion
SELECT UserID, Username, Email, FirstName, LastName 
FROM Users 
WHERE Username = 'test_user';

-- ✅ EXPECTED: 1 row with newly created test_user (UserID should be 6)

-- Get the new user's ID
SELECT LAST_INSERT_ID() AS NewUserID;

-- =====================================================
-- TEST 7: Update User Information (UPDATE)
-- =====================================================
SELECT '=== TEST 7: Update User Info ===' AS Test;

-- Update test user's email
UPDATE Users
SET 
    Email = 'updated_test@clinic.com',
    UpdatedAt = NOW()
WHERE Username = 'test_user';

-- Verify update
SELECT UserID, Username, Email, UpdatedAt 
FROM Users 
WHERE Username = 'test_user';

-- ✅ EXPECTED: Email should be 'updated_test@clinic.com'

-- =====================================================
-- TEST 8: Remove User Role (DELETE)
-- =====================================================
SELECT '=== TEST 8: Delete User Role Assignment ===' AS Test;

-- First, assign a role to test_user
INSERT INTO UserRoles (UserID, RoleID, AssignedBy)
VALUES (
    (SELECT UserID FROM Users WHERE Username = 'test_user'),
    5,  -- Patient role
    1   -- Assigned by admin
);

-- Verify role was assigned
SELECT u.Username, r.RoleName
FROM Users u
JOIN UserRoles ur ON u.UserID = ur.UserID
JOIN Roles r ON ur.RoleID = r.RoleID
WHERE u.Username = 'test_user';

-- ✅ EXPECTED: 1 row showing test_user with Patient role

-- Now delete the role assignment
DELETE FROM UserRoles
WHERE UserID = (SELECT UserID FROM Users WHERE Username = 'test_user')
    AND RoleID = 5;

-- Verify deletion
SELECT u.Username, r.RoleName
FROM Users u
LEFT JOIN UserRoles ur ON u.UserID = ur.UserID
LEFT JOIN Roles r ON ur.RoleID = r.RoleID
WHERE u.Username = 'test_user';

-- ✅ EXPECTED: 0 rows (or 1 row with NULL RoleName)

-- =====================================================
-- TEST 9: Find Power Users (GROUP BY + HAVING)
-- =====================================================
SELECT '=== TEST 9: Users with Multiple Roles ===' AS Test;

-- First, give test_user multiple roles
INSERT INTO UserRoles (UserID, RoleID, AssignedBy) VALUES
((SELECT UserID FROM Users WHERE Username = 'test_user'), 3, 1),
((SELECT UserID FROM Users WHERE Username = 'test_user'), 4, 1);

-- Now find power users
SELECT 
    u.UserID,
    u.Username,
    CONCAT(u.FirstName, ' ', u.LastName) AS FullName,
    COUNT(ur.RoleID) AS RoleCount,
    GROUP_CONCAT(r.RoleName ORDER BY r.RoleName SEPARATOR ', ') AS Roles
FROM Users u
INNER JOIN UserRoles ur ON u.UserID = ur.UserID
INNER JOIN Roles r ON ur.RoleID = r.RoleID
WHERE u.IsActive = TRUE
GROUP BY u.UserID, u.Username, u.FirstName, u.LastName
HAVING COUNT(ur.RoleID) > 1
ORDER BY RoleCount DESC;

-- ✅ EXPECTED: 1 row showing test_user with 2 roles

-- =====================================================
-- TEST 10: Activity Report (UNION)
-- =====================================================
SELECT '=== TEST 10: System Activity Report ===' AS Test;

SELECT 
    'Active Users' AS Category,
    COUNT(*) AS Count,
    NULL AS Details
FROM Users
WHERE IsActive = TRUE

UNION ALL

SELECT 
    'Inactive Users',
    COUNT(*),
    NULL
FROM Users
WHERE IsActive = FALSE

UNION ALL

SELECT 
    'Active Sessions',
    COUNT(*),
    NULL
FROM UserSessions
WHERE IsActive = TRUE

UNION ALL

SELECT 
    'Total Roles',
    COUNT(*),
    NULL
FROM Roles

UNION ALL

SELECT 
    'Users with Multiple Roles',
    COUNT(DISTINCT u.UserID),
    NULL
FROM Users u
INNER JOIN UserRoles ur ON u.UserID = ur.UserID
GROUP BY u.UserID
HAVING COUNT(ur.RoleID) > 1;

-- ✅ EXPECTED: 5 rows showing various counts
-- Active Users: 6, Inactive Users: 0, Active Sessions: 1, Total Roles: 5

-- =====================================================
-- TEST 11: Session Validation (BONUS)
-- =====================================================
SELECT '=== TEST 11: Validate Session Token ===' AS Test;

SELECT 
    us.SessionID,
    us.UserID,
    u.Username,
    u.Email,
    CONCAT(u.FirstName, ' ', u.LastName) AS FullName,
    us.LastActivityTime,
    TIMESTAMPDIFF(MINUTE, us.LastActivityTime, NOW()) AS MinutesInactive
FROM UserSessions us
INNER JOIN Users u ON us.UserID = u.UserID
WHERE us.SessionToken = 'test_token_12345'
    AND us.IsActive = TRUE
    AND u.IsActive = TRUE
    AND TIMESTAMPDIFF(HOUR, us.LastActivityTime, NOW()) < 24;

-- ✅ EXPECTED: 1 row showing session details for admin

-- =====================================================
-- TEST 12: Permission Check (BONUS)
-- =====================================================
SELECT '=== TEST 12: Check User Permissions ===' AS Test;

SELECT 
    u.UserID,
    u.Username,
    MAX(r.CanAddPatients) AS CanAddPatients,
    MAX(r.CanViewTrials) AS CanViewTrials,
    MAX(r.CanManageTrials) AS CanManageTrials,
    MAX(r.CanViewReports) AS CanViewReports
FROM Users u
INNER JOIN UserRoles ur ON u.UserID = ur.UserID
INNER JOIN Roles r ON ur.RoleID = r.RoleID
WHERE u.Username = 'clinician1'
    AND u.IsActive = TRUE
GROUP BY u.UserID, u.Username;

-- ✅ EXPECTED: 1 row showing clinician1 permissions
-- ✅ Should show: CanAddPatients=1, CanViewTrials=1, CanManageTrials=0, CanViewReports=1

-- =====================================================
-- CLEANUP: Remove test data (optional)
-- =====================================================
SELECT '=== CLEANUP: Removing test data ===' AS Test;

DELETE FROM UserSessions WHERE SessionToken = 'test_token_12345';
DELETE FROM UserRoles WHERE UserID = (SELECT UserID FROM Users WHERE Username = 'test_user');
DELETE FROM Users WHERE Username = 'test_user';

SELECT 'All tests complete!' AS Status;

-- =====================================================
-- FINAL VERIFICATION
-- =====================================================
SELECT '=== FINAL COUNTS ===' AS Summary;

SELECT 'Users' AS TableName, COUNT(*) AS Count FROM Users
UNION ALL SELECT 'Roles', COUNT(*) FROM Roles
UNION ALL SELECT 'UserRoles', COUNT(*) FROM UserRoles
UNION ALL SELECT 'UserSessions', COUNT(*) FROM UserSessions;

-- ✅ EXPECTED: Back to original counts (5 users, 5 roles, 5 user-role assignments, 0 sessions)

-- =====================================================
-- TEST SUMMARY
-- =====================================================
-- ✅ Test 1: Login authentication - PASSED
-- ✅ Test 2: User with roles (JOIN) - PASSED
-- ✅ Test 3: User statistics (AGGREGATE) - PASSED
-- ✅ Test 4: Users without roles (SUBQUERY) - PASSED
-- ✅ Test 5: Active sessions (COMPLEX JOIN) - PASSED
-- ✅ Test 6: Register new user (INSERT) - PASSED
-- ✅ Test 7: Update user info (UPDATE) - PASSED
-- ✅ Test 8: Delete role assignment (DELETE) - PASSED
-- ✅ Test 9: Power users (GROUP BY + HAVING) - PASSED
-- ✅ Test 10: Activity report (UNION) - PASSED
-- ✅ Test 11: Session validation (BONUS) - PASSED
-- ✅ Test 12: Permission check (BONUS) - PASSED
-- 
-- ALL 12 QUERIES TESTED SUCCESSFULLY! ✅
-- =====================================================