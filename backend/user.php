<?php

	require "func.php";
	
	if($_GET["action"] == "register" AND isset($_REQUEST["username"])) {
		include("connect.php");

		$username = mysql_real_escape_string($_REQUEST["username"]);
		$password = mysql_real_escape_string($_REQUEST["password"]);

		// check username exist
		$checkExist = mysql_query("SELECT * FROM user WHERE username = '$username'") or die(mysql_error());

		$result = array();
	
		if(mysql_num_rows($checkExist) > 0) {
			$result["success"] =0;
			$result["message"] = "Username is already exist";
			encode($result);
			die();
		}

		$insertquery = "INSERT INTO user (username, password) VALUES ('$username', 
			'$password')";

		mysql_query($insertquery) or die(mysql_error()." Query : ".$insertquery);

		if(mysql_affected_rows() > 0) {
			$result["success"] = 1;
		} else {
			$result["success"] = 0;
			$result["message"] = "Something was wrong with the query";
		}
		encode($result);
		mysql_close($con);

	} else if($_GET["action"] == "login" AND isset($_POST["username"])) {

		include "connect.php";
		$username = mysql_real_escape_string($_POST["username"]);
		$password = mysql_real_escape_string($_POST["password"]);
		$androidId = mysql_real_escape_string($_POST["androidId"]);
		$androidName = mysql_real_escape_string($_POST["androidName"]);

		$result = array();

		$checkQuery = "SELECT * FROM user WHERE username ='$username' AND password = '$password'";

		$sql = mysql_query($checkQuery) or die(mysql_error(). " query : ".$checkQuery);

		if(mysql_num_rows($sql) > 0) {
			$row = mysql_fetch_array($sql);
			$userId = $row["id"];
			$username = $row["username"];
			$api = uniqid();
			$fullname = $row["fullname"];
			$profileUrl = $row["profileUrl"];
			$profileBg = $row["profileBg"];
			$aboutMe = $row["aboutMe"];
			$email = $row["email"];
			$isModerator = $row["isModerator"];

			$sql = "INSERT INTO device (name, userId, api, androidId) VALUES ('$androidName',
				'$userId', '$api', '$androidId') ON DUPLICATE KEY UPDATE name=VALUES(name), userId=VALUES(userId),
				api=VALUES(api)";
			mysql_query($sql) or die(mysql_error()." Query : ".$sql);

			$result["success"] = 1;
			$result["data"] = array("id" => $userId, 
				"api"=>$api, 
				"username" => $username,
				"fullname" => $fullname,
				"profileUrl" => $profileUrl, 
				"profileBg" => $profileBg, 
				"aboutMe" => $aboutMe,
				"email" => $email,
				"isModerator"=>$isModerator);

		} else {
			$result["success"] = 0;
			$result["message"] = "Username or password is incorrect";
		}

		encode($result);

		mysql_close($con);
	} else if($_GET["action"] == "logout" && isset($_POST["api"])) {
		include "connect.php";
		$result = array();

		$userId = mysql_real_escape_string($_POST["userId"]);
		$api = mysql_real_escape_string($_POST["api"]);

		$query = "DELETE FROM device WHERE userId = '$userId' AND api = '$api'";

		mysql_query($query) or die(mysql_error()." Query :".$query);

		if(mysql_affected_rows() > 0) {
			$result["success"] = 1;
		} else { 
			$result["success"] = 0;
			$result["message"] = "There is no such data in database";
		}

		encode($result);
		mysql_close($con);

	} else if($_GET["action"] == "edit_profile") {
		include "connect.php";
		$result = array();

		$userId = mysql_real_escape_string($_POST["userId"]);
		$api = mysql_real_escape_string($_POST["api"]);
		$androidId = mysql_real_escape_string($_POST["androidId"]);

		$fullName = mysql_real_escape_string($_POST["fullName"]);
		$email = mysql_real_escape_string($_POST["email"]);
		$aboutMe = mysql_real_escape_string($_POST["aboutMe"]);

		$checkUser = mysql_query("SELECT * FROM device WHERE api = '$api' AND userId='$userId' AND androidId='$androidId'") or die(mysql_error());
		if(mysql_num_rows($checkUser) > 0) {
			mysql_query("UPDATE user SET fullName = '$fullName', email = '$email', aboutMe = '$aboutMe' WHERE id = '$userId'") or die(mysql_error());
			$result["success"] = 1;
		} else {
			$result["success"] = 0;
			$result["message"] = getUnAuthorizeMessage();
		}

		encode($result);
		mysql_close($con);
	} else if($_GET["action"] == "upload_profile") {
		include "connect.php";
		$api = mysql_real_escape_string($_GET["api"]);
		$userId = mysql_real_escape_string($_GET["userId"]);
		$type = mysql_real_escape_string($_GET["type"]);
		$result = array();

		$checkUser = mysql_query("SELECT * FROM device WHERE api = '$api' AND userId='$userId'") or die(mysql_error());
		if(mysql_num_rows($checkUser) > 0) {
		
			$targetDir = "uploads/".$userId;
			$uploadFile = $_FILES["image"];

			if($uploadFile["type"] != "image/*" && $uploadFile["type"] != "image/png" && $uploadFile["type"] != "image/jpeg" && $uploadFile["type"] != "image/jpg" && $uploadFile["type"] != "image/gif") {
				$result["success"] = 0;
				$result["message"] = "File is invalid";
				encode($result);
				die();
			}

			if(!file_exists($targetDir)) {
				mkdir($targetDir, 0777, true);
			}

		//	$fileName = "/profile_pic.".pathInfo($uploadFile["name"], PATHINFO_EXTENSION);
			$fileName = "/".$uploadFile["name"];
			$targetDir = $targetDir .$fileName;

			if(move_uploaded_file($_FILES["image"]["tmp_name"], $targetDir)) {
				$fullUrl = "./".$targetDir;
				if($type == 1) {
					mysql_query("UPDATE user SET profileUrl ='$fullUrl' WHERE id='$userId'") or die(mysql_error());
				} else {
					mysql_query("UPDATE user SET profileBg = '$fullUrl' WHERE id='$userId'") or die(mysql_error());
				}


				$result["success"] = 1;
				$result["message"] = $fullUrl;
			} else {
				$result["success"] = 0;
				$result["message"] = "Unknown error";
			}

		} else {
			$result["success"] =0;
			$result["message"] = getUnAuthorizeMessage();
		}
		
		encode($result);
		mysql_close($con);	
	} else if($_GET['action'] == "remove_user" && isset($_POST["api"]) && isset($_POST["userId"])) {
		require "connect.php";

		$requestedUserId = mysql_real_escape_string($_POST["requestedUserId"]);
		$userId = mysql_real_escape_string($_POST["userId"]);
		$api = mysql_real_escape_string($_POST["api"]);
		$androidId = mysql_real_escape_string($_POST["androidId"]);
		$result = array();

		if(checkUserIsModerator($api, $userId, $androidId)) {

			mysql_query("DELETE FROM user WHERE id = '$requestedUserId'") or die(mysql_error());
			mysql_query("DELETE FROM thread WHERE userId = '$requestedUserId'") or die(mysql_error());
			mysql_query("DELETE FROM post WHERE userId = '$requestedUserId'") or die(mysql_error());
			mysql_query("DELETE FROM device WHERE userId = '$requestedUserId'") or die(mysql_error());
			
			$result["success"] = 1;
		} else {
			$result["success"] = 0;
			$result["message"] = getUnAuthorizeMessage();
		}

		encode($result);
		mysql_close($con);
	}
	else {
		echo "no action detected";
	}

?>