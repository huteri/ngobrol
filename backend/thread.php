<?php

require "func.php";
	
if($_GET["action"] == "get_all_threads") {

	include "connect.php";

	$result = array();

	$query = "SELECT thread.id, thread.title, thread.dateCreated,thread.categoryId, thread.userId, user.username, category.name, category.color, (SELECT count(*) FROM post WHERE threadId = thread.Id) as 'replies' FROM thread, user, category WHERE thread.userId = user.id AND thread.categoryId = category.Id";

	if(isset($_GET["categoryId"])) {
		$categoryId = mysql_real_escape_string($_GET["categoryId"]);
		$query .= " AND thread.categoryId = ".$categoryId;
	}

	if(isset($_GET["userId"])) {
		$userId = mysql_real_escape_string($_GET["userId"]);
		$query .= " AND thread.userId = '$userId'";
	}

	if($_GET["sortPopular"] == 1) {
		$query .= " ORDER By thread.view DESC";
	} else {
		$query .= " ORDER BY thread.dateCreated DESC";
	}

	$sql = mysql_query($query) or die(mysql_error." Query : ".$query);

	$result["success"] = 1;
	$result["count"] = mysql_num_rows($sql);

	if($result["count"] > 0) {
		$data = array();
		while($row = mysql_fetch_array($sql)) {
			$data[] = array("id"=>$row["id"],
				"replies" => $row["replies"],
				"title"=> $row["title"],
				"dateCreated" => $row["dateCreated"],
				"user" => array("id"=>$row["userId"], 
					 "username" => $row["username"]),
				"category" => array("name"=>$row["name"], 
					"id"=> $row["categoryId"],
					"color"=>$row["color"]));
		}

		$result["data"] = $data;
	}

	encode($result);
	mysql_close($con);
} else if($_GET["action"] == "submit_thread") {
	include "connect.php";

	$result = array();

	$categoryId = mysql_real_escape_string($_POST["categoryId"]);
	$api = mysql_real_escape_string($_POST["api"]);
	$androidId = mysql_real_escape_string($_POST["androidId"]);
	$userId = mysql_real_escape_string($_POST["userId"]);
	$title = mysql_real_escape_string($_POST["title"]);
	$text = mysql_real_escape_string($_POST["text"]);

	$checkUser = mysql_query("SELECT * FROM device WHERE api = '$api' AND userId='$userId' AND androidId='$androidId'") or die(mysql_error());
	if(mysql_num_rows($checkUser) > 0) {
		mysql_query("INSERT INTO thread (title, userId, categoryId) VALUES ('$title', '$userId', '$categoryId')") or die(mysql_error());
		mysql_query("INSERT INTO post (threadId, userId, text) VALUES ('".mysql_insert_id()."', '$userId', '$text')") or die(mysql_error());
		$result["success"] = 1;
	} else {
		$result["success"] = 0;
		$result["message"] = getUnAuthorizeMessage();
	}

	encode($result);
	mysql_close($con);

} else {
	echo "No response";
}
?>