<?php


$response = array();



require_once __DIR__ . '/db_connect.php';

$db = new DB_CONNECT();



if (isset($_GET["email"]) && isset($_GET["pass"])&& isset($_GET["UPemail"])&& isset($_GET["UPpass"])&& isset($_GET["UPfullname"])&& isset($_GET["UPbirthday"])) 
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
        $UPFullName = mysql_real_escape_string($_GET["UPfullname"]);

        $UPEmail = mysql_real_escape_string($_GET["UPemail"]);
 
        $UPPass = mysql_real_escape_string($_GET["UPpass"]);

  
        $UPBirthDay = mysql_real_escape_string($_GET["UPbirthday"]);


         $result2 = mysql_query("UPDATE USERs SET FullName= '$UPFullName', BirthDay='$UPBirthDay'
          , Password ='$UPPass',Email = '$UPEmail' WHERE Email = '$Email'");

  
         if ($result2) {
            $response["message"] = "User successfully updated.";
            $response["success"] = 1;
            $response["userinfo"] = array();
            array_push($response["userinfo"],$userinfo);
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