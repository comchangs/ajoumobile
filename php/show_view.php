<?php

require_once "setting.php";

$date = date('Y-m-d H:i:s');

$db = new mysqli('localhost', $dbuser, $dbpasswd, $dbname);
if (mysqli_connect_errno())
{
	echo 'Error: Could not connect to database. Please try again later.';
	exit;
}

if(isset($_GET['no'])) {
	$no = $_GET['no'];
} else {
	$no = "0";
}

$query = "select * from showinfo where no = $no"; //퀴리(SQL)문 작성
$result_contents = mysqli_query($db, $query); //쿼리 실행

while($row = mysqli_fetch_array($result_contents)) {
	echo "<item>";
	echo "<no>".$row['no']."</no><name>".$row['name']."</name><datetime>".$row['datetime']."</datetime><contents>".$row['contents']."</contents><clubname>".$row['clubname']."</clubname><poster>".$row['poster']."</poster>";
	echo "</item>";
}
?>