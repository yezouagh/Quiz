<?php


$response = array();



require_once __DIR__ . '/db_connect.php';

$db = new DB_CONNECT();



if (isset($_GET["email"]) && isset($_GET["pass"])) 
{
   
$Email=mysql_real_escape_string($_GET['email']);
$Pass=mysql_real_escape_string($_GET["pass"]);

 $result = mysql_query("SELECT * FROM USERs WHERE Email='$Email' and Password = '$Pass'");

    
if (!empty($result)) 
{
       
if (mysql_num_rows($result) > 0) 
{

           
  $result = mysql_fetch_array($result);
            $userinfo=array();
            $userinfo["fullname"]= $result["FullName"];
            $userinfo["birthday"] = $result["BirthDay"];
            $userinfo["email"] = $result["Email"];
            $userinfo["pass"]= $result["Password"];
            $response["success"] = 1;
            $response["userinfo"] = array();
            array_push($response["userinfo"],$userinfo);
            echo json_encode($response); 
} 
else 
{
            
 $response["success"] = 0;
           
 $response["message"] = "No User found";

        
 echo json_encode($response);
        
}
    
} 
else 
{
   
 $response["success"] = 0;
        
 $response["message"] = "No User found";

        
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