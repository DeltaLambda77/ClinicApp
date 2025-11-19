<?php
include('connection.php');
// QUERY TYPE: UPDATE

// Checks if a patient ID is provided
if (isset($_GET['id'])) {
    $patient_id = $_GET['id'];

    // Fetches patient data
    $result = $conn->query("SELECT * FROM Patient WHERE PatientID = $patient_id");
    $patient = $result->fetch_assoc();
}

// Handles form submission
if (isset($_POST['update'])) {
    $id = $_POST['PatientID'];
    $firstName = $_POST['FirstName'];
    $lastName = $_POST['LastName'];
    $dob = $_POST['DateOfBirth'];
    $sex = $_POST['Sex'];
    $contact = $_POST['ContactInfo'];

    $update_sql = "UPDATE Patient SET 
        FirstName='$firstName', 
        LastName='$lastName', 
        DateOfBirth='$dob', 
        Sex='$sex', 
        ContactInfo='$contact' 
        WHERE PatientID=$id";

    if ($conn->query($update_sql) === TRUE) {
        echo "<p style='color:green;'>Patient updated successfully.</p>";
    } else {
        echo "<p style='color:red;'>Error updating patient: " . $conn->error . "</p>";
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Patient</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>Clinical Trial Management System</header>

    <nav>
        <a href="index.php">Home</a>
        <a href="patients.php">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="check_eligibility.php">Check Eligibility</a>
    </nav>

    <main>
        <h2>Update Patient</h2>

        <?php if (isset($patient)): ?>
        <form method="POST" action="">
            <input type="hidden" name="PatientID" value="<?= $patient['PatientID'] ?>">

            <label>First Name*: <input type="text" name="FirstName" value="<?= $patient['FirstName'] ?>" required></label><br>
            <label>Last Name*: <input type="text" name="LastName" value="<?= $patient['LastName'] ?>" required></label><br>
            <label>Date of Birth*: <input type="date" name="DateOfBirth" value="<?= $patient['DateOfBirth'] ?>" required></label><br>
            <label>Sex*: 
                <select name="Sex" required>
                    <option value="M" <?= $patient['Sex']=='M'?'selected':'' ?>>M</option>
                    <option value="F" <?= $patient['Sex']=='F'?'selected':'' ?>>F</option>
                    <option value="O" <?= $patient['Sex']=='O'?'selected':'' ?>>O</option>
                </select>
            </label><br>
            <label>Contact Info: <input type="text" name="ContactInfo" value="<?= $patient['ContactInfo'] ?>"></label><br><br>

            <button type="submit" name="update">Update Patient</button>
        </form>
        <?php else: ?>
            <p style="color:red;">No patient selected.</p>
        <?php endif; ?>
    </main>
</body>
</html>
