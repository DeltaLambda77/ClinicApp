<?php
include('connection.php');

// Get all trials
$trials_result = $conn->query("SELECT TrialID, Title, MinimumAge, MaximumAge FROM trial ORDER BY Title");
$trials = [];
if ($trials_result) {
    $trials = $trials_result->fetch_all(MYSQLI_ASSOC);
}

$selected_trial = $_GET['trial'] ?? null;
$eligible_patients = [];

if ($selected_trial) {
    // QUERY TYPE: JOIN
    $sql = "SELECT PatientID, CONCAT(FirstName, ' ', LastName) AS Name,
            TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) AS Age
            FROM Patient
            WHERE TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) BETWEEN
                  (SELECT MinimumAge FROM trial WHERE TrialID = ?) AND
                  (SELECT MaximumAge FROM trial WHERE TrialID = ?)
            ORDER BY LastName, FirstName";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $selected_trial, $selected_trial);
    $stmt->execute();
    $result = $stmt->get_result();
    $eligible_patients = $result->fetch_all(MYSQLI_ASSOC);
}

// Patients eligible for any trial
// QUERY TYPE: SUBQUERY
$subquery_sql = "SELECT p.PatientID, CONCAT(p.FirstName, ' ', p.LastName) AS Name,
                 TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) AS Age
                 FROM Patient p
                 WHERE EXISTS (
                     SELECT 1 FROM trial t 
                     WHERE TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) 
                     BETWEEN t.MinimumAge AND t.MaximumAge
                 )
                 ORDER BY p.LastName, p.FirstName";
$subquery_result = $conn->query($subquery_sql);
$any_eligible = [];
if ($subquery_result) {
    $any_eligible = $subquery_result->fetch_all(MYSQLI_ASSOC);
}

// Trials with no eligible patients
$no_eligible_sql = "SELECT t.TrialID, t.Title, t.MinimumAge, t.MaximumAge
                    FROM trial t
                    WHERE NOT EXISTS (
                        SELECT 1 FROM Patient p
                        WHERE TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) 
                        BETWEEN t.MinimumAge AND t.MaximumAge
                    )";
$no_eligible_result = $conn->query($no_eligible_sql);
$no_eligible_trials = [];
if ($no_eligible_result) {
    $no_eligible_trials = $no_eligible_result->fetch_all(MYSQLI_ASSOC);
}
?>
<!DOCTYPE html>
<html>
<head>
    <title>Check Trial Eligibility</title>
    <style>
        body { font-family: Arial; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        .eligible { background-color: #d4edda; }
        .no-eligible { background-color: #f8d7da; }
        .subquery-section { background-color: #f0f8ff; padding: 15px; margin: 20px 0; border-radius: 5px; }
        select { padding: 5px; margin: 10px 0; }
        h3 { color: #2c3e50; border-bottom: 2px solid #4CAF50; padding-bottom: 5px; }
        .error { color: red; background: #f8d7da; padding: 10px; border-radius: 5px; }
    </style>
</head>
<body>

    <main>
        <h2>Check Trial Eligibility</h2>
        <a href="index.php">← Back to Home</a>

        <?php if (empty($trials)): ?>
            <div class="error">
                <strong>No trials found in database.</strong> Please make sure the 'trial' table exists and has data.
            </div>
        <?php else: ?>
            <!-- Trial Selection Form -->
            <form method="GET">
                <label><strong>Select Trial:</strong></label>
                <select name="trial" onchange="this.form.submit()">
                    <option value="">-- Select a Trial --</option>
                    <?php foreach($trials as $trial): ?>
                        <option value="<?= $trial['TrialID'] ?>" 
                                <?= $trial['TrialID'] == $selected_trial ? 'selected' : '' ?>>
                            <?= $trial['Title'] ?> (Ages: <?= $trial['MinimumAge'] ?>-<?= $trial['MaximumAge'] ?>)
                        </option>
                    <?php endforeach; ?>
                </select>
            </form>

            
            <?php if ($selected_trial): ?>
                <?php 
                $current_trial = array_filter($trials, function($t) use ($selected_trial) {
                    return $t['TrialID'] == $selected_trial;
                });
                $current_trial = reset($current_trial);
                ?>
                <h3>Eligible Patients for: <?= $current_trial['Title'] ?></h3>
                
                <?php if (count($eligible_patients) > 0): ?>
                    <table>
                        <tr>
                            <th>Patient ID</th>
                            <th>Name</th>
                            <th>Age</th>
                        </tr>
                        <?php foreach($eligible_patients as $p): ?>
                        <tr class="eligible">
                            <td><?= $p['PatientID'] ?></td>
                            <td><?= $p['Name'] ?></td>
                            <td><?= $p['Age'] ?></td>
                        </tr>
                        <?php endforeach; ?>
                    </table>
                    <p><em>Found <?= count($eligible_patients) ?> eligible patient(s)</em></p>
                <?php else: ?>
                    <p style="color: #856404; background: #fff3cd; padding: 10px;">
                        No patients found eligible for this trial based on age criteria.
                    </p>
                <?php endif; ?>
            <?php endif; ?>

            <!-- Additional Analysis Section -->
            <div class="subquery-section">
                <h3>Additional Analysis</h3>

                <h4>Patients Eligible for ANY Trial</h4>
                <?php if (count($any_eligible) > 0): ?>
                    <table>
                        <tr>
                            <th>Patient ID</th>
                            <th>Name</th>
                            <th>Age</th>
                        </tr>
                        <?php foreach($any_eligible as $p): ?>
                        <tr>
                            <td><?= $p['PatientID'] ?></td>
                            <td><?= $p['Name'] ?></td>
                            <td><?= $p['Age'] ?></td>
                        </tr>
                        <?php endforeach; ?>
                    </table>
                    <p><em><?= count($any_eligible) ?> patient(s) are eligible for at least one trial</em></p>
                <?php else: ?>
                    <p>No patients found eligible for any trial.</p>
                <?php endif; ?>

                <h4>Trials With No Eligible Patients</h4>
                <?php if (count($no_eligible_trials) > 0): ?>
                    <table>
                        <tr>
                            <th>Trial ID</th>
                            <th>Title</th>
                            <th>Age Range</th>
                        </tr>
                        <?php foreach($no_eligible_trials as $trial): ?>
                        <tr class="no-eligible">
                            <td><?= $trial['TrialID'] ?></td>
                            <td><?= $trial['Title'] ?></td>
                            <td><?= $trial['MinimumAge'] ?>-<?= $trial['MaximumAge'] ?></td>
                        </tr>
                        <?php endforeach; ?>
                    </table>
                    <p><em><?= count($no_eligible_trials) ?> trial(s) have no eligible patients</em></p>
                <?php else: ?>
                    <p>All trials have at least one eligible patient.</p>
                <?php endif; ?>
            </div>
        <?php endif; ?>

    </main>
</body>
</html>