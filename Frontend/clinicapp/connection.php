<?php
$servername = "localhost";
$username = "root";       // default XAMPP username
$password = "";           // default XAMPP password
$dbname = "clinicdb";     // your database name

// Creates connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Checks connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>

