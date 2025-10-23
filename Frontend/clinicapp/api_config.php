<?php
// api_config.php
$API_BASE_URL = "http://localhost:8080/api";

function callAPI($method, $endpoint, $data = null) {
    global $API_BASE_URL;
    $url = $API_BASE_URL . $endpoint;
    
    $options = [
        'http' => [
            'method' => $method,
            'header' => 'Content-Type: application/json',
            'ignore_errors' => true
        ]
    ];
    
    if ($method === 'POST' || $method === 'PUT') {
        if ($data) {
            $options['http']['content'] = json_encode($data);
        }
    } elseif ($method === 'GET' && $data) {
        $url .= '?' . http_build_query($data);
    }
    
    $context = stream_context_create($options);
    $result = @file_get_contents($url, false, $context);
    
    // Get HTTP response code
    $httpcode = 500; // default to error
    if (isset($http_response_header)) {
        preg_match('/HTTP\/\d\.\d\s+(\d+)/', $http_response_header[0], $matches);
        $httpcode = isset($matches[1]) ? intval($matches[1]) : 500;
    }
    
    return [
        'status' => $httpcode,
        'data' => json_decode($result, true)
    ];
}
?>