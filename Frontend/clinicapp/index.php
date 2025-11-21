<?php 
include('connection.php'); 

// Get some basic stats for the homepage
$patient_count = 0;
$trial_count = 0;

$result = $conn->query("SELECT COUNT(*) as count FROM Patient");
if ($result) {
    $row = $result->fetch_assoc();
    $patient_count = $row['count'];
}

$result = $conn->query("SELECT COUNT(*) as count FROM Trial");
if ($result) {
    $row = $result->fetch_assoc();
    $trial_count = $row['count'];
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clinical Trial Management System</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        Clinical Trial Management System
    </header>
    <nav>
        <a href="index.php" class="active">Home</a>
        <a href="patients.php">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="eligibility.php">Check Eligibility</a>
        <a href="modify_patient.php">Modify Patients</a>
    </nav>
    <main>
        <h2>System Overview</h2>
        <p>Welcome to the Clinical Trial Management System - Your comprehensive solution for managing clinical trials and patient eligibility.</p>
        
        <div style="background: #f0f8ff; padding: 15px; margin: 20px 0; border-radius: 5px;">
            <h3>Quick Stats</h3>
            <p><strong>Total Patients:</strong> <?= $patient_count ?></p>
            <p><strong>Total Trials:</strong> <?= $trial_count ?></p>
        </div>

        <h3>Features</h3>
        <ul>
            <li><strong>View Patients:</strong> Access a complete list of all patients with demographic details, statistics, and age distribution analysis.</li>
            <li><strong>Add Patients:</strong> Easily register new patients for clinical trials with comprehensive data validation.</li>
            <li><strong>Check Eligibility:</strong> Determine which patients meet the requirements for specific trials with advanced eligibility analysis.</li>
            <li><strong>Modify Patients:</strong> Edit patient information or remove patients from the system with proper safeguards.</li>
        </ul>

        <div style="background: #fff3cd; padding: 15px; margin: 20px 0; border-radius: 5px;">
            <h3>Database Information</h3>
            <p>This system uses a normalized MySQL database with the following tables:</p>
            <ul>
                <li>Patient - Core patient demographic information</li>
                <li>Trial - Clinical trial details and requirements</li>
                <li>MedicalCondition - Medical conditions and diagnoses</li>
                <li>Medication - Medication information</li>
                <li>PatientCondition - Patient-Condition relationships</li>
                <li>PatientMedication - Patient-Medication relationships</li>
                <li>TrialRequirement - Trial eligibility requirements</li>
                <li>Users & Roles - User authentication and authorization</li>
            </ul>
        </div>
    </main>
</body>
</html>