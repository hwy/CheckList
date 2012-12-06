<?php


if ($_REQUEST['List_Name']!=null){

mysql_connect('localhost', 'rapidtao_test', 'test');
mysql_query("set names 'utf8'");
mysql_select_db("rapidtao_test");
$updatecount=0;
$count=0;
$res = mysql_query("SELECT * FROM CheckList_List where List_id='".$_REQUEST['List_id']."'");

$row = mysql_fetch_assoc($res);

$updatecount=$row[Createtime]+1;

if ($_REQUEST['List_Type']==1){
$typetable="CheckList_Bbq_Food";
}else{
$typetable="CheckList_Hotpot_Food";
}

if($_REQUEST[List_id]>=100000) {
mysql_query("UPDATE CheckList_List SET Name ='".$_REQUEST['List_Name']."', Createtime='".$updatecount."' WHERE List_id='".$_REQUEST[List_id]."' ");


mysql_query("DELETE FROM CheckList_Listitems WHERE List_id='".$_REQUEST[List_id]."'");

for ($count = 0; $count <= $_REQUEST['Count']; $count++) {
mysql_query("INSERT INTO CheckList_Listitems (List_id, Food_id, Checkstate, Price, Qty) VALUES ('".$_REQUEST[List_id]."' , '".$_REQUEST['Food_id'][$count]."', '".$_REQUEST['Checkstate'][$count]."', '".$_REQUEST['Price'][$count]."', '".$_REQUEST['Qty'][$count]."')");

}//END FOR
print("update#xy".$updatecount);
}else {

$random=rand(1000, 9999); 

if(mysql_query("INSERT INTO CheckList_List (Name, Type, Pw, Createtime) VALUES ('".$_REQUEST['List_Name']."' ,'".$_REQUEST['List_Type']."', '".$random."', '".$updatecount."')"))
{print("sucess#xy");}
$rtn=mysql_insert_id();


for ($count = 0; $count <= $_REQUEST['Count']; $count++) {
mysql_query("INSERT INTO CheckList_Listitems (List_id, Food_id, Checkstate, Price, Qty) VALUES ('".$rtn."' , '".$_REQUEST['Food_id'][$count]."', '".$_REQUEST['Checkstate'][$count]."', '".$_REQUEST['Price'][$count]."', '".$_REQUEST['Qty'][$count]."')");
mysql_query("UPDATE ".$typetable." SET Vote=Vote+1 WHERE Food_id=".$_REQUEST['Food_id'][$count]);
}//END FOR
Print($rtn."#xy".$random."#xy".$updatecount);

}

mysql_close();
}



?>
 			        