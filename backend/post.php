<?php
require "func.php";

if($_GET["action"] == "get_posts") {
	require "connect.php";
	$threadId = mysql_real_escape_string($_GET["threadId"]);
	$userId = mysql_real_escape_string($_GET["userId"]);
	$result = array();

	if(isset($_GET["p"])) 
		$page = mysql_real_escape_string($_GET["p"]);
	else 
		$page = 1;

	if(isset($_GET["limit"])) 
		$limit = mysql_real_escape_string($_GET["limit"]);
	else
		$limit = 0;

	$offset = $limit * ($page-1);

	if($threadId > 0 && $userId > 0)  {
		$where .= "post.threadId = '$threadId' AND post.userId='$userId'";
	} else if($threadId > 0) {
		$where .= "post.threadId = '$threadId'";
	} else if($userId > 0) {
		$where .= "post.userId = '$userId'";
	} else {
		$result["success"] = 0;
		$result["message"] = "Can't have anything to show";
		encode($result);
		die();
	}

	$sql = "SELECT SQL_CALC_FOUND_ROWS post.id, post.text, post.dateCreated, post.userId, post.threadId, post.isModified, post.timestamp, user.username, 
	user.fullname, user.profileUrl, user.profileBg, user.aboutMe, thread.title, thread.categoryId, category.name, category.color FROM post, thread, user, category WHERE post.threadId = thread.id AND post.userId = user.id AND thread.categoryId = category.Id AND ".$where." ORDER BY post.dateCreated ";

	if($limit > 0) 
		$sql .= "LIMIT $offset, $limit";

	mysql_query('SET CHARACTER SET utf8');
	mysql_query("UPDATE thread Set view = view+1 WHERE id = '$threadId'");

	$rows = mysql_query($sql) or die(mysql_error()." Query : ".$sql);

	$result["success"] = 1;
	$result["count"] = mysql_num_rows($rows);
	$count = mysql_query("SELECT FOUND_ROWS()") or die(mysql_error()); $countTotal = mysql_fetch_row($count);
	$result["total"] = $countTotal[0];

	if($result["count"] > 0) {
		
		while($row = mysql_fetch_array($rows)) {
			$data[] = array("id"=>$row["id"], 
				"text" =>$row["text"], 
				"dateCreated" => $row["dateCreated"], 
				"isModified" => $row["isModified"],
				"timestamp" => $row["timestamp"],
				"user" => array("id" => $row["userId"], 
					"username" => $row["username"], 
					"fullname" => $row["fullname"],
					"profileUrl" => $row["profileUrl"],
					"profileBg" => $row["profileBg"],
					"aboutMe" => $row["aboutMe"]),
				"thread" => array("id" => $row["threadId"],
					"title" => $row["title"],
					"category" => array("id" => $row["categoryId"],
					"name" => $row["name"],
					"color" => $row["color"]))
				);
		}
	}

	$result["data"] = $data;

	echo json_encode($result, JSON_HEX_QUOT | JSON_HEX_TAG);
	mysql_close($con);
} else if($_GET["action"] == "submit_post" && isset($_GET["threadId"]) && isset($_POST["api"])) {
	
	require "connect.php";
	$result = array();

	$threadId = mysql_real_escape_string($_GET["threadId"]);
	$api = mysql_real_escape_string($_POST["api"]);
	$androidId = mysql_real_escape_string($_POST["androidId"]);
	$userId = mysql_real_escape_string($_POST["userId"]);
	$text = mysql_real_escape_string($_POST["text"]);

	$checkUser = mysql_query("SELECT * FROM device WHERE api = '$api' AND userId='$userId' AND androidId='$androidId'") or die(mysql_error());
	if(mysql_num_rows($checkUser) > 0) {
		mysql_query("INSERT INTO post (text, threadId, userId) VALUES ('$text', '$threadId', '$userId')") or die(mysql_error());
		$result["success"] = 1;
	} else {
		$result["success"] = 0;
		$result["message"] = getUnAuthorizeMessage();
	}

	encode($result);
	mysql_close($con);

} else if($_GET["action"] == "edit_post" && isset($_GET["threadId"]) && isset($_POST["api"])) {

	require "connect.php";
	$result = array();

	$threadId = mysql_real_escape_string($_GET["threadId"]);
	$api = mysql_real_escape_string($_POST["api"]);
	$androidId = mysql_real_escape_string($_POST["androidId"]);
	$userId = mysql_real_escape_string($_POST["userId"]);
	$text = mysql_real_escape_string($_POST["text"]);
	$postId = mysql_real_escape_string($_POST["postId"]);

	$checkUser = mysql_query("SELECT * FROM device LEFT JOIN user ON device.userId = user.Id WHERE device.api = '$api' AND device.userId='$userId' AND device.androidId='$androidId'") or die(mysql_error());
	if(mysql_num_rows($checkUser) > 0) {
		$row = mysql_fetch_array($checkUser);
		$isModerator = $row["isModerator"];

		if($isModerator == 0) 
			$update = mysql_query("UPDATE post SET text = '$text', isModified = 1 WHERE id = '$postId' AND threadId = '$threadId' AND userId = '$userId'") or die(mysql_error());
		else		
			$update = mysql_query("UPDATE post SET text = '$text', isModified = 1 WHERE id = '$postId' AND threadId = '$threadId'") or die(mysql_error());

		if(mysql_affected_rows() > 0) {
			$result["success"] = 1;
		} else  {
			$result["success"] = 0;
			$result["message"] = "Nothing changed";
		}
	} else {
		$result['success'] = 0;
		$result['message'] = getUnAuthorizeMessage();
	}

	encode($result);

}
else if($_GET["action"] == "delete_post" && isset($_GET["threadId"]) && isset($_POST["api"])) {
	require "connect.php";
	$result = array();

	$threadId = mysql_real_escape_string($_GET["threadId"]);
	$api = mysql_real_escape_string($_POST["api"]);
	$androidId = mysql_real_escape_string($_POST["androidId"]);
	$userId = mysql_real_escape_string($_POST["userId"]);
	$postId = mysql_real_escape_string($_POST["postId"]);

	
	if(checkUserIsModerator($api, $userId, $androidId)) {
		mysql_query("DELETE FROM post WHERE threadId = '$threadId' AND id='$postId'") or die(mysql_error());
		if(mysql_affected_rows() > 0) {
			$result["success"] = 1;
		} else  {
			$result["success"] = 0;
			$result["message"] = "Nothing changed";
		}
	} else {
		$result['success'] = 0;
		$result['message'] = getUnAuthorizeMessage();
	}

	encode($result);
}
else {
	echo "No response";
}
?>