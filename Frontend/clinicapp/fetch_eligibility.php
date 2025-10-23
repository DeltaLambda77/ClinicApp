<?php
include 'connection.php';

$selected_trial = $_GET['trial'] ?? null;

if (!$selected_trial) {
    echo "";
    exit;
}

// Fetch eligible patients (age-based)
$sql = "SELECT DISTINCT
            p.PatientID,
            CONCAT(p.FirstName, ' ', p.LastName) AS PatientName,
            TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) AS Age,
            t.MinimumAge,
            t.MaximumAge
        FROM Patient p
        CROSS JOIN Trial t
        WHERE t.TrialID = ?
          AND TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) 
              BETWEEN t.MinimumAge AND t.MaximumAge
        ORDER BY p.LastName, p.FirstName";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $selected_trial);
$stmt->execute();
$result = $stmt->get_result();
$patients = $result->fetch_all(MYSQLI_ASSOC);

if (count($patients) == 0) {
    echo "<p>No patients meet the age criteria for this trial.</p>";
    exit;
}

// Build HTML table
echo "<h2>Eligible Patients (Age-Based)</h2>";
echo "<table>
        <tr>
            <th>Patient ID</th>
            <th>Patient Name</th>
            <th>Age</th>
            <th>Trial Age Range</th>
            <th>Status</th>
        </tr>";
foreach($patients as $p) {
    echo "<tr class='eligible'>
            <td>{$p['PatientID']}</td>
            <td>{$p['PatientName']}</td>
            <td>{$p['Age']}</td>
            <td>{$p['MinimumAge']}-{$p['MaximumAge']}</td>
            <td>✓ Eligible</td>
          </tr>";
}
echo "</table>";
?>
