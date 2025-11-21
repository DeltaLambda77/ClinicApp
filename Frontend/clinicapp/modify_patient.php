<?php
include('connection.php');

$patient = null;
$message = '';
$message_type = '';

// QUERY TYPE: UPDATE
// Check if a patient ID is provided
if (isset($_GET['id'])) {
    $patient_id = intval($_GET['id']);
    
    // Fetch patient data using prepared statement
    $stmt = $conn->prepare("SELECT * FROM Patient WHERE PatientID = ?");
    $stmt->bind_param("i", $patient_id);
    $stmt->execute();
    $result = $stmt->get_result();
    $patient = $result->fetch_assoc();
    $stmt->close();
    
    if (!$patient) {
        $message = "Patient not found with ID: $patient_id";
        $message_type = 'error';
    }
}

// Handle form submission
if (isset($_POST['update'])) {
    $id = intval($_POST['PatientID']);
    $firstName = trim($_POST['FirstName']);
    $lastName = trim($_POST['LastName']);
    $dob = $_POST['DateOfBirth'];
    $sex = $_POST['Sex'];
    $contact = trim($_POST['ContactInfo']);
    
    // Validate inputs
    if (empty($firstName) || empty($lastName) || empty($dob) || empty($sex)) {
        $message = "Error: All required fields must be filled out.";
        $message_type = 'error';
    } else {
        // Use prepared statement for security
        $update_sql = "UPDATE Patient SET 
                       FirstName = ?, 
                       LastName = ?, 
                       DateOfBirth = ?, 
                       Sex = ?, 
                       ContactInfo = ? 
                       WHERE PatientID = ?";
        
        $stmt = $conn->prepare($update_sql);
        $stmt->bind_param("sssssi", $firstName, $lastName, $dob, $sex, $contact, $id);
        
        if ($stmt->execute()) {
            if ($stmt->affected_rows > 0) {
                $message = "Patient updated successfully!";
                $message_type = 'success';
                
                // Refresh patient data
                $stmt2 = $conn->prepare("SELECT * FROM Patient WHERE PatientID = ?");
                $stmt2->bind_param("i", $id);
                $stmt2->execute();
                $patient = $stmt2->get_result()->fetch_assoc();
                $stmt2->close();
            } else {
                $message = "No changes were made to the patient record.";
                $message_type = 'warning';
            }
        } else {
            $message = "Error updating patient: " . $conn->error;
            $message_type = 'error';
        }
        $stmt->close();
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Patient - Clinical Trial Management</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .form-container {
            background: #f9f9f9;
            padding: 25px;
            max-width: 500px;
            margin: 20px auto;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .submit-btn {
            background: #4CAF50;
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
        }
        .submit-btn:hover {
            background: #45a049;
        }
        .message {
            padding: 12px;
            margin: 15px 0;
            border-radius: 4px;
            font-weight: bold;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .warning {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }
        .required {
            color: red;
        }
        .patient-info {
            background: #e7f3ff;
            padding: 15px;
            margin: 15px 0;
            border-radius: 5px;
            border-left: 4px solid #2196F3;
        }
    </style>
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
        <h2>Update Patient Information</h2>
        
        <?php if ($message): ?>
            <div class="message <?= $message_type ?>">
                <?= htmlspecialchars($message) ?>
            </div>
        <?php endif; ?>

        <?php if (isset($patient) && $patient): ?>
            <div class="patient-info">
                <h3>Current Patient: <?= htmlspecialchars($patient['FirstName'] . ' ' . $patient['LastName']) ?></h3>
                <p><strong>Patient ID:</strong> <?= $patient['PatientID'] ?></p>
                <p><strong>Current Age:</strong> <?= date_diff(date_create($patient['DateOfBirth']), date_create('today'))->y ?> years</p>
            </div>

            <div class="form-container">
                <form method="POST" action="">
                    <input type="hidden" name="PatientID" value="<?= $patient['PatientID'] ?>">
                    
                    <div class="form-group">
                        <label for="FirstName">First Name <span class="required">*</span></label>
                        <input type="text" 
                               id="FirstName" 
                               name="FirstName" 
                               value="<?= htmlspecialchars($patient['FirstName']) ?>" 
                               required 
                               maxlength="50">
                    </div>

                    <div class="form-group">
                        <label for="LastName">Last Name <span class="required">*</span></label>
                        <input type="text" 
                               id="LastName" 
                               name="LastName" 
                               value="<?= htmlspecialchars($patient['LastName']) ?>" 
                               required 
                               maxlength="50">
                    </div>

                    <div class="form-group">
                        <label for="DateOfBirth">Date of Birth <span class="required">*</span></label>
                        <input type="date" 
                               id="DateOfBirth" 
                               name="DateOfBirth" 
                               value="<?= htmlspecialchars($patient['DateOfBirth']) ?>" 
                               required 
                               max="<?= date('Y-m-d') ?>">
                    </div>

                    <div class="form-group">
                        <label for="Sex">Sex <span class="required">*</span></label>
                        <select id="Sex" name="Sex" required>
                            <option value="M" <?= $patient['Sex'] == 'M' ? 'selected' : '' ?>>Male</option>
                            <option value="F" <?= $patient['Sex'] == 'F' ? 'selected' : '' ?>>Female</option>
                            <option value="Other" <?= $patient['Sex'] == 'Other' ? 'selected' : '' ?>>Other</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="ContactInfo">Contact Info</label>
                        <input type="text" 
                               id="ContactInfo" 
                               name="ContactInfo" 
                               value="<?= htmlspecialchars($patient['ContactInfo'] ?? '') ?>" 
                               maxlength="100">
                    </div>

                    <button type="submit" name="update" class="submit-btn">Update Patient</button>
                </form>
            </div>

            <p style="text-align: center; margin-top: 20px;">
                <a href="modify_patient.php">← Back to Patient List</a>
            </p>
        <?php else: ?>
            <p class="error" style="text-align: center;">
                No patient selected or patient not found. 
                <a href="modify_patient.php">← Back to Patient List</a>
            </p>
        <?php endif; ?>
    </main>
</body>
</html>