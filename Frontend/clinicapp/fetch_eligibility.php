<?php
// fetch_eligibility.php

include 'api_config.php';

$selected_trial = $_GET['trial'] ?? null;

if (!$selected_trial) {
    echo "";
    exit;
}

// Call API to get eligible patients for this trial
$response = callAPI('GET', '/trials/' . $selected_trial . '/eligible-patients');

if ($response['status'] != 200 || empty($response['data'])) {
    echo "<p>No patients meet the criteria for this trial.</p>";
    exit;
}

$patients = $response['data'];

// Build HTML table
echo "<h2>Eligible Patients</h2>";
echo "<table>
        <tr>
            <th>Patient ID</th>
            <th>Patient Name</th>
            <th>Age</th>
            <th>Status</th>
        </tr>";

foreach($patients as $p) {
    $dob = new DateTime($p['dateOfBirth']);
    $now = new DateTime();
    $age = $now->diff($dob)->y;
    $name = $p['firstName'] . ' ' . $p['lastName'];
    
    echo "<tr class='eligible'>
            <td>{$p['patientId']}</td>
            <td>" . htmlspecialchars($name) . "</td>
            <td>$age</td>
            <td>✓ Eligible</td>
          </tr>";
}

echo "</table>";
?>