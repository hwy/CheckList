<?
if ($_REQUEST['loadlistid']!=null){
mysql_connect('localhost', 'rapidtao_test', 'test');
mysql_query("set names 'utf8'");
mysql_select_db("rapidtao_test");


$result = mysql_query("SELECT * FROM CheckList_List where List_id='".$_REQUEST['loadlistid']."'");

if(mysql_num_rows($result)==1){

mysql_query("UPDATE CheckList_List SET loadcount = loadcount + 1 where List_id='".$_REQUEST['loadlistid']."'");

$row = mysql_fetch_array($result);
echo "sucess#xy".$row['List_id']."#xy".$row['Name']."#xy".$row['Type']."#xy".$row['Pw']."#xy".$row['Createtime']."#xy";



$q=mysql_query("SELECT * FROM CheckList_Listitems Where List_id='".$_REQUEST['loadlistid']."'");

while($e=mysql_fetch_assoc($q))

              $output[]=$e;

           print(json_encode($output));
                  
}else{
echo "nth";
}

mysql_close();
}
?>