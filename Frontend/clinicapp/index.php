<?php include('connection.php'); ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clinical Trial Management System - Milestone 2 Demo</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        Clinical Trial Management System
    </header>

    <nav>
        <a href="index.php">Home</a>
        <a href="patients.php">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="eligibility.php">Check Eligibility</a>
        <a href="modify_patient.php">Modify Patients</a>
    </nav>

    <main>
        <h2>System Overview</h2>
        <p>Welcome to the Clinical Trial Management System </p>
        <ul>
    <li><strong>View Patients:</strong> Access a list of all patients with demographic details along with statistics and a distribution of ages.</li>
    <li><strong>Add Patients:</strong> Easily register new patients for clinical trials.</li>
    <li><strong>Check Eligibility:</strong> Determines which patients meet the requierments for the trails and the additional analysis.</li>
    <li><strong>Modify Patients:</strong> Allows the editing of patient information or the ability to remove patients.</li>
    </ul>
    </main>
</body>
</html>
