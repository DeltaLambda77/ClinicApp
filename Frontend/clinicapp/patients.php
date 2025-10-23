<?php
include('connection.php');

$sql = "SELECT PatientID, CONCAT(FirstName, ' ', LastName) AS Name, 
        TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) AS Age, Sex, ContactInfo
        FROM Patient
        ORDER BY LastName, FirstName";

$result = $conn->query($sql);

$patients = [];
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $patients[] = $row;
    }
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
    <header>View Patients</header>

    <nav>
        <a href="index.php">Home</a>
        <a href="patients.php">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="eligibility.php">Check Eligibility</a>
    </nav>

    <main>
        <h2>Patient List</h2>

        
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Age</th>
                <th>Sex</th>
                <th>Contact Info</th>
            </tr>
            <?php foreach ($patients as $p): ?>
            <tr>
                <td><?= $p['PatientID'] ?></td>
                <td><?= $p['Name'] ?></td>
                <td><?= $p['Age'] ?></td>
                <td><?= $p['Sex'] ?></td>
                <td><?= $p['ContactInfo'] ?></td>
            </tr>
            <?php endforeach; ?>
        </table>
    </main>
</body>
</html>