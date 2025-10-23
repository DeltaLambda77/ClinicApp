<?php
include 'connection.php';

// Get all trials for the dropdown
$trials_stmt = $conn->query("SELECT TrialID, Title FROM Trial ORDER BY Title");
$trials = $trials_stmt->fetch_all(MYSQLI_ASSOC);
?>

<!DOCTYPE html>
<html>
<head>
    <title>Check Trial Eligibility</title>
    <style>
        body { font-family: Arial; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin: 20px 0; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        .eligible { background-color: #d4edda; }
        select { padding: 5px; margin: 10px 0; }
    </style>
</head>
<body>
    <h1>Check Trial Eligibility</h1>
    <a href="index.php">← Back to Home</a>

    <label>Select Trial:</label>
    <select id="trialSelect">
        <option value="">-- Select a Trial --</option>
        <?php foreach($trials as $trial): ?>
            <option value="<?= $trial['TrialID'] ?>"><?= $trial['Title'] ?></option>
        <?php endforeach; ?>
    </select>

    <div id="eligibilityResults"></div>

    <script>
    document.getElementById('trialSelect').addEventListener('change', function() {
        const trialId = this.value;
        const resultsDiv = document.getElementById('eligibilityResults');

        if (trialId === "") {
            resultsDiv.innerHTML = ""; // clear table if no trial selected
            return;
        }

        // AJAX request
        const xhr = new XMLHttpRequest();
        xhr.open("GET", "fetch_eligibility.php?trial=" + trialId, true);
        xhr.onload = function() {
            if (xhr.status === 200) {
                resultsDiv.innerHTML = xhr.responseText;
            } else {
                resultsDiv.innerHTML = "Error fetching data.";
            }
        };
        xhr.send();
    });
    </script>
</body>
</html>
