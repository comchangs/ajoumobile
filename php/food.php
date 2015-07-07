<?php

require_once "setting.php";

$date = date('Y-m-d H:i:s');

$db = new mysqli('localhost', $dbuser, $dbpasswd, $dbname);
if (mysqli_connect_errno())
{
	echo 'Error: Could not connect to database. Please try again later.';
	exit;
}

if(isset($_GET['first'])) {
	$first = $_GET['first'];
} else {
	$first = "0";
}


if(isset($_GET['limit'])) {
	$limit = $_GET['limit'];
} else {
	$limit = "10";
}

if(isset($_GET['category'])) {
	$category = $_GET['category'];
} else {
	$category = "";
}

$query = "select * from food where category = '$category' order by no DESC LIMIT $first , $limit"; //柠府(SQL)巩 累己
$result_contents = mysqli_query($db, $query); //孽府 角青

while($row = mysqli_fetch_array($result_contents)) {
	echo "<item>";
	echo "<no>".$row['no']."</no><restaurant_name>".$row['restaurant_name']."</restaurant_name>";
	echo "</item>";
}

echo "<debug>".$category."</debug>";
?>