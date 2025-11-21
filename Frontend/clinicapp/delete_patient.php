<?php
include('connection.php');

// QUERY TYPE: DELETE
$message = '';
$message_type = '';
$patient_to_delete = null;

// Check if we're confirming a deletion
if (isset($_GET['id']) && !isset($_POST['confirm_delete'])) {
    $patient_id = intval($_GET['id']);
    
    // Fetch patient details for confirmation
    $stmt = $conn->prepare("SELECT PatientID, FirstName, LastName, DateOfBirth, Sex FROM Patient WHERE PatientID = ?");
    $stmt->bind_param("i", $patient_id);
    $stmt->execute();
    $patient_to_delete = $stmt->get_result()->fetch_assoc();
    $stmt->close();
    
    if (!$patient_to_delete) {
        $message = "Patient not found with ID: $patient_id";
        $message_type = 'error';
    }
}

// Handle confirmed deletion
if (isset($_POST['confirm_delete']) && isset($_POST['delete_id'])) {
    $delete_id = intval($_POST['delete_id']);
    
    // First, check if patient exists and get their info
    $stmt = $conn->prepare("SELECT FirstName, LastName FROM Patient WHERE PatientID = ?");
    $stmt->bind_param("i", $delete_id);
    $stmt->execute();
    $patient_info = $stmt->get_result()->fetch_assoc();
    $stmt->close();
    
    if ($patient_info) {
        // Delete associated records first (if not using CASCADE)
        // Note: If your database has CASCADE DELETE set up, this isn't necessary
        
        // Delete PatientCondition records
        $stmt = $conn->prepare("DELETE FROM PatientCondition WHERE PatientID = ?");
        $stmt->bind_param("i", $delete_id);
        $stmt->execute();
        $stmt->close();
        
        // Delete PatientMedication records
        $stmt = $conn->prepare("DELETE FROM PatientMedication WHERE PatientID = ?");
        $stmt->bind_param("i", $delete_id);
        $stmt->execute();
        $stmt->close();
        
        // Now delete the patient
        $stmt = $conn->prepare("DELETE FROM Patient WHERE PatientID = ?");
        $stmt->bind_param("i", $delete_id);
        
        if ($stmt->execute()) {
            if ($stmt->affected_rows > 0) {
                $message = "Patient '{$patient_info['FirstName']} {$patient_info['LastName']}' (ID: $delete_id) has been deleted successfully.";
                $message_type = 'success';
            } else {
                $message = "No patient found with ID: $delete_id";
                $message_type = 'error';
            }
        } else {
            $message = "Error deleting patient: " . $conn->error;
            $message_type = 'error';
        }
        $stmt->close();
    } else {
        $message = "Patient not found with ID: $delete_id";
        $message_type = 'error';
    }
}

// Fetch all patients for the selection dropdown
$patients = [];
$result = $conn->query("SELECT PatientID, FirstName, LastName, DateOfBirth, Sex FROM Patient ORDER BY LastName, FirstName");
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
    <title>Delete Patient - Clinical Trial Management</title>
    <link rel="stylesheet" href="styles.css">
    <style>
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
        .confirmation-box {
            background: #fff3cd;
            border: 2px solid #ffc107;
            padding: 20px;
            margin: 20px 0;
            border-radius: 5px;
        }
        .confirmation-box h3 {
            color: #856404;
            margin-top: 0;
        }
        .patient-details {
            background: #f9f9f9;
            padding: 15px;
            margin: 15px 0;
            border-radius: 5px;
            border-left: 4px solid #f44336;
        }
        .button-group {
            margin-top: 15px;
        }
        .btn-confirm {
            background: #f44336;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        .btn-confirm:hover {
            background: #da190b;
        }
        .btn-cancel {
            background: #6c757d;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            display: inline-block;
        }
        .btn-cancel:hover {
            background: #5a6268;
        }
        .form-container {
            background: #f9f9f9;
            padding: 25px;
            max-width: 500px;
            margin: 20px auto;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        select {
            width: 100%;
            padding: 8px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .delete-btn {
            background: #f44336;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
        }
        .delete-btn:hover {
            background: #da190b;
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
        <h2>Delete a Patient</h2>
        
        <?php if ($message): ?>
            <div class="message <?= $message_type ?>">
                <?= htmlspecialchars($message) ?>
            </div>
        <?php endif; ?>

        <?php if ($patient_to_delete): ?>
            <!-- Confirmation Screen -->
            <div class="confirmation-box">
                <h3>⚠️ Confirm Patient Deletion</h3>
                <p>You are about to delete the following patient. This action cannot be undone.</p>
                
                <div class="patient-details">
                    <p><strong>Patient ID:</strong> <?= htmlspecialchars($patient_to_delete['PatientID']) ?></p>
                    <p><strong>Name:</strong> <?= htmlspecialchars($patient_to_delete['FirstName'] . ' ' . $patient_to_delete['LastName']) ?></p>
                    <p><strong>Date of Birth:</strong> <?= htmlspecialchars($patient_to_delete['DateOfBirth']) ?></p>
                    <p><strong>Sex:</strong> <?= htmlspecialchars($patient_to_delete['Sex']) ?></p>
                </div>

                <p style="color: #721c24; font-weight: bold;">
                    Warning: This will also delete all associated medical conditions and medication records for this patient.
                </p>

                <form method="POST" action="delete_patient.php" class="button-group">
                    <input type="hidden" name="delete_id" value="<?= $patient_to_delete['PatientID'] ?>">
                    <button type="submit" name="confirm_delete" class="btn-confirm" 
                            onclick="return confirm('Are you absolutely sure you want to delete this patient? This cannot be undone!')">
                        Yes, Delete Patient
                    </button>
                    <a href="modify_patient.php" class="btn-cancel">Cancel</a>
                </form>
            </div>
        <?php else: ?>
            <!-- Selection Screen -->
            <?php if (count($patients) > 0): ?>
                <div class="form-container">
                    <p>Select a patient from the list below to delete:</p>
                    <form method="GET" action="delete_patient.php">
                        <label for="id"><strong>Select Patient:</strong></label>
                        <select name="id" id="id" required>
                            <option value="">-- Select a Patient --</option>
                            <?php foreach ($patients as $p): 
                                $age = date_diff(date_create($p['DateOfBirth']), date_create('today'))->y;
                            ?>
                                <option value="<?= $p['PatientID'] ?>">
                                    ID: <?= $p['PatientID'] ?> - <?= htmlspecialchars($p['FirstName'] . ' ' . $p['LastName']) ?> 
                                    (<?= $p['Sex'] ?>, Age: <?= $age ?>)
                                </option>
                            <?php endforeach; ?>
                        </select>
                        <button type="submit" class="delete-btn">Select Patient to Delete</button>
                    </form>
                </div>
            <?php else: ?>
                <p class="warning">
                    No patients found in the database.
                </p>
            <?php endif; ?>
            
            <p style="text-align: center; margin-top: 20px;">
                <a href="modify_patient.php">← Back to Modify Patients</a>
            </p>
        <?php endif; ?>
    </main>
</body>
</html>