<?php


require_once __DIR__ . '/db_connect.php';

$db = new DB_CONNECT();


$response = array();



if (isset($_GET['fullname']) && isset($_GET['email']) && isset($_GET['pass']))
{
    
    
$FullName = mysql_real_escape_string($_GET['fullname']);

$Email=mysql_real_escape_string($_GET['email']);
$Pass = mysql_real_escape_string($_GET['pass']);

  
$BirthDay = mysql_real_escape_string($_GET['birthday']);

 
$result = mysql_query("insert into USERs (FullName,BirthDay,Email,Password,WhoAreYou) VALUES('$FullName','$BirthDay', '$Email', '$Pass',1)");
if ($result) {

$response["success"] = 1;

$response["message"] = "User successfully created.";

echo json_encode($response);
    
} 
else {

$response["success"] = 0;

$response["message"] = "Oops! An error occurred.";

echo json_encode($response);
    
}

} 
else {
 
$response["success"] = 0;

$response["message"] = "Required field(s) is missing";

echo json_encode($response);

}
?>