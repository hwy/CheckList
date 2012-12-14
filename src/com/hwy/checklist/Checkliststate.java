package com.hwy.checklist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hwy.checklist.library.PullToRefreshBase.OnRefreshListener;
import com.hwy.checklist.library.PullToRefreshExpandableListView;


public class Checkliststate extends ExpandableListActivity  {

		//用一个list对象保存所有的二级列表数据
		List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();

	 
	 //SQL
		//SQLiteDatabase对象
		SQLiteDatabase db;
		//数据库名
		public String db_name = "CheckList.db";
		//SQL表名
		public String table_name;
		public String CheckList_Food;
		//TYPE
		public int gettypeno;
		//Table id
		String Table_id; 
		//辅助类名
		final DbHelper helper = new DbHelper(this, db_name, null, 2);
	 //end sql
	 
		ExpandableChecklist viewAdapter;
		List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
		Map<String, String> group1 = new HashMap<String, String>();
		Map<String, String> group2 = new HashMap<String, String>();
		Map<String, String> group3 = new HashMap<String, String>();
		Map<String, String> group4 = new HashMap<String, String>();
		Map<String, String> group5 = new HashMap<String, String>();
		Map<String, String> group6 = new HashMap<String, String>();
		Map<String, String> group7 = new HashMap<String, String>();
		Map<String, String> group8 = new HashMap<String, String>();
		Map<String, String> group9 = new HashMap<String, String>();
		Map<String, String> group10 = new HashMap<String, String>();

		
	    List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child2 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child3 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child4 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child5 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child6 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child7 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child8 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child9 = new ArrayList<Map<String, String>>();
      	List<Map<String, String>> child10 = new ArrayList<Map<String, String>>();      	      	
		
		
	//loading hp	
	 public static final int MEG_INVALIDATE = 1110; //自訂一個號碼
	 Thread t;
	 ProgressDialog dialog;

