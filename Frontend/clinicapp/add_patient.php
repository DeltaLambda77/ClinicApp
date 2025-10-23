<?php
include('api_config.php');

$message = '';
$messageClass = '';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $patientData = [
        'firstName' => $_POST['FirstName'],
        'lastName' => $_POST['LastName'],
        'dateOfBirth' => $_POST['DateOfBirth'],
        'sex' => $_POST['Sex'],
        'contactInfo' => $_POST['ContactInfo']
    ];
    
    $response = callAPI('POST', '/patients', $patientData);
    
    if ($response['status'] == 200 || $response['status'] == 201) {
        $patientId = $response['data']['patientId'] ?? 'Unknown';
        $message = "Patient added successfully! ID: " . $patientId;
        $messageClass = 'success';
    } else {
        $message = "Error adding patient. Please try again.";
        $messageClass = 'error';
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
    <?php if ($message): ?>
        <p class="<?= $messageClass ?>"><?= htmlspecialchars($message) ?></p>
    <?php endif; ?>
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
        
        <label>Contact Info (Optional):</label>
        <input type="text" name="ContactInfo" placeholder="email@example.com">
        
        <button type="submit">Add Patient</button>
    </form>
</body>
</html>