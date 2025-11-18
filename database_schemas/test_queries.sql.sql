-- Test 1: Count all records
SELECT 'Patients' AS TableName, COUNT(*) AS Count FROM Patient
UNION ALL SELECT 'Conditions', COUNT(*) FROM MedicalCondition
UNION ALL SELECT 'Medications', COUNT(*) FROM Medication
UNION ALL SELECT 'Trials', COUNT(*) FROM Trial;

-- Test 2: Show all patients
SELECT * FROM Patient;

-- Test 3: Show trials with their requirements
SELECT t.Title, tr.Notes 
FROM Trial t
LEFT JOIN TrialRequirement tr ON t.TrialID = tr.TrialID;