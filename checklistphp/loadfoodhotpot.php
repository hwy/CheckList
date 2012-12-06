<?php

          mysql_connect('localhost', 'rapidtao_test', 'test');
		mysql_query("set names 'utf8'");
          mysql_select_db("rapidtao_test");

$q=mysql_query("SELECT * FROM CheckList_Hotpot_Food ORDER BY Vote DESC, Types_id, Food_id asc");

/*
$in="INSERT INTO CheckList_List (Name, Pw, Createtime) VALUES ('".$_REQUEST[newListname]."', '".$_REQUEST[Senderemail]."',  '".$_REQUEST[Title]."', '".$_REQUEST[Msg]."', '".$_REQUEST[Sendmsgtime]."')";
if(mysql_query($in)){print(mysql_insert_id()+"@^x");}else{print($in);}
*/

while($e=mysql_fetch_assoc($q))

              $output[]=$e;

           print(json_encode($output));
                  


        mysql_close();

?>