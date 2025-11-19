<?php
include('connection.php');
// QUERY TYPE: DELETE

$message = '';
$message_type = '';

// Handles delete request
if (isset($_POST['delete_id']) && !empty($_POST['delete_id'])) {
    $delete_id = intval($_POST['delete_id']);
    
    // SECURE: Use prepared statement
    $stmt = $conn->prepare("DELETE FROM Patient WHERE PatientID = ?");
    $stmt->bind_param("i", $delete_id);
    
    if ($stmt->execute()) {
        if ($stmt->affected_rows > 0) {
            $message = "Patient with ID $delete_id has been deleted successfully.";
            $message_type = 'success';
        } else {
            $message = "No patient found with ID $delete_id.";
            $message_type = 'error';
        }
    } else {
        $message = "Error deleting patient: " . $conn->error;
        $message_type = 'error';
    }
    
    $stmt->close();
}

// Fetches all patients for display
$patients = [];
$result = $conn->query("SELECT PatientID, FirstName, LastName FROM Patient ORDER BY LastName, FirstName");
if ($result && $result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $patients[] = $row;
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delete Patients</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<header>Clinical Trial Management System</header>
<nav>
    <a href="index.php">Home</a>
    <a href="patients.php">View Patients</a>
    <a href="add_patient.php">Add Patient</a>
    <a href="eligibility.php">Check Eligibility</a>
    <a href="modify_patient.php">Modify Patients</a>
</nav>

<main>
    <h2>Delete a Patient</h2>

    <?php if (isset($message)) echo "<p style='color:red;'>$message</p>"; ?>

    <form method="post">
        <label for="delete_id">Select Patient to Delete:</label>
        <select name="delete_id" id="delete_id" required>
            <option value="">--Select Patient--</option>
            <?php foreach ($patients as $p): ?>
                <option value="<?= $p['PatientID'] ?>">
                    <?= $p['PatientID'] ?> - <?= $p['FirstName'] . ' ' . $p['LastName'] ?>
                </option>
            <?php endforeach; ?>
        </select>
        <button type="submit">Delete Patient</button>
    </form>
</main>
</body>
</html>
