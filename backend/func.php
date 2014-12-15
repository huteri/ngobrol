<?php
function encode($result) {
	echo json_encode($result);
}

function checkUser($api, $userId, $androidId) {
	$checkUser = mysql_query("SELECT * FROM device WHERE api = '$api' AND userId='$userId' AND androidId='$androidId'") or die(mysql_error());
	if(mysql_num_rows($checkUser) > 0) {
		return true;
	} else {
		return false;
	}
}

function checkUserIsModerator($api, $userId, $androidId) {

	$queryCheck = "SELECT * FROM device LEFT JOIN user ON device.userId = user.Id WHERE device.api = '$api' AND device.userId='$userId' AND device.androidId='$androidId' AND user.isModerator = 1";

	$checkUser = mysql_query($queryCheck) or die(mysql_error());
	if(mysql_num_rows($checkUser) > 0) {
		return true;
	} else 
		return false;

}

function getUnAuthorizeMessage() {
	return "You're not authorized to perform this action. Please logout and login again.";
}
?>