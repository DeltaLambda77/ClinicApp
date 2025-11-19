-- =====================================================
-- COMPLETE PATIENT-TRIAL MATCHING DATABASE
-- =====================================================
-- Includes: Patient/Trial tables + Authentication system
-- Gabriela Rodriguez   PID: 6442196
-- =====================================================

-- Drop all tables in correct order (due to foreign keys)
DROP TABLE IF EXISTS UserSessions;
DROP TABLE IF EXISTS UserRoles;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Roles;
DROP TABLE IF EXISTS PatientMedication;
DROP TABLE IF EXISTS PatientCondition;
DROP TABLE IF EXISTS TrialRequirement;
DROP TABLE IF EXISTS Trial;
DROP TABLE IF EXISTS Medication;
DROP TABLE IF EXISTS MedicalCondition;
DROP TABLE IF EXISTS Patient;

-- =====================================================
-- PART 1: PATIENT-TRIAL MATCHING TABLES
-- =====================================================

CREATE TABLE Patient (
    PatientID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Sex ENUM('M', 'F', 'Other') NOT NULL,
    ContactInfo VARCHAR(100)
);

CREATE TABLE MedicalCondition (
    ConditionID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL UNIQUE,
    Code VARCHAR(20) UNIQUE,
    Description TEXT
);

CREATE TABLE Medication (
    MedicationID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL UNIQUE,
    Dosage VARCHAR(50)
);

CREATE TABLE Trial (
    TrialID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(200) NOT NULL,
    MinimumAge INT,
    MaximumAge INT,
    StartDate DATE NOT NULL,
    EndDate DATE
);

CREATE TABLE TrialRequirement (
    RequirementID INT PRIMARY KEY AUTO_INCREMENT,
    TrialID INT NOT NULL,
    ConditionID INT,
    ExcludedMedicationID INT,
    Notes TEXT,
    FOREIGN KEY (TrialID) REFERENCES Trial(TrialID) ON DELETE CASCADE,
    FOREIGN KEY (ConditionID) REFERENCES MedicalCondition(ConditionID) ON DELETE CASCADE,
    FOREIGN KEY (ExcludedMedicationID) REFERENCES Medication(MedicationID) ON DELETE CASCADE
);

CREATE TABLE PatientCondition (
    PatientID INT NOT NULL,
    ConditionID INT NOT NULL,
    DiagnosisDate DATE NOT NULL,
    PRIMARY KEY (PatientID, ConditionID),
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (ConditionID) REFERENCES MedicalCondition(ConditionID) ON DELETE CASCADE
);

CREATE TABLE PatientMedication (
    PatientID INT NOT NULL,
    MedicationID INT NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE,
    PRIMARY KEY (PatientID, MedicationID),
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (MedicationID) REFERENCES Medication(MedicationID) ON DELETE CASCADE
);

-- =====================================================
-- PART 2: AUTHENTICATION TABLES
-- =====================================================

