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
	$limit = "2";
}

if(isset($_GET['big_type'])) {
	$big_type = $_GET['big_type'];
} else {
	$big_type = "";
}

$query = "select * from school_phone where big_type = '$big_type' order by no DESC" /*LIMIT $first , $limit*/; //퀴리(SQL)문 작성
$result_contents = mysqli_query($db, $query); //쿼리 실행

echo "{ "."\"phone\" : [";
$start = 0;
while($row = mysqli_fetch_array($result_contents)) {
	echo (($start != 0) ? "," : "");
	$start++;
	$data['big_type'] = $row['big_type'];
	$data['small_type'] = $row['small_type'];
	$data['name'] = $row['name'];
	$data['tel'] = $row['tel'];
	echo json_encode($data);
}
echo "] }";
?>