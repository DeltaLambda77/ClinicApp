<?php
include 'api_config.php';

// Get all trials for the dropdown
$response = callAPI('GET', '/trials');
$trials = [];

if ($response['status'] == 200 && $response['data']) {
    foreach ($response['data'] as $trial) {
        $trials[] = [
            'TrialID' => $trial['trialId'],
            'Title' => $trial['title']
        ];
    }
}
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
            <option value="<?= htmlspecialchars($trial['TrialID']) ?>">
                <?= htmlspecialchars($trial['Title']) ?>
            </option>
        <?php endforeach; ?>
    </select>
    
    <div id="eligibilityResults"></div>
    
    <script>
    document.getElementById('trialSelect').addEventListener('change', function() {
        const trialId = this.value;
        const resultsDiv = document.getElementById('eligibilityResults');
        
        if (trialId === "") {
            resultsDiv.innerHTML = "";
            return;
        }
        
        fetch('fetch_eligibility.php?trial=' + trialId)
            .then(response => response.text())
            .then(data => {
                resultsDiv.innerHTML = data;
            })
            .catch(error => {
                resultsDiv.innerHTML = "Error fetching data.";
            });
    });
    </script>
</body>
</html>