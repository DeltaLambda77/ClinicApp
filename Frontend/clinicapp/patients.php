<?php
include('connection.php');

// QUERY TYPE 1: SELECT with conditions
$patients_result = $conn->query("
    SELECT PatientID, FirstName, LastName, 
           DATE_FORMAT(DateOfBirth,'%Y-%m-%d') AS DOB, 
           Sex, ContactInfo,
           TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) AS Age
    FROM Patient
    ORDER BY LastName, FirstName
");
$patients = [];
if ($patients_result) {
    while ($row = $patients_result->fetch_assoc()) {
        $patients[] = $row;
    }
}

// QUERY TYPE 2: AGGREGATION FUNCTIONS (COUNT, AVG, SUM)
$stats_result = $conn->query("
    SELECT 
        COUNT(*) AS TotalPatients,
        AVG(TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE())) AS AvgAge,
        MIN(TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE())) AS MinAge,
        MAX(TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE())) AS MaxAge,
        SUM(CASE WHEN Sex='M' THEN 1 ELSE 0 END) AS MaleCount,
        SUM(CASE WHEN Sex='F' THEN 1 ELSE 0 END) AS FemaleCount,
        SUM(CASE WHEN Sex='Other' THEN 1 ELSE 0 END) AS OtherCount
    FROM Patient
");
$stats = $stats_result ? $stats_result->fetch_assoc() : null;

// QUERY TYPE 3: GROUP BY / HAVING
$age_groups_result = $conn->query("
    SELECT 
        CASE 
            WHEN TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) < 18 THEN 'Under 18'
            WHEN TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) BETWEEN 18 AND 35 THEN '18-35'
            WHEN TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) BETWEEN 36 AND 55 THEN '36-55'
            ELSE 'Over 55'
        END AS AgeGroup,
        COUNT(*) AS PatientCount
    FROM Patient 
    GROUP BY AgeGroup
    HAVING COUNT(*) > 0
    ORDER BY MIN(TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()))
");
$age_groups = [];
if ($age_groups_result) {
    $age_groups = $age_groups_result->fetch_all(MYSQLI_ASSOC);
}

// QUERY TYPE 4: JOIN - Get patients with their conditions
$patients_with_conditions = $conn->query("
    SELECT p.PatientID, 
           CONCAT(p.FirstName, ' ', p.LastName) AS PatientName,
           COUNT(pc.ConditionID) AS ConditionCount
    FROM Patient p
    LEFT JOIN PatientCondition pc ON p.PatientID = pc.PatientID
    GROUP BY p.PatientID, PatientName
    HAVING ConditionCount > 0
    ORDER BY ConditionCount DESC
    LIMIT 10
");
$top_patients_conditions = [];
if ($patients_with_conditions) {
    $top_patients_conditions = $patients_with_conditions->fetch_all(MYSQLI_ASSOC);
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Patients - Clinical Trial Management</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>Clinical Trial Management System</header>
    <nav>
        <a href="index.php">Home</a>
        <a href="patients.php" class="active">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="eligibility.php">Check Eligibility</a>
        <a href="modify_patient.php">Modify Patients</a>
    </nav>
    <main>
        <h2>All Patients (<?= count($patients) ?>)</h2>
        <?php if (count($patients) > 0): ?>
        <table>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Date of Birth</th>
                <th>Age</th>
                <th>Sex</th>
                <th>Contact Info</th>
            </tr>
            <?php foreach ($patients as $p): ?>
            <tr>
                <td><?= htmlspecialchars($p['PatientID']) ?></td>
                <td><?= htmlspecialchars($p['FirstName']) ?></td>
                <td><?= htmlspecialchars($p['LastName']) ?></td>
                <td><?= htmlspecialchars($p['DOB']) ?></td>
                <td><?= htmlspecialchars($p['Age']) ?></td>
                <td><?= htmlspecialchars($p['Sex']) ?></td>
                <td><?= htmlspecialchars($p['ContactInfo'] ?? 'N/A') ?></td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php else: ?>
        <p style="color: #856404; background: #fff3cd; padding: 10px; border-radius: 5px;">
            No patients found in the database. <a href="add_patient.php">Add a patient</a> to get started.
        </p>
        <?php endif; ?>

        <h2>Patient Statistics</h2>
        <?php if ($stats): ?>
        <table>
            <tr>
                <th>Total Patients</th>
                <th>Average Age</th>
                <th>Min Age</th>
                <th>Max Age</th>
                <th>Male</th>
                <th>Female</th>
                <th>Other</th>
            </tr>
            <tr>
                <td><?= $stats['TotalPatients'] ?></td>
                <td><?= round($stats['AvgAge'], 1) ?> years</td>
                <td><?= $stats['MinAge'] ?? 'N/A' ?></td>
                <td><?= $stats['MaxAge'] ?? 'N/A' ?></td>
                <td><?= $stats['MaleCount'] ?></td>
                <td><?= $stats['FemaleCount'] ?></td>
                <td><?= $stats['OtherCount'] ?></td>
            </tr>
        </table>
        <?php endif; ?>

        <h2>Patient Age Distribution</h2>
        <?php if (count($age_groups) > 0): ?>
        <table>
            <tr>
                <th>Age Group</th>
                <th>Patient Count</th>
                <th>Percentage</th>
            </tr>
            <?php foreach ($age_groups as $group): ?>
            <tr>
                <td><?= htmlspecialchars($group['AgeGroup']) ?></td>
                <td><?= $group['PatientCount'] ?></td>
                <td><?= $stats ? round(($group['PatientCount'] / $stats['TotalPatients']) * 100, 1) : 0 ?>%</td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php endif; ?>

        <?php if (count($top_patients_conditions) > 0): ?>
        <h2>Patients with Medical Conditions (Top 10)</h2>
        <table>
            <tr>
                <th>Patient ID</th>
                <th>Patient Name</th>
                <th>Number of Conditions</th>
            </tr>
            <?php foreach ($top_patients_conditions as $patient): ?>
            <tr>
                <td><?= htmlspecialchars($patient['PatientID']) ?></td>
                <td><?= htmlspecialchars($patient['PatientName']) ?></td>
                <td><?= $patient['ConditionCount'] ?></td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php endif; ?>
    </main>
</body>
</html>