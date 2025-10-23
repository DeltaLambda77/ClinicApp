<?php
include('connection.php');

$message = '';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $sql = "INSERT INTO Patient (FirstName, LastName, DateOfBirth, Sex, ContactInfo) 
            VALUES (?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param(
        "sssss",
        $_POST['FirstName'],
        $_POST['LastName'],
        $_POST['DateOfBirth'],
        $_POST['Sex'],
        $_POST['ContactInfo']
    );

    if ($stmt->execute()) {
        $message = "Patient added successfully! ID: " . $stmt->insert_id;
    } else {
        $message = "Error: " . $stmt->error;
    }
}
?>
<!DOCTYPE html>
<html>
<head>
    <title>Add Patient</title>
    <style>
        body { font-family: Arial; margin: 20px; }
        form { background: #f2f2f2; padding: 20px; width: 400px; border-radius: 8px; }
        input, select { width: 100%; padding: 8px; margin: 8px 0; border: 1px solid #ccc; border-radius: 4px; }
        
        
        label.required::after {
            content: " *";
            color: red;
            font-weight: bold;
        }

        
        button { 
            background: #4CAF50; 
            color: white; 
            padding: 10px; 
            border: none; 
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        
        button:hover {
            outline: 2px solid #333; 
            background: #45a049;
        }

        .success { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>
    <h1>Add New Patient</h1>
    <a href="index.php">← Back to Home</a>

    <?php if ($message) echo "<p class='success'>$message</p>"; ?>

    <form method="POST">
        <label class="required">First Name:</label>
        <input type="text" name="FirstName" required>

        <label class="required">Last Name:</label>
        <input type="text" name="LastName" required>

        <label class="required">Date of Birth:</label>
        <input type="date" name="DateOfBirth" required max="<?= date('Y-m-d') ?>">

        <label class="required">Sex:</label>
        <select name="Sex" required>
            <option value="M">Male</option>
            <option value="F">Female</option>
            <option value="O">Other</option>
        </select>

        <label>Contact Info(Optional):</label>
        <input type="text" name="ContactInfo" placeholder="email@example.com">

        <button type="submit">Add Patient</button>
    </form>
</body>
</html>