	 int Int_id=0;
	 public String newListname;
	 private PullToRefreshExpandableListView mPullRefreshListView;
	    
	 
	 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.showlist);

	TabActivity parent = (TabActivity) getParent();
	TabHost tabhost = parent.getTabHost();
	
	
	 Intent i = getParent().getIntent(); 


		gettypeno=tabhost.getCurrentTab();



    //未有NO.
	 if (gettypeno==1){
		 Table_id = i.getStringExtra("Bbq_id");	
		 table_name="CheckList_BbqListitems";
		 CheckList_Food="CheckList_Bbq_Food";
		 System.out.println("CheckList_Bbq_Food");
	 }else if (gettypeno==2){
		 Table_id = i.getStringExtra("Hotpot_id");
		 table_name="CheckList_HotListitems";
		 CheckList_Food="CheckList_Hot_Food";
		 System.out.println("CheckList_Hot_Food");
	 }else  if (gettypeno==3){
		 Table_id = i.getStringExtra("Travel_id");
		 table_name="CheckList_TravelListitems";
		 CheckList_Food="CheckList_Travel";
		 System.out.println("CheckList_Travel");	 };
	 
	 System.out.println("第一次"+Table_id);
	
	 
	 if(Table_id==null){
		 newlistbox ();
		// 建立新表..
	 }else {

	  Setuplist(Table_id);
    	  
	  Cursor cu = db.query("CheckList_List", null, "List_id="+Table_id, null, null,	null, null);
  	cu.moveToFirst();

  	if(cu.getInt(1)>=100000){
 
	  t=new Thread() {
          public void run() {
         	 
    		Looper.prepare();   		
    		Itemchangestate();
    			Message m = new Message();
     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
     		m.what = MEG_INVALIDATE;
     		handler.sendMessage(m);
     		
     		
    	    Looper.loop();
         	
          }
    };      
    t.start();
  	}//end check intel id
    
    
		 }//end if tableid =null
 
    	 
    	 
	}//end create


	//Setup expandable list view
	private void Setuplist(final String Table_id){

 
		//sql
		//从辅助类获得数据库对象
		
		if (db==null){
    		 db = helper.getWritableDatabase();
    		}

		//初始化数据
		//initDatabase(db); <==load 資料
			    
		   
		 Button buttonUpdate = (Button) findViewById(R.id.Buttonupdate);
		 Cursor cu = db.query("CheckList_List", null, "List_id="+Table_id, null, null,	null, null);
    	cu.moveToFirst();
    	String disptext;
    	
        newListname=cu.getString(2);

    	if(cu.getInt(1)>=100000){
    	disptext = "名: "+newListname+" ID:"+cu.getInt(1)+" PW:"+cu.getString(4);
    	Int_id=cu.getInt(1);
		  
    	}else{
    	disptext = "名: "+newListname+"(尚未上傳到網上)";	
    	buttonUpdate.setVisibility(View.GONE);
    	}
    	
      TextView textViewidpw = (TextView)findViewById(R.id.textViewidpw);    
      textViewidpw.setText(disptext);

   	 Button buttonEdit = (Button) findViewById(R.id.Buttonedit);
   	 buttonEdit.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {

			    Intent i = getParent().getIntent();
			    i.putExtra("newListname", newListname);
			    i.putExtra("gettypeno", Integer.toString(gettypeno));
			    i.putExtra("tableid", Table_id);
			    System.out.println(gettypeno+newListname+Table_id);
			    TabActivity ta = (TabActivity) Checkliststate.this.getParent();
			    ta.getTabHost().setCurrentTab(4);
		
           }
       });
    	
   	
   	 
   	 
   	 

   	 buttonUpdate.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
           	
           	
           	 showDialog(0);
           	 

               	
            		   t=new Thread() {
            	           public void run() {
            	          	 
            	     		Looper.prepare();   		
            	     		Itemchangestate();
	     	     			Message m = new Message();
		     	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
		     	     		m.what = MEG_INVALIDATE;
		     	     		handler.sendMessage(m);
		     	     		
		     	     		
            	     	    Looper.loop();
            	          	
            	           }
            	     };      
            	     t.start();
            	     
    
           }
       });
    	
   	 pricecount();

   	 
		//准备一级列表中显示的数据:2个一级列表,分别显示"group1"和"group2"
   	groups.clear();
   	
   	if (gettypeno==1){
		Cursor c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='1' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		
		group1.put("group", " 蔬菜類   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='2' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group2.put("group", "丸類  "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='3' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group3.put("group", "肉類  "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='4' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group4.put("group", "海鮮類  "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='5' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group5.put("group", "其他  "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='6' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group6.put("group", "工具  "+c.getInt(0)+"個");
				
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		groups.add(group4);
		groups.add(group5);
		groups.add(group6);
					 		 	

		child1.clear();
	   	child2.clear();
	   	child3.clear();
	   	child4.clear();
	   	child5.clear();
	   	child6.clear();


      	
      	
      	//display all data
		//	Cursor c = db.query(table_name, null, "List_id2="+Table_id, null, null, null, null);
      	c = db.rawQuery("SELECT "+table_name+".*, "+CheckList_Food+".Name, "+CheckList_Food+".Types_id FROM "+table_name+", "+CheckList_Food+" WHERE "+table_name+".List_id2="+Table_id+" and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);	
      	            	
      	String bbc;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			
			if(c.getString(2).equals("1")){
				bbc="true";
			}else{
				bbc="false";
			}
				
				
			switch (c.getInt(7)) {
            case 1:       	            	
            	  Map<String, String> child1Data1 = new HashMap<String, String>();
            	  child1Data1.put("foodname", c.getString(6));
            	  child1Data1.put("checkboxstate", bbc);
            	  child1Data1.put("Food_id", c.getString(1));
            	  child1Data1.put("Price", c.getString(3));
            	  child1Data1.put("Qty", c.getString(4));
            	  child1.add(child1Data1);
                  break;
            case 2: 
            	  Map<String, String> child2Data1 = new HashMap<String, String>();
            	  child2Data1.put("foodname", c.getString(6));
            	  child2Data1.put("checkboxstate", bbc);
            	  child2Data1.put("Food_id", c.getString(1));
            	  child2Data1.put("Price", c.getString(3));
            	  child2Data1.put("Qty", c.getString(4));
            	  child2.add(child2Data1);
                  break;
            case 3: 
            	  Map<String, String> child3Data1 = new HashMap<String, String>();
            	  child3Data1.put("foodname", c.getString(6));
            	  child3Data1.put("checkboxstate", bbc);
            	  child3Data1.put("Food_id", c.getString(1));
            	  child3Data1.put("Price", c.getString(3));
            	  child3Data1.put("Qty", c.getString(4));
            	  child3.add(child3Data1);
                  break;
            case 4:  
            	  Map<String, String> child4Data1 = new HashMap<String, String>();
            	  child4Data1.put("foodname", c.getString(6));
            	  child4Data1.put("checkboxstate", bbc);
            	  child4Data1.put("Food_id", c.getString(1));
            	  child4Data1.put("Price", c.getString(3));
            	  child4Data1.put("Qty", c.getString(4));
            	  child4.add(child4Data1);
                  break;
            case 5:  
            	  Map<String, String> child5Data1 = new HashMap<String, String>();
            	  child5Data1.put("foodname", c.getString(6));
            	  child5Data1.put("checkboxstate", bbc);
            	  child5Data1.put("Food_id", c.getString(1));
            	  child5Data1.put("Price", c.getString(3));
            	  child5Data1.put("Qty", c.getString(4));
            	  child5.add(child5Data1);
                  break;
            case 6: 
            	  Map<String, String> child6Data1 = new HashMap<String, String>();
            	  child6Data1.put("foodname", c.getString(6));
            	  child6Data1.put("checkboxstate", bbc);
            	  child6Data1.put("Food_id", c.getString(1));
            	  child6Data1.put("Price", c.getString(3));
            	  child6Data1.put("Qty", c.getString(4));
            	  child6.add(child6Data1);
                  break;
        	}
			}
   
			c.close();
			cu.close();	
	    childs.add(child1);
       	childs.add(child2);
       	childs.add(child3);
       	childs.add(child4);
       	childs.add(child5);
       	childs.add(child6);
   	}else if (gettypeno==2){
   		
		Cursor c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='1' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();	
		group1.put("group", "湯底   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='2' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group2.put("group", "肉類   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='3' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group3.put("group", "海鮮類   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='4' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group4.put("group", "丸類   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='5' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group5.put("group", "其他   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='6' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group6.put("group", "鮮菌類   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='7' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group7.put("group", "時蔬類   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='8' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group8.put("group", "粉麵類   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='9' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group9.put("group", "醬料   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='10' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group10.put("group", "飲品   "+c.getInt(0)+"個");
	
		
		
		
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		groups.add(group4);
		groups.add(group5);
		groups.add(group6);
		groups.add(group7);
		groups.add(group8);
		groups.add(group9);
		groups.add(group10);
		

		child1.clear();
	   	child2.clear();
	   	child3.clear();
	   	child4.clear();
	   	child5.clear();
	   	child6.clear();
	   	child7.clear();
	   	child8.clear();
	   	child9.clear();
	   	child10.clear();


      	
      	
      	//display all data
		//	Cursor c = db.query(table_name, null, "List_id2="+Table_id, null, null, null, null);
      	c = db.rawQuery("SELECT "+table_name+".*, "+CheckList_Food+".Name, "+CheckList_Food+".Types_id FROM "+table_name+", "+CheckList_Food+" WHERE "+table_name+".List_id2="+Table_id+" and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);	
      	            	
      	String bbc;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			
			if(c.getString(2).equals("1")){
				bbc="true";
			}else{
				bbc="false";
			}
				
				
			switch (c.getInt(7)) {
	        case 1:       	            	
	        	  Map<String, String> child1Data1 = new HashMap<String, String>();
	        	  child1Data1.put("foodname", c.getString(6));
	        	  child1Data1.put("checkboxstate", bbc);
	        	  child1Data1.put("Food_id", c.getString(1));
	        	  child1Data1.put("Price", c.getString(3));
	        	  child1Data1.put("Qty", c.getString(4));
	        	  child1.add(child1Data1);
	              break;
	        case 2: 
	        	  Map<String, String> child2Data1 = new HashMap<String, String>();
	        	  child2Data1.put("foodname", c.getString(6));
	        	  child2Data1.put("checkboxstate", bbc);
	        	  child2Data1.put("Food_id", c.getString(1));
	        	  child2Data1.put("Price", c.getString(3));
	        	  child2Data1.put("Qty", c.getString(4));
	        	  child2.add(child2Data1);
	              break;
	        case 3: 
	        	  Map<String, String> child3Data1 = new HashMap<String, String>();
	        	  child3Data1.put("foodname", c.getString(6));
	        	  child3Data1.put("checkboxstate", bbc);
	        	  child3Data1.put("Food_id", c.getString(1));
	        	  child3Data1.put("Price", c.getString(3));
	        	  child3Data1.put("Qty", c.getString(4));
	        	  child3.add(child3Data1);
	              break;
	        case 4:  
	        	  Map<String, String> child4Data1 = new HashMap<String, String>();
	        	  child4Data1.put("foodname", c.getString(6));
	        	  child4Data1.put("checkboxstate", bbc);
	        	  child4Data1.put("Food_id", c.getString(1));
	        	  child4Data1.put("Price", c.getString(3));
	        	  child4Data1.put("Qty", c.getString(4));
	        	  child4.add(child4Data1);
	              break;
	        case 5:  
	        	  Map<String, String> child5Data1 = new HashMap<String, String>();
	        	  child5Data1.put("foodname", c.getString(6));
	        	  child5Data1.put("checkboxstate", bbc);
	        	  child5Data1.put("Food_id", c.getString(1));
	        	  child5Data1.put("Price", c.getString(3));
	        	  child5Data1.put("Qty", c.getString(4));
	        	  child5.add(child5Data1);
	              break;
	        case 6: 
	        	  Map<String, String> child6Data1 = new HashMap<String, String>();
	        	  child6Data1.put("foodname", c.getString(6));
	        	  child6Data1.put("checkboxstate", bbc);
	        	  child6Data1.put("Food_id", c.getString(1));
	        	  child6Data1.put("Price", c.getString(3));
	        	  child6Data1.put("Qty", c.getString(4));
	        	  child6.add(child6Data1);
	              break;
	        case 7: 
	      	  Map<String, String> child7Data1 = new HashMap<String, String>();
	      	  child7Data1.put("foodname", c.getString(6));
	      	child7Data1.put("checkboxstate", bbc);
	      	child7Data1.put("Food_id", c.getString(1));
	      	child7Data1.put("Price", c.getString(3));
	      	child7Data1.put("Qty", c.getString(4));
	      	  child7.add(child7Data1);
	            break;
	        case 8: 
	      	  Map<String, String> child8Data1 = new HashMap<String, String>();
	      	  child8Data1.put("foodname", c.getString(6));
	      	child8Data1.put("checkboxstate", bbc);
	      	child8Data1.put("Food_id", c.getString(1));
	      	child8Data1.put("Price", c.getString(3));
	      	child8Data1.put("Qty", c.getString(4));
	      	  child8.add(child8Data1);
	            break;
	        case 9: 
	      	  Map<String, String> child9Data1 = new HashMap<String, String>();
	      	  child9Data1.put("foodname", c.getString(6));
	      	child9Data1.put("checkboxstate", bbc);
	      	child9Data1.put("Food_id", c.getString(1));
	      	child9Data1.put("Price", c.getString(3));
	      	child9Data1.put("Qty", c.getString(4));
	      	  child9.add(child9Data1);
	            break;
	        case 10: 
	      	  Map<String, String> child10Data1 = new HashMap<String, String>();
	      	  child10Data1.put("foodname", c.getString(6));
	      	child10Data1.put("checkboxstate", bbc);
	      	child10Data1.put("Food_id", c.getString(1));
	      	child10Data1.put("Price", c.getString(3));
	      	child10Data1.put("Qty", c.getString(4));
	      	  child10.add(child10Data1);
	            break;
	    	}
			}
   
			c.close();
			cu.close();	
			childs.add(child1);
			childs.add(child2);
			childs.add(child3);
			childs.add(child4);
			childs.add(child5);
			childs.add(child6);
			childs.add(child7);
			childs.add(child8);
			childs.add(child9);
			childs.add(child10);
   	}   else if (gettypeno==3){
   		
		Cursor c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='1' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();	
		group1.put("group", "證明文件  "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='2' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group2.put("group", "金錢   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='3' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group3.put("group", "衣物   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='4' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group4.put("group", "個人護理   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='5' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group5.put("group", "小型電器   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='6' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group6.put("group", "個人護理–女性   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='7' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group7.put("group", "其他   "+c.getInt(0)+"個");

		c = db.rawQuery("SELECT COUNT("+CheckList_Food+".Types_id) FROM "+table_name+", "+CheckList_Food+" WHERE  "+table_name+".List_id2="+Table_id+" and "+CheckList_Food+".Types_id='8' and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);
		c.moveToFirst();
		group8.put("group", "藥品   "+c.getInt(0)+"個");

		
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		groups.add(group4);
		groups.add(group5);
		groups.add(group6);
		groups.add(group7);
		groups.add(group8);


		child1.clear();
	   	child2.clear();
	   	child3.clear();
	   	child4.clear();
	   	child5.clear();
	   	child6.clear();
	   	child7.clear();
	   	child8.clear();
      	
      	
      	//display all data
		//	Cursor c = db.query(table_name, null, "List_id2="+Table_id, null, null, null, null);
      	c = db.rawQuery("SELECT "+table_name+".*, "+CheckList_Food+".Name, "+CheckList_Food+".Types_id FROM "+table_name+", "+CheckList_Food+" WHERE "+table_name+".List_id2="+Table_id+" and "+table_name+".Food_id="+CheckList_Food+".Food_id", null);	
      	            	
      	String bbc;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			
			if(c.getString(2).equals("1")){
				bbc="true";
			}else{
				bbc="false";
			}
				
				
			switch (c.getInt(7)) {
	        case 1:       	            	
	        	  Map<String, String> child1Data1 = new HashMap<String, String>();
	        	  child1Data1.put("foodname", c.getString(6));
	        	  child1Data1.put("checkboxstate", bbc);
	        	  child1Data1.put("Food_id", c.getString(1));
	        	  child1Data1.put("Price", c.getString(3));
	        	  child1Data1.put("Qty", c.getString(4));
	        	  child1.add(child1Data1);
	              break;
	        case 2: 
	        	  Map<String, String> child2Data1 = new HashMap<String, String>();
	        	  child2Data1.put("foodname", c.getString(6));
	        	  child2Data1.put("checkboxstate", bbc);
	        	  child2Data1.put("Food_id", c.getString(1));
	        	  child2Data1.put("Price", c.getString(3));
	        	  child2Data1.put("Qty", c.getString(4));
	        	  child2.add(child2Data1);
	              break;
	        case 3: 
	        	  Map<String, String> child3Data1 = new HashMap<String, String>();
	        	  child3Data1.put("foodname", c.getString(6));
	        	  child3Data1.put("checkboxstate", bbc);
	        	  child3Data1.put("Food_id", c.getString(1));
	        	  child3Data1.put("Price", c.getString(3));
	        	  child3Data1.put("Qty", c.getString(4));
	        	  child3.add(child3Data1);
	              break;
	        case 4:  
	        	  Map<String, String> child4Data1 = new HashMap<String, String>();
	        	  child4Data1.put("foodname", c.getString(6));
	        	  child4Data1.put("checkboxstate", bbc);
	        	  child4Data1.put("Food_id", c.getString(1));
	        	  child4Data1.put("Price", c.getString(3));
	        	  child4Data1.put("Qty", c.getString(4));
	        	  child4.add(child4Data1);
	              break;
	        case 5:  
	        	  Map<String, String> child5Data1 = new HashMap<String, String>();
	        	  child5Data1.put("foodname", c.getString(6));
	        	  child5Data1.put("checkboxstate", bbc);
	        	  child5Data1.put("Food_id", c.getString(1));
	        	  child5Data1.put("Price", c.getString(3));
	        	  child5Data1.put("Qty", c.getString(4));
	        	  child5.add(child5Data1);
	              break;
	        case 6: 
	        	  Map<String, String> child6Data1 = new HashMap<String, String>();
	        	  child6Data1.put("foodname", c.getString(6));
	        	  child6Data1.put("checkboxstate", bbc);
	        	  child6Data1.put("Food_id", c.getString(1));
	        	  child6Data1.put("Price", c.getString(3));
	        	  child6Data1.put("Qty", c.getString(4));
	        	  child6.add(child6Data1);
	              break;
	        case 7: 
	      	  Map<String, String> child7Data1 = new HashMap<String, String>();
	      	  child7Data1.put("foodname", c.getString(6));
	      	child7Data1.put("checkboxstate", bbc);
	      	child7Data1.put("Food_id", c.getString(1));
	      	child7Data1.put("Price", c.getString(3));
	      	child7Data1.put("Qty", c.getString(4));
	      	  child7.add(child7Data1);
	            break;
	        case 8: 
	      	  Map<String, String> child8Data1 = new HashMap<String, String>();
	      	  child8Data1.put("foodname", c.getString(6));
	      	child8Data1.put("checkboxstate", bbc);
	      	child8Data1.put("Food_id", c.getString(1));
	      	child8Data1.put("Price", c.getString(3));
	      	child8Data1.put("Qty", c.getString(4));
	      	  child8.add(child8Data1);
	            break;
	    	}
			}
   
			c.close();
			cu.close();	
			childs.add(child1);
			childs.add(child2);
			childs.add(child3);
			childs.add(child4);
			childs.add(child5);
			childs.add(child6);
			childs.add(child7);
			childs.add(child8);

   	}

        mPullRefreshListView = (PullToRefreshExpandableListView) findViewById(R.id.ExpandableListViewall);

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

	            

	      		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

	      		if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED 
	      			    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {


	            	
		     		   t=new Thread() {
		     	           public void run() {
		     	          	 
		     	     		Looper.prepare();   		
		     	     		Itemchangestate();
		     	     			Message m = new Message();
		     	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
		     	     		m.what = MEG_INVALIDATE;
		     	     		handler.sendMessage(m);
		     	     		
		     	     		
		     	     	    Looper.loop();
		     	          	
		     	           }
		     	     };      
		     	     t.start();
		     	     t.setPriority(Thread.MAX_PRIORITY);
		     	     
	      			}else {
	      			
	      			Errorbox("裝置沒有連線,請確保有裝置連線到網上");
	      		  mPullRefreshListView.onRefreshComplete();

	      		}//end check internet
            }
        });

        
viewAdapter = new ExpandableChecklist(this, groups, childs, db, table_name, Table_id);
setListAdapter(viewAdapter);
	
	

    
	}//end setuplist

	
	
	
	@Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox1); 
        if( checkBox != null ){
        	checkBox.toggle();
        }    

  	
        
        pricecount();
        
        return false;
    }
    
	  
	@Override
	protected void onDestroy() {
	super.onDestroy();
	if (db != null)
	{
	db.close();
	}
	}
	
	
	
	
	
	
	
	
	
	protected void pricecount(){
				
      	// TOTAL PRICE
      	int totalprice=0;
      	
		Cursor	c = db.rawQuery("SELECT Price, Qty FROM "+table_name+" WHERE List_id2="+Table_id+" AND Checkstate ='1'", null);	

		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
		totalprice=totalprice+c.getInt(0)*c.getInt(1);		
		}
		c.close();
		
		TextView textViewttl = (TextView)findViewById(R.id.textViewttl);
		textViewttl.setText(Integer.toString(totalprice));
			
	}//end pricecount
	
	
	
	
	 // change state
	private void Itemchangestate() {
		// TODO Auto-generated method stub
    		
		if (db==null){
	         		 db = helper.getWritableDatabase();
	         		}
		Cursor c = db.rawQuery("SELECT Createtime FROM CheckList_List WHERE List_id='"+Table_id+"'", null);
		c.moveToFirst();
			
        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
        nameValuePairs.clear();
        nameValuePairs.add(new BasicNameValuePair("Int_id",  Integer.toString(Int_id)));
        nameValuePairs.add(new BasicNameValuePair("Updatetime",  c.getString(0)));
			c = db.rawQuery("SELECT Checkstate, Food_id FROM "+table_name+" WHERE List_id2="+Table_id+" AND Send ='1'", null);	
		int count=0;
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			nameValuePairs.add(new BasicNameValuePair("Checkstate["+count+"]", c.getString(0)));
			nameValuePairs.add(new BasicNameValuePair("Food_id["+count+"]", c.getString(1)));
			
			count++;
			}
		nameValuePairs.add(new BasicNameValuePair("Count", Integer.toString(count-1)));
		
       
        InputStream is = null;
 	   
 	   String result = "";
        

 	    //http post
	        try{
 	            HttpClient httpclient = new DefaultHttpClient();
 	            HttpPost httppost = new HttpPost("http://www.rapidtao.com/t/hwy/checklistphp/checkchange.php");
	 	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
 	            HttpResponse response = httpclient.execute(httppost);
 	            HttpEntity entity = response.getEntity();
 	            is = entity.getContent();
   
 	           
 	    }catch(Exception e){
 	    	removeDialog(0);
	    	Toast.makeText(this, "連線出現問題,請稍後再試...", Toast.LENGTH_LONG).show(); 	
 	         System.out.println(e);   
 	    }

 	    //convert response to string
 	    try{
 	    	removeDialog(0);
 	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF8"));
 	            StringBuilder sb = new StringBuilder();
 	            String line = null;
 	            while ((line = reader.readLine()) != null) {
 	                    sb.append(line + "\n");
 	            }
 	            is.close();
 	            result=sb.toString().trim();
 	           System.out.println(result);
 	           
 	            
 	    }catch(Exception e){
 	    	removeDialog(0);
 	    	  Toast.makeText(this,"連線錯誤,請稍後再試", Toast.LENGTH_LONG).show();
		 	  
 	    }

 	   String[] msgreturn=result.split("#xy");

    		ContentValues cv = new ContentValues();
			cv.put("Send", "0");
			
        	db.update(table_name, cv, "List_id2='"+Table_id+"'and Send='1'", null);
	 	   
        	if(msgreturn[0].equals("ok")){
        	
        		if (msgreturn[1].equals("n1")){
                	
        			Toast.makeText(this,"已經是最新資料", Toast.LENGTH_LONG).show();
	        
        		}else{
        			 ContentValues args = new ContentValues();
 	 	            args.put("Createtime", msgreturn[1]);
 	 	            db.update("CheckList_List", args, "List_id=" + Table_id, null);
 	 	          Toast.makeText(this,"更新成功", Toast.LENGTH_LONG).show();
        		}
			
        	 
        	}else if (msgreturn[0].equals("up")){
        	System.out.println( "name:"+msgreturn[1]+"date"+ msgreturn[3]+"table : "+Table_id);
   			
        	ContentValues args = new ContentValues();
	            args.put("List_Name", msgreturn[1]);
	            args.put("Createtime", msgreturn[3]);
	            db.update("CheckList_List", args, "List_id=" + Table_id, null);
    		     
   			c = db.rawQuery("SELECT Createtime FROM CheckList_List WHERE List_id='"+Table_id+"'", null);
   			c.moveToFirst();
   			System.out.println("newtime"+c.getInt(0));
    		 	   String[] sameup=msgreturn[2].split("#zy");
    		 	//display all data
    		 	   String msg="";
    		 	  System.out.println("length "+ sameup[0]); 
    		 	  
    		 	  
    		 	  try{
  			    	
  			    	// Load the requested page converted to a string into a JSONObject.
  			        JSONArray jArray = new JSONArray(msgreturn[4]);


  			    //    db.execSQL("INSERT INTO widgets (name, inventory)"+ "VALUES ('Sprocket', 5)"); 
  			        
  			        db.delete(table_name, "List_id2='"+Table_id+"'", null);
  		        	 cv = new ContentValues();

  			        for(int u=0;u<jArray.length();u++){
  			        	System.out.println(u);
  			        	JSONObject jsonResponse = jArray.getJSONObject(u);
  		            	cv.put("List_id2", Table_id);
  		            	cv.put("Food_id", jsonResponse.getInt("Food_id"));
  						cv.put("Checkstate", jsonResponse.getString("Checkstate"));
  						cv.put("Price", jsonResponse.getInt("Price"));
  						cv.put("Qty", jsonResponse.getInt("Qty"));
  						//添加方法
  						db.insert(table_name, "", cv);
  						
  			        };
//  			        System.out.println(Sqlexe);
  			        
  			  	    }catch(JSONException e){
  				        System.out.println(e.toString());
  				      Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
  	        		
  			    }
    		 	  
    		 	   if(sameup[0].equals("no")){
    		 		  Errorbox("資料已自動更新");
    		 	   }else{
	    		 	
    		 	   for(int i = 0;i<sameup.length;i++){
    		 		System.out.println(i+" length : "+sameup.length+"what "+msgreturn[2]);  
    		 	   c = db.rawQuery("Select Name from "+CheckList_Food+" WHERE Food_id='"+sameup[i]+"'", null);
    		 	  c.moveToFirst();
    		 	  msg=msg+","+c.getString(0);
    		 	  }
    		 	  Errorbox(msg+"已經被人修改或刪除了,資料已自動更新");
    		 	   }
    		 	  
    		 	  
    		 	  
 	   }else{
 		  Toast.makeText(this,"更新出現問題或是尚未發布上網", Toast.LENGTH_LONG).show(); 
 	   System.out.println(msgreturn);
 	   }

        	c.close();

	} //end change state items
	
	
	
	
	  //loading box
    @Override
    protected Dialog onCreateDialog(int id) {
          switch (id) {
                case 0: {
                      dialog = new ProgressDialog(this);
                      dialog.setMessage("更新中,請稍等...");
                      dialog.setIndeterminate(true);
                      dialog.setCancelable(true);
                      return dialog;
                }
          }
          return null;
    }
    
	//errorbox
	  private void Errorbox(String errormsg) {
	      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	      dialog.setTitle("注意");
	      dialog.setMessage(errormsg);
	      dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
	              public void onClick(DialogInterface dialoginterface, int i){

	            	   }
	              });
	      dialog.show();
	} //end errorbox
	  
	  
	  
	  
	  
	  
	    //handler msg
	    Handler handler = new Handler() 
	    {

	          public void handleMessage(Message msg) 
	    {
	     switch (msg.what) 
	  {
	  case MEG_INVALIDATE:
		  Setuplist(Table_id);
		  viewAdapter.notifyDataSetChanged();
		  mPullRefreshListView.onRefreshComplete();
		
    break;
            }
	      super.handleMessage(msg);
	          }

	      };//end handler msg
	      
	      
	      
	      
	      
	      //insert item box
	     private void newlistbox (){


	      	AlertDialog.Builder builder = new AlertDialog.Builder(this);

	      	builder.setTitle("新增清單");
	      	builder.setMessage("請輸入清單名稱");

	      	 LayoutInflater inflater = LayoutInflater.from(this);

	      	 View alertDialogView = inflater.inflate(R.layout.newlist, null);
	         
	      	builder.setView(alertDialogView); 
	          	 
	      	 final EditText editTextnewlist = (EditText) alertDialogView.findViewById(R.id.editTextnewlist); 
	      	 
	      	 

	           //spinner stype
	           Spinner spinnerlisttype = (Spinner) alertDialogView.findViewById(R.id.spinnerlisttypes);
	           //建立一個ArrayAdapter物件，並放置下拉選單的內容
	           ArrayAdapter<String> adapterlisttype = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new String[]{"請選擇種類","BBQ","打邊爐","旅行"});
	           

	           //設定下拉選單的樣式
	           adapterlisttype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	          // adapter.add("some string");
	           spinnerlisttype.setAdapter(adapterlisttype);
	           
	           
	           //設定項目被選取之後的動作
	           spinnerlisttype.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
	               public void onItemSelected(AdapterView adapterView, View view, int position, long id){
	              	 
	              	 if(position!=0){
	                   //Toast.makeText(getApplicationContext(), "你所選擇的是"+adapterView.getSelectedItem().toString()+"id no. "+position, Toast.LENGTH_LONG).show();
	                   gettypeno=position;
	              	 }
	                   
	               }
	               public void onNothingSelected(AdapterView arg0) {
	                   //Toast.makeText(getApplicationContext(), "請選擇種類", Toast.LENGTH_LONG).show();
	               } 
	           });
	      	 
	      	 
	      	 
	      	 
	         builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
	        	  public void onClick(DialogInterface dialog, int whichButton) {

	        	  }
	        	});//onclick dialog
	      	
	      	
	         builder.setNegativeButton("完成", new DialogInterface.OnClickListener() {
	      	  public void onClick(DialogInterface dialog, int whichButton) {

	      		  
	      		   if (!editTextnewlist.getText().toString().trim().equals("")&& gettypeno!=0){
	      		         
	      			   String temNewlistname = editTextnewlist.getText().toString().trim();
	      			   
	      			    Intent i = getParent().getIntent();
	      			    i.putExtra("newListname", temNewlistname);
	      			    i.putExtra("gettypeno", Integer.toString(gettypeno));
	      			    i.removeExtra("tableid");
	      			    TabActivity ta = (TabActivity) Checkliststate.this.getParent();
	      			    ta.getTabHost().setCurrentTab(4);
	      			    
	      			    
	      	    }else{
	      	    	Errorbox("你沒有輸入名稱或選擇類別");
	      	    }
	     
	      	  }
	      	});

	        AlertDialog alert = builder.create();
	      	alert.show();
	      	

	      }//end insert items box
	      
	     
	     
	     
	}