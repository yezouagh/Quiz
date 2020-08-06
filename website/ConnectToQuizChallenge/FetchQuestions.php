<?php

if(isset($_GET["Themes"]) && isset($_GET["Count"]))
{
$Themes=$_GET["Themes"];
$Count=$_GET["Count"];
$Lang=$_GET["Lang"];
$response = array();
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$result = mysql_query("SELECT * FROM QuestionAndAnswers WHERE Lang='$Lang' and  (".$Themes.") ORDER BY rand()  LIMIT ".$Count.";");
if (!empty($result)) 
{
if (mysql_num_rows($result) > 0) 
{
$response["QuestionAndAnswers"] = array();
while ($row = mysql_fetch_array($result)) 
{
            $Question = array();
            $Question ["Qid"]= $row["Qid"];
            $Question ["Dif"]= $row["Dif"];
            $Question ["Question"] = $row["Question"];
            $Question ["CorrectAnswer"] = $row["CorrectAnswer"];
            $Question ["WrongAnswer1"]= $row["WrongAnswer1"];
            $Question ["WrongAnswer2"]= $row["WrongAnswer2"];
            $Question ["WrongAnswer3"]= $row["WrongAnswer3"];
            $Question ["Dif"]= $row["Dif"];
            array_push($response["QuestionAndAnswers"],$Question);
}
            $response["success"] = 1;
            echo json_encode($response); 
} 
else 
{
            
 $response["success"] = 0;
           
 $response["message"] = "No Questions found";

        
 echo json_encode($response);
        
}
    
} 
else 
{
   
 $response["success"] = 0;
        
 $response["message"] = "error ,No Questions found";

        
 echo json_encode($response);
    
}
}

else 
{
   
 $response["success"] = 0;
             
 echo json_encode($response);
    
}
?>