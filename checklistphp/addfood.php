<?php


if ($_REQUEST['Foodlist_id']==1){
$Listtype=CheckList_BBQ_Types_table;
$Foodlist=CheckList_Bbq_Food;
}else{
$Listtype=CheckList_HOT_Types_table;
$Foodlist=CheckList_Hotpot_Food;
}

if($_REQUEST['Name']){

mysql_connect('localhost', 'rapidtao_test', 'test');
mysql_query("set names 'utf8'");
mysql_select_db("rapidtao_test");

$checkfoodname = "SELECT TYPES FROM ".$Listtype."
WHERE Types_id = (
SELECT Types_id
FROM ".$Foodlist."
WHERE Name = '".$_REQUEST[Name]."')";

$result = mysql_query($checkfoodname);  

if($row=mysql_fetch_object($result)){
Print($row->TYPES."#xy");
}else{

$addfd ="INSERT INTO ".$Foodlist." (Types_id, Name, Vote, Report) VALUES ('".$_REQUEST[Types_id]."','".$_REQUEST[Name]."', '0','0')";

if(mysql_query($addfd)){print("sucess#xy");}else{print("Added Error#xy");}


}


$q=mysql_query("SELECT * FROM ".$Foodlist." ORDER BY Vote DESC, Types_id, Food_id asc");

while($e=mysql_fetch_assoc($q))

              $output[]=$e;

           print(json_encode($output));
                  

mysql_close();
}
?>