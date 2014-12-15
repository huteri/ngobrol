<?php
	require "func.php";
	
	if($_GET["action"] = "get_categories") {
		require "connect.php";
		$result = array();

		$sql = mysql_query("SELECT * FROM category ORDER BY name") or die(mysql_error());
		$result["success"] = 1;
		while($row = mysql_fetch_array($sql)) {
			$data[] = array("id"=>$row["id"],
				"name" => $row["name"],
				"color" => $row["color"],
				"icon"=>$row["icon"]);
		}

		$result["data"] = $data;

		encode($result);
		mysql_close($con);

	}
?>