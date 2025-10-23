<?php
include('api_config.php');

$response = callAPI('GET', '/patients');

echo "<pre>";
echo "Status Code: " . $response['status'] . "\n";
echo "Response Data:\n";
print_r($response['data']);
echo "</pre>";

$patients = [];

if ($response['status'] == 200 && $response['data']) {
    foreach ($response['data'] as $patient) {

        if (!isset($patient['dateOfBirth']) || !isset($patient['patientId'])) {
            continue;
        }
        
        $dob = new DateTime($patient['dateOfBirth']);
        $now = new DateTime();
        $age = $now->diff($dob)->y;
        
        $patients[] = [
            'PatientID' => $patient['patientId'],
            'Name' => ($patient['firstName'] ?? '') . ' ' . ($patient['lastName'] ?? ''),
            'Age' => $age,
            'Sex' => $patient['sex'] ?? '',
            'ContactInfo' => $patient['contactInfo'] ?? ''
        ];
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
        
        <?php if (empty($patients)): ?>
            <p>No patients found.</p>
        <?php else: ?>
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
                <td><?= htmlspecialchars($p['PatientID']) ?></td>
                <td><?= htmlspecialchars($p['Name']) ?></td>
                <td><?= htmlspecialchars($p['Age']) ?></td>
                <td><?= htmlspecialchars($p['Sex']) ?></td>
                <td><?= htmlspecialchars($p['ContactInfo']) ?></td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php endif; ?>
    </main>
</body>
</html>