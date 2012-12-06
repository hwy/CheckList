<?php

if ($_REQUEST['Int_id']>=100000){

mysql_connect('localhost', 'rapidtao_test', 'test');
mysql_query("set names 'utf8'");
mysql_select_db("rapidtao_test");


$res = mysql_query("SELECT * FROM CheckList_List where List_id='".$_REQUEST['Int_id']."'");

$row = mysql_fetch_assoc($res);

$updatecount=$row[Createtime]+1;
//print ($_REQUEST[Updatetime]."aaaaaaaa".$row[Createtime]);
if($_REQUEST[Updatetime]==$row[Createtime]){
echo "ok#xy";

if ($_REQUEST['Count']>0){



for ($count = 0; $count < $_REQUEST['Count']; $count++) {

mysql_query("UPDATE CheckList_Listitems SET Checkstate ='".$_REQUEST[Checkstate][$count]."' WHERE Food_id='".$_REQUEST[Food_id][$count]."' and  List_id='".$_REQUEST[Int_id]."'");

}//end for
mysql_query("UPDATE CheckList_List SET Createtime ='".$updatecount."' WHERE List_id='".$_REQUEST['Int_id']."'");
echo $updatecount;

}else {
echo"n1#xy";
}//end if


}else {//check update time

echo "up#xy".$row['Name']."#xy";

if ($_REQUEST['Count']>0){

for($count = 0; $count < $_REQUEST['Count']; $count++) {

$result = mysql_query("SELECT * FROM CheckList_Listitems where Checkstate ='".$_REQUEST[Checkstate][$count]."' and Food_id='".$_REQUEST[Food_id][$count]."' and  List_id='".$_REQUEST[Int_id]."'");
if(mysql_num_rows($result)>=1){
mysql_query("UPDATE CheckList_Listitems SET Checkstate ='".$_REQUEST[Checkstate][$count]."' WHERE Food_id='".$_REQUEST[Food_id][$count]."' and  List_id='".$_REQUEST[Int_id]."'");

}else{

if($_REQUEST[Food_id][$count]!=null||$_REQUEST[Food_id][$count]!=""){
print($_REQUEST[Food_id][$count]."#zy");  }

}//end if

}//end for


}else{
$updatecount=$row[Createtime];
echo "no";}

echo "#xy".$updatecount."#xy";
mysql_query("UPDATE CheckList_List SET Createtime ='".$updatecount."' WHERE List_id='".$_REQUEST['Int_id']."'");
$q=mysql_query("SELECT * FROM CheckList_Listitems WHERE List_id ='".$_REQUEST['Int_id']."'");

while($e=mysql_fetch_assoc($q))

              $output[]=$e;

           print(json_encode($output));

mysql_close();
}
}
?> 			        