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


$query = "select * from showinfo order by no DESC LIMIT $first , $limit"; //퀴리(SQL)문 작성
$result_contents = mysqli_query($db, $query); //쿼리 실행

echo "{ "."\"show\" : [";
while($row = mysqli_fetch_array($result_contents)) {
	echo json_encode($row);
}
echo "] }";
?>