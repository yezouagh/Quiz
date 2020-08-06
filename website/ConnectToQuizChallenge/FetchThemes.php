<?php
$Lang="en";
if(isset($_GET["Lang"]))
{
$Lang=$_GET["Lang"];
}
$response = array();
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$result = mysql_query("SELECT Themes.ThemeName,(IFNULL(COUNT(QuestionAndAnswers.Qid),0)) as 'NBR_Q_T' FROM Themes LEFT JOIN QuestionAndAnswers ON Themes.ThemeName=QuestionAndAnswers.ThemeName Where Themes.Lang='$Lang' GROUP BY Themes.ThemeName") or die(mysql_error());
if (!empty($result)) 
{
if (mysql_num_rows($result) > 0) 
{
             $response["Themes"] = array();
  while ($row = mysql_fetch_array($result)) {
            $Theme=array();
            $Theme["ThemeName"]= $row["ThemeName"];
            $Theme["NBR_Q_T"]= $row["NBR_Q_T"];
            array_push($response["Themes"],$Theme);
            }
             $response["success"] = 1;
            echo json_encode($response); 
} 
else 
{
 $response["success"] = 0;
 $response["message"] = "No Theme found";
 echo json_encode($response);
}
} 
else 
{
 $response["success"] = 0;
 $response["message"] = "No Theme found";
 echo json_encode($response);
}
?>