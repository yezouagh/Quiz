<?php
$response = array();
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
if (isset($_GET["email"]) && isset($_GET["Score"])) 
{
$Email=mysql_real_escape_string($_GET['email']);
$Score=mysql_real_escape_string($_GET["Score"]);

       $result = mysql_query("UPDATE USERs SET Score=Score+$Score WHERE Email = '$Email'");
         if ($result) {
            $response["message"] = "User successfully updated.";
            $response["success"] = 1;
            echo json_encode($response);  
             } 
              else 
             {
              $response["success"] = 0;
              $response["message"] = "Error Updating the data Entered.";
              echo json_encode($response);
             }            
    } 
else 
{
$response["success"] = 0;
$response["message"] = "Required field(s) is missing";    
echo json_encode($response);

}

?>