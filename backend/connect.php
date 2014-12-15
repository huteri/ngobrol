<?php

// Include database connection settings
$username = "";
$password = "";
$hostname = "";
$con = mysql_connect($hostname, $username, $password) or die("Unable to connect to MySQL");
$selected = mysql_select_db("", $con) or die("Could not fetch database");

?>