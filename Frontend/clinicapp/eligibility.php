<?php
include('connection.php');

// QUERY TYPE: SELECT with JOIN - Get all trials with their requirement counts
$trials_result = $conn->query("
    SELECT t.TrialID, t.Title, t.MinimumAge, t.MaximumAge, 
           t.StartDate, t.EndDate,
           COUNT(tr.RequirementID) as RequirementCount
    FROM Trial t
    LEFT JOIN TrialRequirement tr ON t.TrialID = tr.TrialID
    GROUP BY t.TrialID, t.Title, t.MinimumAge, t.MaximumAge, t.StartDate, t.EndDate
    ORDER BY t.Title
");
$trials = [];
if ($trials_result) {
    $trials = $trials_result->fetch_all(MYSQLI_ASSOC);
}

$selected_trial = $_GET['trial'] ?? null;
$eligible_patients = [];
$trial_details = null;

if ($selected_trial) {
    // Get trial details
    $stmt = $conn->prepare("SELECT * FROM Trial WHERE TrialID = ?");
    $stmt->bind_param("i", $selected_trial);
    $stmt->execute();
    $trial_details = $stmt->get_result()->fetch_assoc();
    $stmt->close();

    // QUERY TYPE: Complex SELECT with SUBQUERY and age calculation
    // Find patients eligible by age
    $sql = "SELECT p.PatientID, 
                   CONCAT(p.FirstName, ' ', p.LastName) AS Name,
                   p.FirstName,
                   p.LastName,
                   p.DateOfBirth,
                   TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) AS Age,
                   p.Sex,
                   p.ContactInfo
            FROM Patient p
            WHERE TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) BETWEEN
                  (SELECT MinimumAge FROM Trial WHERE TrialID = ?) AND
                  (SELECT MaximumAge FROM Trial WHERE TrialID = ?)
            ORDER BY p.LastName, p.FirstName";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $selected_trial, $selected_trial);
    $stmt->execute();
    $result = $stmt->get_result();
    $eligible_patients = $result->fetch_all(MYSQLI_ASSOC);
    $stmt->close();
}

// QUERY TYPE: SUBQUERY with EXISTS - Patients eligible for ANY trial
$subquery_sql = "SELECT p.PatientID, 
                        CONCAT(p.FirstName, ' ', p.LastName) AS Name,
                        TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) AS Age,
                        COUNT(DISTINCT t.TrialID) as EligibleTrialCount
                 FROM Patient p
                 CROSS JOIN Trial t
                 WHERE TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) 
                       BETWEEN t.MinimumAge AND t.MaximumAge
                 GROUP BY p.PatientID, Name, Age
                 HAVING EligibleTrialCount > 0
                 ORDER BY EligibleTrialCount DESC, p.LastName, p.FirstName";
$subquery_result = $conn->query($subquery_sql);
$any_eligible = [];
if ($subquery_result) {
    $any_eligible = $subquery_result->fetch_all(MYSQLI_ASSOC);
}

// QUERY TYPE: NOT EXISTS subquery - Trials with no eligible patients
$no_eligible_sql = "SELECT t.TrialID, t.Title, t.MinimumAge, t.MaximumAge,
                           DATEDIFF(t.EndDate, CURDATE()) as DaysRemaining
                    FROM Trial t
                    WHERE NOT EXISTS (
                        SELECT 1 FROM Patient p
                        WHERE TIMESTAMPDIFF(YEAR, p.DateOfBirth, CURDATE()) 
                        BETWEEN t.MinimumAge AND t.MaximumAge
                    )
                    ORDER BY t.Title";
$no_eligible_result = $conn->query($no_eligible_sql);
$no_eligible_trials = [];
if ($no_eligible_result) {
    $no_eligible_trials = $no_eligible_result->fetch_all(MYSQLI_ASSOC);
}

