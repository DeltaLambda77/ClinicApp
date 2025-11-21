<?php
// connection.php
// Database configuration
$servername = "localhost";
$username = "root";           // default MySQL username
$password = "";               // default MySQL password (empty for XAMPP)
$dbname = "clinic_db";         // your database name

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Set charset to UTF-8 to handle special characters properly
$conn->set_charset("utf8mb4");

// Optional: Display success message (comment out in production)
// echo "Connected successfully to database: $dbname";
?>