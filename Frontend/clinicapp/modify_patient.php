<?php
include('connection.php');

// Fetches all patients
$result = $conn->query("SELECT * FROM Patient");
$patients = $result->fetch_all(MYSQLI_ASSOC);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modify Patients</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>Modify Patients</header>

    <nav>
        <a href="index.php">Home</a>
        <a href="patients.php">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="eligibility.php">Check Eligibility</a>
        <a href="modify_patient.php">Modify Patients</a>
    </nav>

    <main>
        <h2>Modify Patients (Update/Delete)</h2>
        <table>
            <tr>
                <th>Patient ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Date of Birth</th>
                <th>Sex</th>
                <th>Contact Info</th>
                <th>Actions</th>
            </tr>
            <?php foreach($patients as $patient): ?>
            <tr>
                <td><?= $patient['PatientID'] ?></td>
                <td><?= $patient['FirstName'] ?></td>
                <td><?= $patient['LastName'] ?></td>
                <td><?= $patient['DateOfBirth'] ?></td>
                <td><?= $patient['Sex'] ?></td>
                <td><?= $patient['ContactInfo'] ?></td>
                <td>
                    <a href="update_patient.php?id=<?= $patient['PatientID'] ?>">Edit</a>
                    <a href="delete_patient.php?id=<?= $patient['PatientID'] ?>" onclick="return confirm('Are you sure you want to delete this patient?')">Delete</a>
                </td>
            </tr>
            <?php endforeach; ?>
        </table>
    </main>
</body>
</html>