// QUERY TYPE: Complex JOIN - Get trial requirements details
$trial_requirements = [];
if ($selected_trial) {
    $req_sql = "SELECT tr.RequirementID, tr.Notes,
                       mc.Name as ConditionName, mc.Code as ConditionCode,
                       m.Name as ExcludedMedicationName
                FROM TrialRequirement tr
                LEFT JOIN MedicalCondition mc ON tr.ConditionID = mc.ConditionID
                LEFT JOIN Medication m ON tr.ExcludedMedicationID = m.MedicationID
                WHERE tr.TrialID = ?";
    $stmt = $conn->prepare($req_sql);
    $stmt->bind_param("i", $selected_trial);
    $stmt->execute();
    $trial_requirements = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
    $stmt->close();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Check Trial Eligibility</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .eligible { background-color: #d4edda; }
        .no-eligible { background-color: #f8d7da; }
        .subquery-section { 
            background-color: #f0f8ff; 
            padding: 15px; 
            margin: 20px 0; 
            border-radius: 5px; 
            border: 1px solid #b8daff;
        }
        .trial-info {
            background: #e7f3ff;
            padding: 15px;
            margin: 15px 0;
            border-radius: 5px;
            border-left: 4px solid #2196F3;
        }
        select { 
            padding: 8px; 
            margin: 10px 0; 
            width: 100%;
            max-width: 500px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        h3 { 
            color: #2c3e50; 
            border-bottom: 2px solid #4CAF50; 
            padding-bottom: 5px; 
        }
        .error { 
            color: #721c24; 
            background: #f8d7da; 
            padding: 10px; 
            border-radius: 5px; 
            border: 1px solid #f5c6cb;
        }
        .warning {
            color: #856404;
            background: #fff3cd;
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ffeaa7;
        }
        .badge {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
        }
        .badge-success { background: #d4edda; color: #155724; }
        .badge-danger { background: #f8d7da; color: #721c24; }
        .badge-info { background: #d1ecf1; color: #0c5460; }
    </style>
</head>
<body>
    <header>Clinical Trial Management System</header>
    <nav>
        <a href="index.php">Home</a>
        <a href="patients.php">View Patients</a>
        <a href="add_patient.php">Add Patient</a>
        <a href="eligibility.php" class="active">Check Eligibility</a>
        <a href="modify_patient.php">Modify Patients</a>
    </nav>
    <main>
        <h2>Check Trial Eligibility</h2>

        <?php if (empty($trials)): ?>
            <div class="error">
                <strong>No trials found in database.</strong> Please make sure the 'Trial' table exists and has data.
            </div>
        <?php else: ?>
            <!-- Trial Selection Form -->
            <form method="GET" action="">
                <label><strong>Select Trial:</strong></label>
                <select name="trial" onchange="this.form.submit()">
                    <option value="">-- Select a Trial --</option>
                    <?php foreach($trials as $trial): ?>
                        <option value="<?= $trial['TrialID'] ?>" 
                                <?= $trial['TrialID'] == $selected_trial ? 'selected' : '' ?>>
                            <?= htmlspecialchars($trial['Title']) ?> 
                            (Ages: <?= $trial['MinimumAge'] ?>-<?= $trial['MaximumAge'] ?>)
                            <?php if ($trial['RequirementCount'] > 0): ?>
                                - <?= $trial['RequirementCount'] ?> requirement(s)
                            <?php endif; ?>
                        </option>
                    <?php endforeach; ?>
                </select>
            </form>

            <?php if ($selected_trial && $trial_details): ?>
                <div class="trial-info">
                    <h3><?= htmlspecialchars($trial_details['Title']) ?></h3>
                    <p><strong>Trial ID:</strong> <?= $trial_details['TrialID'] ?></p>
                    <p><strong>Age Range:</strong> <?= $trial_details['MinimumAge'] ?> - <?= $trial_details['MaximumAge'] ?> years</p>
                    <p><strong>Start Date:</strong> <?= $trial_details['StartDate'] ?></p>
                    <p><strong>End Date:</strong> <?= $trial_details['EndDate'] ?></p>
                    
                    <?php if (count($trial_requirements) > 0): ?>
                        <h4>Trial Requirements:</h4>
                        <ul>
                            <?php foreach($trial_requirements as $req): ?>
                                <li>
                                    <?php if ($req['ConditionName']): ?>
                                        <span class="badge badge-info">Required Condition:</span> 
                                        <?= htmlspecialchars($req['ConditionName']) ?> 
                                        (<?= htmlspecialchars($req['ConditionCode']) ?>)
                                    <?php endif; ?>
                                    <?php if ($req['ExcludedMedicationName']): ?>
                                        <span class="badge badge-danger">Excluded Medication:</span> 
                                        <?= htmlspecialchars($req['ExcludedMedicationName']) ?>
                                    <?php endif; ?>
                                    <?php if ($req['Notes']): ?>
                                        <br><em><?= htmlspecialchars($req['Notes']) ?></em>
                                    <?php endif; ?>
                                </li>
                            <?php endforeach; ?>
                        </ul>
                    <?php endif; ?>
                </div>

                <h3>Eligible Patients (Age-Based)</h3>
                
                <?php if (count($eligible_patients) > 0): ?>
                    <table>
                        <tr>
                            <th>Patient ID</th>
                            <th>Name</th>
                            <th>Age</th>
                            <th>Sex</th>
                            <th>Date of Birth</th>
                            <th>Contact</th>
                        </tr>
                        <?php foreach($eligible_patients as $p): ?>
                        <tr class="eligible">
                            <td><?= htmlspecialchars($p['PatientID']) ?></td>
                            <td><?= htmlspecialchars($p['Name']) ?></td>
                            <td><?= htmlspecialchars($p['Age']) ?></td>
                            <td><?= htmlspecialchars($p['Sex']) ?></td>
                            <td><?= htmlspecialchars($p['DateOfBirth']) ?></td>
                            <td><?= htmlspecialchars($p['ContactInfo'] ?? 'N/A') ?></td>
                        </tr>
                        <?php endforeach; ?>
                    </table>
                    <p><em>Found <strong><?= count($eligible_patients) ?></strong> eligible patient(s) based on age criteria</em></p>
                <?php else: ?>
                    <p class="warning">
                        No patients found eligible for this trial based on age criteria.
                    </p>
                <?php endif; ?>
            <?php endif; ?>

            <!-- Additional Analysis Section -->
            <div class="subquery-section">
                <h3>📊 Additional Eligibility Analysis</h3>

                <h4>Patients Eligible for ANY Trial</h4>
                <?php if (count($any_eligible) > 0): ?>
                    <table>
                        <tr>
                            <th>Patient ID</th>
                            <th>Name</th>
                            <th>Age</th>
                            <th>Eligible Trials</th>
                        </tr>
                        <?php foreach($any_eligible as $p): ?>
                        <tr>
                            <td><?= htmlspecialchars($p['PatientID']) ?></td>
                            <td><?= htmlspecialchars($p['Name']) ?></td>
                            <td><?= htmlspecialchars($p['Age']) ?></td>
                            <td><span class="badge badge-success"><?= $p['EligibleTrialCount'] ?> trial(s)</span></td>
                        </tr>
                        <?php endforeach; ?>
                    </table>
                    <p><em><?= count($any_eligible) ?> patient(s) are eligible for at least one trial</em></p>
                <?php else: ?>
                    <p class="warning">No patients found eligible for any trial.</p>
                <?php endif; ?>

                <h4>Trials With No Eligible Patients</h4>
                <?php if (count($no_eligible_trials) > 0): ?>
                    <table>
                        <tr>
                            <th>Trial ID</th>
                            <th>Title</th>
                            <th>Age Range</th>
                            <th>Days Remaining</th>
                        </tr>
                        <?php foreach($no_eligible_trials as $trial): ?>
                        <tr class="no-eligible">
                            <td><?= htmlspecialchars($trial['TrialID']) ?></td>
                            <td><?= htmlspecialchars($trial['Title']) ?></td>
                            <td><?= $trial['MinimumAge'] ?>-<?= $trial['MaximumAge'] ?> years</td>
                            <td>
                                <?php if ($trial['DaysRemaining'] > 0): ?>
                                    <?= $trial['DaysRemaining'] ?> days
                                <?php else: ?>
                                    <span style="color: red;">Ended</span>
                                <?php endif; ?>
                            </td>
                        </tr>
                        <?php endforeach; ?>
                    </table>
                    <p><em><?= count($no_eligible_trials) ?> trial(s) have no eligible patients based on age</em></p>
                <?php else: ?>
                    <p style="color: green;">✓ All trials have at least one eligible patient!</p>
                <?php endif; ?>
            </div>
        <?php endif; ?>
    </main>
</body>
</html>