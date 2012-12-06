<?
mysql_connect('localhost', 'rapidtao_test', 'test');
mysql_query("set names 'utf8'");
mysql_select_db("rapidtao_test");

$res = mysql_query("SELECT * FROM CheckList_List where List_id='100036'");

$row = mysql_fetch_assoc($res);

echo $row['Name']."#xy".$row['Createtime']."#xy";

//$row = mysql_fetch_assoc($res);
//echo $row['Createtime'];
?>