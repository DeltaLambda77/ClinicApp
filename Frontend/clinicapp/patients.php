<?php
include('connection.php');

// QUERY TYPE: SELECT
$patients_result = $conn->query("SELECT PatientID, FirstName, LastName, DATE_FORMAT(DateOfBirth,'%Y-%m-%d') AS DOB, Sex, ContactInfo FROM Patient");
$patients = [];
if ($patients_result) {
    while ($row = $patients_result->fetch_assoc()) {
        $patients[] = $row;
    }
}

// QUERY TYPE: AGGREGATION FUNCTIONS
$stats_result = $conn->query("
    SELECT 
        COUNT(*) AS TotalPatients,
        AVG(TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE())) AS AvgAge,
        SUM(CASE WHEN Sex='M' THEN 1 ELSE 0 END) AS MaleCount,
        SUM(CASE WHEN Sex='F' THEN 1 ELSE 0 END) AS FemaleCount,
        SUM(CASE WHEN Sex='O' THEN 1 ELSE 0 END) AS OtherCount
    FROM Patient
");
$stats = $stats_result->fetch_assoc();

// QUERY TYPE: GROUP BY / HAVING
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
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Patients</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>Clinical Trial Management System</header>
    <nav>
        <a href="index.php">Home</a>
        <a href="patients.php">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="eligibility.php">Check Eligibility</a>
        <a href="delete_patient.php">Modify Patients</a>
    </nav>

    <main>
        <h2>All Patients</h2>
        <table>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Date of Birth</th>
                <th>Sex</th>
                <th>Contact Info</th>
            </tr>
            <?php foreach ($patients as $p): ?>
            <tr>
                <td><?= $p['PatientID'] ?></td>
                <td><?= $p['FirstName'] ?></td>
                <td><?= $p['LastName'] ?></td>
                <td><?= $p['DOB'] ?></td>
                <td><?= $p['Sex'] ?></td>
                <td><?= $p['ContactInfo'] ?></td>
            </tr>
            <?php endforeach; ?>
        </table>

        <h2>Patient Statistics</h2>
        <table>
            <tr>
                <th>Total Patients</th>
                <th>Average Age</th>
                <th>Male</th>
                <th>Female</th>
                <th>Other</th>
            </tr>
            <tr>
                <td><?= $stats['TotalPatients'] ?></td>
                <td><?= round($stats['AvgAge'], 1) ?></td>
                <td><?= $stats['MaleCount'] ?></td>
                <td><?= $stats['FemaleCount'] ?></td>
                <td><?= $stats['OtherCount'] ?></td>
            </tr>
        </table>

        <h2>Patient Age Distribution</h2>
        <table>
            <tr>
                <th>Age Group</th>
                <th>Patient Count</th>
            </tr>
            <?php foreach ($age_groups as $group): ?>
            <tr>
                <td><?= $group['AgeGroup'] ?></td>
                <td><?= $group['PatientCount'] ?></td>
            </tr>
            <?php endforeach; ?>
        </table>
    </main>
</body>
</html>