CREATE TABLE Roles (
    RoleID INT PRIMARY KEY AUTO_INCREMENT,
    RoleName VARCHAR(50) NOT NULL UNIQUE,
    Description TEXT,
    CanAddPatients BOOLEAN DEFAULT FALSE,
    CanViewTrials BOOLEAN DEFAULT FALSE,
    CanManageTrials BOOLEAN DEFAULT FALSE,
    CanViewReports BOOLEAN DEFAULT FALSE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Users (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Email VARCHAR(100) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    IsActive BOOLEAN DEFAULT TRUE,
    LastLogin TIMESTAMP NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE UserRoles (
    UserID INT NOT NULL,
    RoleID INT NOT NULL,
    AssignedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    AssignedBy INT,
    PRIMARY KEY (UserID, RoleID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) ON DELETE CASCADE,
    FOREIGN KEY (AssignedBy) REFERENCES Users(UserID) ON DELETE SET NULL
);

CREATE TABLE UserSessions (
    SessionID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT NOT NULL,
    SessionToken VARCHAR(255) NOT NULL UNIQUE,
    IPAddress VARCHAR(45),
    UserAgent VARCHAR(255),
    LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    LastActivityTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    IsActive BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- =====================================================
-- SAMPLE DATA - PART 1: PATIENT/TRIAL DATA
-- =====================================================

INSERT INTO MedicalCondition (Name, Code, Description) VALUES
('Type 2 Diabetes', 'E11', 'Non-insulin-dependent diabetes mellitus'),
('Hypertension', 'I10', 'Essential (primary) hypertension'),
('Asthma', 'J45', 'Chronic inflammatory disease of airways'),
('Rheumatoid Arthritis', 'M05', 'Autoimmune disorder affecting joints'),
('Major Depressive Disorder', 'F32', 'Persistent feeling of sadness and loss of interest'),
('Chronic Kidney Disease', 'N18', 'Progressive loss of kidney function'),
('Coronary Artery Disease', 'I25', 'Damage to heart blood vessels'),
('COPD', 'J44', 'Chronic Obstructive Pulmonary Disease'),
('Migraine', 'G43', 'Recurrent severe headaches'),
('Osteoarthritis', 'M19', 'Degenerative joint disease');

INSERT INTO Medication (Name, Dosage) VALUES
('Metformin', '500mg twice daily'),
('Lisinopril', '10mg once daily'),
('Albuterol', '90mcg as needed'),
('Methotrexate', '15mg weekly'),
('Sertraline', '50mg once daily'),
('Atorvastatin', '20mg once daily'),
('Prednisone', '5mg once daily'),
('Insulin Glargine', '20 units at bedtime'),
('Aspirin', '81mg once daily'),
('Ibuprofen', '400mg as needed'),
('Warfarin', '5mg once daily'),
('Levothyroxine', '100mcg once daily');

INSERT INTO Patient (FirstName, LastName, DateOfBirth, Sex, ContactInfo) VALUES
('Maria', 'Garcia', '1965-03-15', 'F', 'maria.garcia@email.com'),
('James', 'Wilson', '1958-07-22', 'M', 'james.wilson@email.com'),
('Sarah', 'Chen', '1972-11-08', 'F', 'sarah.chen@email.com'),
('Robert', 'Johnson', '1980-05-30', 'M', 'robert.j@email.com'),
('Lisa', 'Martinez', '1955-12-18', 'F', 'lisa.m@email.com'),
('David', 'Brown', '1968-09-25', 'M', 'david.brown@email.com'),
('Jennifer', 'Lee', '1975-04-12', 'F', 'jennifer.lee@email.com'),
('Michael', 'Davis', '1963-08-07', 'M', 'michael.d@email.com'),
('Patricia', 'Anderson', '1970-02-20', 'F', 'patricia.a@email.com'),
('William', 'Taylor', '1952-06-14', 'M', 'william.t@email.com'),
('Emily', 'Rodriguez', '1985-10-03', 'F', 'emily.r@email.com'),
('Daniel', 'Martinez', '1990-01-25', 'M', 'daniel.m@email.com'),
('Jessica', 'Hernandez', '1978-07-19', 'F', 'jessica.h@email.com'),
('Christopher', 'Lopez', '1966-12-08', 'M', 'chris.lopez@email.com'),
('Amanda', 'Gonzalez', '1982-03-28', 'F', 'amanda.g@email.com');

INSERT INTO Trial (Title, MinimumAge, MaximumAge, StartDate, EndDate) VALUES
('Novel Diabetes Treatment Study', 40, 75, '2024-01-15', '2025-12-31'),
('Hypertension Control Trial', 35, 70, '2024-03-01', '2025-06-30'),
('Asthma Management Research', 18, 65, '2024-02-01', '2025-08-31'),
('Arthritis Pain Relief Study', 45, 80, '2024-04-01', '2026-03-31'),
('Depression Treatment Comparison', 25, 60, '2024-01-01', '2025-12-31'),
('Cardiovascular Health Initiative', 50, 85, '2024-05-01', '2026-04-30'),
('Kidney Disease Prevention Trial', 40, 75, '2024-06-01', '2025-12-31'),
('Migraine Prevention Study', 21, 65, '2024-03-15', '2025-09-30');

INSERT INTO TrialRequirement (TrialID, ConditionID, ExcludedMedicationID, Notes) VALUES
(1, 1, NULL, 'Must have Type 2 Diabetes diagnosis'),
(1, NULL, 8, 'Cannot be currently using insulin therapy'),
(2, 2, NULL, 'Must have diagnosed hypertension'),
(2, NULL, 11, 'Cannot be on blood thinners'),
(3, 3, NULL, 'Must have confirmed asthma diagnosis'),
(4, 4, NULL, 'Must have rheumatoid arthritis'),
(4, NULL, 7, 'Cannot be on long-term corticosteroids'),
(5, 5, NULL, 'Must have major depressive disorder diagnosis'),
(6, 7, NULL, 'Must have coronary artery disease'),
(7, 6, NULL, 'Must have chronic kidney disease'),
(7, NULL, 10, 'Cannot be taking NSAIDs regularly'),
(8, 9, NULL, 'Must experience chronic migraines');

INSERT INTO PatientCondition (PatientID, ConditionID, DiagnosisDate) VALUES
(1, 1, '2020-05-10'), (1, 2, '2019-03-15'),
(2, 2, '2015-07-20'), (2, 7, '2018-11-05'),
(3, 3, '2010-09-12'), (3, 9, '2022-01-08'),
(4, 1, '2021-03-25'),
(5, 4, '2016-06-18'), (5, 10, '2020-09-30'),
(6, 2, '2017-04-12'), (6, 1, '2019-08-22'),
(7, 5, '2021-02-14'),
(8, 8, '2014-11-30'), (8, 2, '2016-05-17'),
(9, 6, '2022-03-10'), (9, 2, '2020-07-25'),
(10, 7, '2012-09-08'), (10, 1, '2015-12-20'),
(11, 3, '2023-01-15'),
(12, 9, '2022-06-30'),
(13, 1, '2020-10-05'), (13, 2, '2021-03-18'),
(14, 4, '2019-07-22'),
(15, 5, '2021-11-12'), (15, 9, '2020-04-08');

INSERT INTO PatientMedication (PatientID, MedicationID, StartDate, EndDate) VALUES
(1, 1, '2020-05-15', NULL), (1, 2, '2019-03-20', NULL),
(2, 2, '2015-07-25', NULL), (2, 6, '2018-11-10', NULL),
(2, 9, '2018-11-10', NULL), (2, 11, '2019-02-01', NULL),
(3, 3, '2010-09-15', NULL),
(4, 1, '2021-04-01', NULL),
(5, 4, '2016-06-25', NULL), (5, 7, '2020-10-01', NULL),
(6, 2, '2017-04-20', NULL), (6, 1, '2019-09-01', NULL),
(7, 5, '2021-02-20', NULL),
(8, 3, '2014-12-05', NULL), (8, 2, '2016-05-25', NULL),
(9, 2, '2020-08-01', NULL), (9, 10, '2022-03-15', NULL),
(10, 8, '2016-01-10', NULL), (10, 6, '2012-09-15', NULL),
(10, 9, '2012-09-15', NULL),
(11, 3, '2023-01-20', NULL),
(13, 1, '2020-10-10', NULL), (13, 2, '2021-03-25', NULL),
(14, 4, '2019-08-01', NULL),
(15, 5, '2021-11-20', NULL);

-- =====================================================
-- SAMPLE DATA - PART 2: AUTHENTICATION DATA
-- =====================================================

INSERT INTO Roles (RoleName, Description, CanAddPatients, CanViewTrials, CanManageTrials, CanViewReports) VALUES
('Admin', 'System administrator with full access', TRUE, TRUE, TRUE, TRUE),
('Researcher', 'Clinical researcher who can manage trials and view reports', FALSE, TRUE, TRUE, TRUE),
('Clinician', 'Medical staff who can add patients and view trials', TRUE, TRUE, FALSE, TRUE),
('Data Analyst', 'Analyst who can view reports but not modify data', FALSE, TRUE, FALSE, TRUE),
('Patient', 'Patient who can view their own eligibility', FALSE, TRUE, FALSE, FALSE);

INSERT INTO Users (Username, Email, PasswordHash, FirstName, LastName, IsActive) VALUES
('admin', 'admin@clinicapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Alice', 'Admin', TRUE),
('researcher1', 'dr.smith@clinicapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'John', 'Smith', TRUE),
('clinician1', 'dr.jones@clinicapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Emily', 'Jones', TRUE),
('analyst1', 'analyst@clinicapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'David', 'Chen', TRUE),
('patient1', 'maria.garcia@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Maria', 'Garcia', TRUE);

INSERT INTO UserRoles (UserID, RoleID, AssignedBy) VALUES
(1, 1, NULL),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1);

-- =====================================================
-- CREATE INDEXES
-- =====================================================

CREATE INDEX idx_patient_dob ON Patient(DateOfBirth);
CREATE INDEX idx_patient_name ON Patient(LastName, FirstName);
CREATE INDEX idx_trial_dates ON Trial(StartDate, EndDate);
CREATE INDEX idx_trial_age ON Trial(MinimumAge, MaximumAge);
CREATE INDEX idx_users_username ON Users(Username);
CREATE INDEX idx_users_email ON Users(Email);
CREATE INDEX idx_sessions_token ON UserSessions(SessionToken);

-- =====================================================
-- END OF COMPLETE DATABASE
-- =====================================================