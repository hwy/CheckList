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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hwy.checklist.library.PullToRefreshBase.OnRefreshListener;
import com.hwy.checklist.library.PullToRefreshExpandableListView;


public class Createnewlist extends ExpandableListActivity  {

	
	//http://www.cnblogs.com/noTice520/archive/2012/01/20/2328035.html 下拉更新
	 public String Loadfood;
		//用一个list对象保存所有的二级列表数据
		List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();

			 //SQL
		//SQLiteDatabase对象
		SQLiteDatabase db;
		//数据库名
		public String db_name = "CheckList.db";
		//表名
		public String table_listname;
		public String table_foodtypename;
		public String newListname;
		public String gettypeno;
		public String tableid;
		
		//辅助类名
		final DbHelper helper = new DbHelper(this, db_name, null, 2);
	 //end sql
	 
	//loading hp	
	 ProgressDialog dialog;
	 Thread t;
	 private String loadfood;
	 public static final int MEG_TAGCHANGE = 3330; //自訂一個號碼
	 public static final int MEG_INVALIDATE = 1110; //自訂一個號碼
	 
	 //add item to hp
	 String getitemno=null;
	 Thread t2;
	 
	 //get value
	 ExpandableAdapter viewAdapter;
	 private PullToRefreshExpandableListView mPullRefreshListView;
	 String updatetime;
	 
	 //child list     
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
   	
   	public String[] typeoffood;
	 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.createnewlist);

// db.execSQL("select last_insert_rowid()5"); 直接upload名上網
	
	 Intent i = getParent().getIntent();
	 gettypeno = i.getStringExtra("gettypeno");
	   newListname = i.getStringExtra("newListname");
	   tableid = i.getStringExtra("tableid");
	   System.out.println(gettypeno+newListname+"tableid"+tableid);


	//check bbq or hotpot type
		  if(gettypeno.equals("1")){
				table_foodtypename = "CheckList_Bbq_Food";
				table_listname = "CheckList_BbqListitems";
				Loadfood = "http://www.rapidtao.com/t/hwy/checklistphp/loadfoodbbq.php";
				typeoffood =new String[]{"請選擇種類","蔬菜類","丸類","肉類","海鮮類","其他","工具"};
		  }else if(gettypeno.equals("2")){
					table_foodtypename = "CheckList_Hot_Food";
					table_listname = "CheckList_HotListitems";
					Loadfood = "http://www.rapidtao.com/t/hwy/checklistphp/loadfoodhotpot.php";
					typeoffood =new String[]{"請選擇種類","湯底", "肉類", "海鮮類", "丸類", "其他", "鮮菌類", "時蔬類", "粉麵類","醬料","飲品"};
				}else if(gettypeno.equals("3")){
					table_foodtypename = "CheckList_Travel";
					table_listname = "CheckList_TravelListitems";
					Loadfood = "http://www.rapidtao.com/t/hwy/checklistphp/loadtravel.php";
					typeoffood =new String[]{"請選擇種類","證明文件", "金錢", "衣物", "個人護理", "小型電器", "個人護理–女性", "其他", "藥品"};
				}//end check
		  
		 
		  

		  
		  System.out.println(table_foodtypename+table_listname);
		  
		  //check create table list or not

		 if(tableid==null){

	      		if (db==null){
	         		 db = helper.getWritableDatabase();
	         		}
				ContentValues cv = new ContentValues();
				
				
				db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'CheckList_List'");	 

				//cv.put("List_id", "1");
            	cv.put("List_Name", newListname);
            	cv.put("List_Type", gettypeno);
				//添加方法
				tableid=Long.toString(db.insert("CheckList_List", "", cv));
		 
		 }

	   
		    TextView textView = (TextView)findViewById(R.id.textViewnameid);    
		    textView.setText("名: "+newListname);

      
  		Setupchildlist();
		displaylistview();
		
		
		
	// add food	
     Button buttonAdd = (Button) findViewById(R.id.Buttonadd);
     buttonAdd.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
        	   insertbox ();
           }
       });

     // Rename
     Button buttonRename = (Button) findViewById(R.id.Buttonrename);
     buttonRename.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
        	   renamebox ();
           }
       });

    	
    	//save and upload
    	 Button buttonSave = (Button) findViewById(R.id.Buttonsave);
    	 buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            	 showDialog(0);
            	  	
          		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

          		if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED 
          			    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {

          		  
    	      		 	   t2=new Thread() {
    	                       public void run() {
    	             
    	                 		 Looper.prepare();
    	                 		
    	                 		updatelist();
    	                     	 Looper.loop();
    	                      	
    	                       }

    	                 };
    	           t2.start();
          		}else {
	      			
	      			Errorbox("裝置沒有連線,請確保有裝置連線到網上");
	      			}
          		         		

            } 
        });
     	
	}//end create

	private void displaylistview(){
	//	ExpandableListView elv = (ExpandableListView)findViewById(R.id.ExpandableListViewall);
			
		List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
		
		  if(gettypeno.equals("1")){

				
				//准备一级列表中显示的数据:2个一级列表,分别显示"group1"和"group2"
				
				Map<String, String> group1 = new HashMap<String, String>();
				group1.put("group", "蔬菜類");
				Map<String, String> group2 = new HashMap<String, String>();
				group2.put("group", "丸類");
				Map<String, String> group3 = new HashMap<String, String>();
				group3.put("group", "肉類");
				Map<String, String> group4 = new HashMap<String, String>();
				group4.put("group", "海鮮類");
				Map<String, String> group5 = new HashMap<String, String>();
				group5.put("group", "其他");
				Map<String, String> group6 = new HashMap<String, String>();
				group6.put("group", "工具");
				
				groups.add(group1);
				groups.add(group2);
				groups.add(group3);
				groups.add(group4);
				groups.add(group5);
				groups.add(group6);
				
				  }else if(gettypeno.equals("2")){
			
				
				//准备一级列表中显示的数据:2个一级列表,分别显示"group1"和"group2"
				Map<String, String> group1 = new HashMap<String, String>();
				group1.put("group", "湯底");
				Map<String, String> group2 = new HashMap<String, String>();
				group2.put("group", "肉類");
				Map<String, String> group3 = new HashMap<String, String>();
				group3.put("group", "海鮮類");
				Map<String, String> group4 = new HashMap<String, String>();
				group4.put("group", "丸類");
				Map<String, String> group5 = new HashMap<String, String>();
				group5.put("group", "其他");
				Map<String, String> group6 = new HashMap<String, String>();
				group6.put("group", "鮮菌類");
				Map<String, String> group7 = new HashMap<String, String>();
				group7.put("group", "時蔬類");
				Map<String, String> group8 = new HashMap<String, String>();
				group8.put("group", "粉麵類");
				Map<String, String> group9 = new HashMap<String, String>();
				group9.put("group", "醬料");
				Map<String, String> group10 = new HashMap<String, String>();
				group10.put("group", "飲品");

				
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
				
				
				  }else if(gettypeno.equals("3")){
						
						//准备一级列表中显示的数据:2个一级列表,分别显示"group1"和"group2"
						Map<String, String> group1 = new HashMap<String, String>();
						group1.put("group", "證明文件");
						Map<String, String> group2 = new HashMap<String, String>();
						group2.put("group", "金錢");
						Map<String, String> group3 = new HashMap<String, String>();
						group3.put("group", "衣物");
						Map<String, String> group4 = new HashMap<String, String>();
						group4.put("group", "個人護理");
						Map<String, String> group5 = new HashMap<String, String>();
						group5.put("group", "小型電器");
						Map<String, String> group6 = new HashMap<String, String>();
						group6.put("group", "個人護理–女性");
						Map<String, String> group7 = new HashMap<String, String>();
						group7.put("group", "其他");
						Map<String, String> group8 = new HashMap<String, String>();
						group8.put("group", "藥品");
											
						groups.add(group1);
						groups.add(group2);
						groups.add(group3);
						groups.add(group4);
						groups.add(group5);
						groups.add(group6);
						groups.add(group7);
						groups.add(group8);						
						
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
			     	     		loadfood=loadFoodlist();
			     	     		if(loadfood!=null){
			     	     			laodtosql(loadfood);
			     	     			Message m = new Message();
			     	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
			     	     		m.what = MEG_INVALIDATE;
			     	     		handler.sendMessage(m);
			     	     		
			     	     		}
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

		  
		  
		viewAdapter = new ExpandableAdapter(this, groups, childs, db, table_listname, tableid);
		setListAdapter(viewAdapter);
		//mPullRefreshListView.setAdapter(viewAdapter);
		//mPullRefreshListView.setVisibility(View.VISIBLE);
		
		
		
	}
	
	
	//Setup expandable list view
	private void Setupchildlist(){
		

		//sql
		//从辅助类获得数据库对象
  		if (db==null){
     		 db = helper.getWritableDatabase();
     		}
	   		   	
		Cursor clist = db.query(table_listname, null,"List_id2='"+tableid+"'", null, null, null, null);
		Cursor cfood = db.query(table_foodtypename, null, null, null, null, null, "Vote DESC");
	  System.out.println(gettypeno+"setupchild");
		if (gettypeno.equals("1")){	
			
			child1.clear();
		   	child2.clear();
		   	child3.clear();
		   	child4.clear();
		   	child5.clear();
		   	child6.clear();
		   	
        for(cfood.moveToFirst();!cfood.isAfterLast();cfood.moveToNext()){
        	//System.out.println(cfood.getString(2));
		String checkboxstate ="false";
    	String Price ="0";
    	String Qty ="1";
		for(clist.moveToFirst();!clist.isAfterLast();clist.moveToNext()){

		 	if(cfood.getInt(0)==clist.getInt(1)){
    	 		checkboxstate="true";
	        	Price =clist.getString(3);
	        	Qty =clist.getString(4);
    	 	}	
		};
    	switch (cfood.getInt(1)) {
        case 1:       	            	
        	  Map<String, String> child1Data1 = new HashMap<String, String>();
        	  child1Data1.put("foodname", cfood.getString(2));
        	  child1Data1.put("checkboxstate", checkboxstate);
        	  child1Data1.put("Food_id", cfood.getString(0));
        	  child1Data1.put("Price", Price);
        	  child1Data1.put("Qty", Qty);
        	  child1.add(child1Data1);
              break;
        case 2: 
        	  Map<String, String> child2Data1 = new HashMap<String, String>();
        	  child2Data1.put("foodname", cfood.getString(2));
        	  child2Data1.put("checkboxstate", checkboxstate);
        	  child2Data1.put("Food_id", cfood.getString(0));
        	  child2Data1.put("Price", Price);
        	  child2Data1.put("Qty", Qty);
        	  child2.add(child2Data1);
              break;
        case 3: 
        	  Map<String, String> child3Data1 = new HashMap<String, String>();
        	  child3Data1.put("foodname", cfood.getString(2));
        	  child3Data1.put("checkboxstate", checkboxstate);
        	  child3Data1.put("Food_id", cfood.getString(0));
        	  child3Data1.put("Price", Price);
        	  child3Data1.put("Qty", Qty);
        	  child3.add(child3Data1);
              break;
        case 4:  
        	  Map<String, String> child4Data1 = new HashMap<String, String>();
        	  child4Data1.put("foodname", cfood.getString(2));
        	  child4Data1.put("checkboxstate", checkboxstate);
        	  child4Data1.put("Food_id", cfood.getString(0));
        	  child4Data1.put("Price", Price);
        	  child4Data1.put("Qty", Qty);
        	  child4.add(child4Data1);
              break;
        case 5:  
        	  Map<String, String> child5Data1 = new HashMap<String, String>();
        	  child5Data1.put("foodname", cfood.getString(2));
        	  child5Data1.put("checkboxstate", checkboxstate);
        	  child5Data1.put("Food_id", cfood.getString(0));
        	  child5Data1.put("Price", Price);
        	  child5Data1.put("Qty", Qty);
        	  child5.add(child5Data1);
              break;
        case 6: 
        	  Map<String, String> child6Data1 = new HashMap<String, String>();
        	  child6Data1.put("foodname", cfood.getString(2));
        	  child6Data1.put("checkboxstate", checkboxstate);
        	  child6Data1.put("Food_id", cfood.getString(0));
        	  child6Data1.put("Price", Price);
        	  child6Data1.put("Qty", Qty);
        	  child6.add(child6Data1);
              break;
    	}
    	

    };
    

	childs.add(child1);
	childs.add(child2);
	childs.add(child3);
	childs.add(child4);
	childs.add(child5);
	childs.add(child6);
		} else if (gettypeno.equals("2")){
			
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
		   	
			  for(cfood.moveToFirst();!cfood.isAfterLast();cfood.moveToNext()){
		        	//System.out.println(cfood.getString(2));
				String checkboxstate ="false";
		    	String Price ="0";
		    	String Qty ="1";
				for(clist.moveToFirst();!clist.isAfterLast();clist.moveToNext()){

				 	if(cfood.getInt(0)==clist.getInt(1)){
		    	 		checkboxstate="true";
			        	Price =clist.getString(3);
			        	Qty =clist.getString(4);
		    	 	}	
				};
		    	switch (cfood.getInt(1)) {
		        case 1:       	            	
		        	  Map<String, String> child1Data1 = new HashMap<String, String>();
		        	  child1Data1.put("foodname", cfood.getString(2));
		        	  child1Data1.put("checkboxstate", checkboxstate);
		        	  child1Data1.put("Food_id", cfood.getString(0));
		        	  child1Data1.put("Price", Price);
		        	  child1Data1.put("Qty", Qty);
		        	  child1.add(child1Data1);
		              break;
		        case 2: 
		        	  Map<String, String> child2Data1 = new HashMap<String, String>();
		        	  child2Data1.put("foodname", cfood.getString(2));
		        	  child2Data1.put("checkboxstate", checkboxstate);
		        	  child2Data1.put("Food_id", cfood.getString(0));
		        	  child2Data1.put("Price", Price);
		        	  child2Data1.put("Qty", Qty);
		        	  child2.add(child2Data1);
		              break;
		        case 3: 
		        	  Map<String, String> child3Data1 = new HashMap<String, String>();
		        	  child3Data1.put("foodname", cfood.getString(2));
		        	  child3Data1.put("checkboxstate", checkboxstate);
		        	  child3Data1.put("Food_id", cfood.getString(0));
		        	  child3Data1.put("Price", Price);
		        	  child3Data1.put("Qty", Qty);
		        	  child3.add(child3Data1);
		              break;
		        case 4:  
		        	  Map<String, String> child4Data1 = new HashMap<String, String>();
		        	  child4Data1.put("foodname", cfood.getString(2));
		        	  child4Data1.put("checkboxstate", checkboxstate);
		        	  child4Data1.put("Food_id", cfood.getString(0));
		        	  child4Data1.put("Price", Price);
		        	  child4Data1.put("Qty", Qty);
		        	  child4.add(child4Data1);
		              break;
		        case 5:  
		        	  Map<String, String> child5Data1 = new HashMap<String, String>();
		        	  child5Data1.put("foodname", cfood.getString(2));
		        	  child5Data1.put("checkboxstate", checkboxstate);
		        	  child5Data1.put("Food_id", cfood.getString(0));
		        	  child5Data1.put("Price", Price);
		        	  child5Data1.put("Qty", Qty);
		        	  child5.add(child5Data1);
		              break;
		        case 6: 
		        	  Map<String, String> child6Data1 = new HashMap<String, String>();
		        	  child6Data1.put("foodname", cfood.getString(2));
		        	  child6Data1.put("checkboxstate", checkboxstate);
		        	  child6Data1.put("Food_id", cfood.getString(0));
		        	  child6Data1.put("Price", Price);
		        	  child6Data1.put("Qty", Qty);
		        	  child6.add(child6Data1);
		              break;
		        case 7: 
		        	  Map<String, String> child7Data1 = new HashMap<String, String>();
		        	  child7Data1.put("foodname", cfood.getString(2));
		        	  child7Data1.put("checkboxstate", checkboxstate);
		        	  child7Data1.put("Food_id", cfood.getString(0));
		        	  child7Data1.put("Price", Price);
		        	  child7Data1.put("Qty", Qty);
		        	  child7.add(child7Data1);
		              break;
		        case 8: 
		        	  Map<String, String> child8Data1 = new HashMap<String, String>();
		        	  child8Data1.put("foodname", cfood.getString(2));
		        	  child8Data1.put("checkboxstate", checkboxstate);
		        	  child8Data1.put("Food_id", cfood.getString(0));
		        	  child8Data1.put("Price", Price);
		        	  child8Data1.put("Qty", Qty);
		        	  child8.add(child8Data1);
		              break;
		        case 9: 
		        	  Map<String, String> child9Data1 = new HashMap<String, String>();
		        	  child9Data1.put("foodname", cfood.getString(2));
		        	  child9Data1.put("checkboxstate", checkboxstate);
		        	  child9Data1.put("Food_id", cfood.getString(0));
		        	  child9Data1.put("Price", Price);
		        	  child9Data1.put("Qty", Qty);
		        	  child9.add(child9Data1);
		              break;
		        case 10: 
		        	  Map<String, String> child10Data1 = new HashMap<String, String>();
		        	  child10Data1.put("foodname", cfood.getString(2));
		        	  child10Data1.put("checkboxstate", checkboxstate);
		        	  child10Data1.put("Food_id", cfood.getString(0));
		        	  child10Data1.put("Price", Price);
		        	  child10Data1.put("Qty", Qty);
		        	  child10.add(child10Data1);
		              break;
		    	}
		    	

		    };


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
		
		}else{
			
			child1.clear();
		   	child2.clear();
		   	child3.clear();
		   	child4.clear();
		   	child5.clear();
		   	child6.clear();
		   	child7.clear();
		   	child8.clear();
		   	
			  for(cfood.moveToFirst();!cfood.isAfterLast();cfood.moveToNext()){
		        	//System.out.println(cfood.getString(2));
				String checkboxstate ="false";
		    	String Price ="0";
		    	String Qty ="1";
				for(clist.moveToFirst();!clist.isAfterLast();clist.moveToNext()){

				 	if(cfood.getInt(0)==clist.getInt(1)){
		    	 		checkboxstate="true";
			        	Price =clist.getString(3);
			        	Qty =clist.getString(4);
		    	 	}	
				};
		    	switch (cfood.getInt(1)) {
		        case 1:       	            	
		        	  Map<String, String> child1Data1 = new HashMap<String, String>();
		        	  child1Data1.put("foodname", cfood.getString(2));
		        	  child1Data1.put("checkboxstate", checkboxstate);
		        	  child1Data1.put("Food_id", cfood.getString(0));
		        	  child1Data1.put("Price", Price);
		        	  child1Data1.put("Qty", Qty);
		        	  child1.add(child1Data1);
		              break;
		        case 2: 
		        	  Map<String, String> child2Data1 = new HashMap<String, String>();
		        	  child2Data1.put("foodname", cfood.getString(2));
		        	  child2Data1.put("checkboxstate", checkboxstate);
		        	  child2Data1.put("Food_id", cfood.getString(0));
		        	  child2Data1.put("Price", Price);
		        	  child2Data1.put("Qty", Qty);
		        	  child2.add(child2Data1);
		              break;
		        case 3: 
		        	  Map<String, String> child3Data1 = new HashMap<String, String>();
		        	  child3Data1.put("foodname", cfood.getString(2));
		        	  child3Data1.put("checkboxstate", checkboxstate);
		        	  child3Data1.put("Food_id", cfood.getString(0));
		        	  child3Data1.put("Price", Price);
		        	  child3Data1.put("Qty", Qty);
		        	  child3.add(child3Data1);
		              break;
		        case 4:  
		        	  Map<String, String> child4Data1 = new HashMap<String, String>();
		        	  child4Data1.put("foodname", cfood.getString(2));
		        	  child4Data1.put("checkboxstate", checkboxstate);
		        	  child4Data1.put("Food_id", cfood.getString(0));
		        	  child4Data1.put("Price", Price);
		        	  child4Data1.put("Qty", Qty);
		        	  child4.add(child4Data1);
		              break;
		        case 5:  
		        	  Map<String, String> child5Data1 = new HashMap<String, String>();
		        	  child5Data1.put("foodname", cfood.getString(2));
		        	  child5Data1.put("checkboxstate", checkboxstate);
		        	  child5Data1.put("Food_id", cfood.getString(0));
		        	  child5Data1.put("Price", Price);
		        	  child5Data1.put("Qty", Qty);
		        	  child5.add(child5Data1);
		              break;
		        case 6: 
		        	  Map<String, String> child6Data1 = new HashMap<String, String>();
		        	  child6Data1.put("foodname", cfood.getString(2));
		        	  child6Data1.put("checkboxstate", checkboxstate);
		        	  child6Data1.put("Food_id", cfood.getString(0));
		        	  child6Data1.put("Price", Price);
		        	  child6Data1.put("Qty", Qty);
		        	  child6.add(child6Data1);
		              break;
		        case 7: 
		        	  Map<String, String> child7Data1 = new HashMap<String, String>();
		        	  child7Data1.put("foodname", cfood.getString(2));
		        	  child7Data1.put("checkboxstate", checkboxstate);
		        	  child7Data1.put("Food_id", cfood.getString(0));
		        	  child7Data1.put("Price", Price);
		        	  child7Data1.put("Qty", Qty);
		        	  child7.add(child7Data1);
		              break;
		        case 8: 
		        	  Map<String, String> child8Data1 = new HashMap<String, String>();
		        	  child8Data1.put("foodname", cfood.getString(2));
		        	  child8Data1.put("checkboxstate", checkboxstate);
		        	  child8Data1.put("Food_id", cfood.getString(0));
		        	  child8Data1.put("Price", Price);
		        	  child8Data1.put("Qty", Qty);
		        	  child8.add(child8Data1);
		              break;

		    	}
		    	

		    };


			childs.add(child1);
			childs.add(child2);
			childs.add(child3);
			childs.add(child4);
			childs.add(child5);
			childs.add(child6);
			childs.add(child7);
			childs.add(child8);

		
		}
		
		
	    clist.close();
	    cfood.close();
	} //end setup function
	
	
	
	//load food function
	private String loadFoodlist() {

		InputStream is = null;

		String result = "";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Loadfood);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			removeDialog(0);
			Errorbox("連線出現問題,請稍後再試");
		}

		// convert response to string
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		
		} catch (Exception e) {
			removeDialog(0);
			Errorbox("Data Error, Please try later");
		}
		
		
		return result;
		  
	}//load food function
	
	
	
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
	  

	  //loading box
	    @Override
	    protected Dialog onCreateDialog(int id) {
	          switch (id) {
	                case 0: {
	                      dialog = new ProgressDialog(this);
	                      dialog.setMessage("連線中,請稍等...");
	                      dialog.setIndeterminate(true);
	                      dialog.setCancelable(true);
	                      return dialog;
	                }
	          }
	          return null;
	    }

	    
	    
	    
	    //handler msg
	    Handler handler = new Handler() 
	    {

	          public void handleMessage(Message msg) 
	    {
	     switch (msg.what) 
	  {
	  case MEG_INVALIDATE:
		  Setupchildlist();
		  mPullRefreshListView.onRefreshComplete();
	      break;
	  case MEG_TAGCHANGE:
		 Setupchildlist(); 
		  viewAdapter.notifyDataSetChanged();
		
      break;
              }
	      super.handleMessage(msg);
	          }

	      };//end handler msg
	      
	      
	      

	      //insert item box
	      private void insertbox (){

	    	  
	      	AlertDialog.Builder builder = new AlertDialog.Builder(this);

	      	builder.setTitle("新增物件");
	      	builder.setMessage("請選擇種類及輸入物件名稱");

	      	 LayoutInflater inflater = LayoutInflater.from(this);

	      	 View alertDialogView = inflater.inflate(R.layout.insertitem, null);
	         
	      	builder.setView(alertDialogView); 
	          	 
	      	 final EditText itemname = (EditText) alertDialogView.findViewById(R.id.editTextfoodname); 
	      	 	      	 
	      	 
	         //spinner stype
	         Spinner spinnertype = (Spinner) alertDialogView.findViewById(R.id.spinnertypes);
	         //建立一個ArrayAdapter物件，並放置下拉選單的內容
	         
	         
	         ArrayAdapter<String> adaptertype = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, typeoffood);
	         

	         //設定下拉選單的樣式
	         adaptertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        // adapter.add("some string");
	         spinnertype.setAdapter(adaptertype);
	         
	         
	         //設定項目被選取之後的動作
	         spinnertype.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
	             public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
	            	 
	            	 if(position!=0){
	               //  Toast.makeText(getApplicationContext(), "你所選擇的是"+adapterView.getSelectedItem().toString()+"id no. "+position, Toast.LENGTH_LONG).show();
	                 getitemno=String.valueOf(position);
	            	 }
	             }
	             public void onNothingSelected(AdapterView<?> arg0) {
	                 Toast.makeText(getApplicationContext(), "請選擇種類", Toast.LENGTH_LONG).show();
	             } 
	         });
	      	 
	         

	      	 
	         builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
	        	  public void onClick(DialogInterface dialog, int whichButton) {

	        	  }
	        	});//onclick dialog
	      	
	      	
	         builder.setNegativeButton("完成", new DialogInterface.OnClickListener() {
	      	  public void onClick(DialogInterface dialog, int whichButton) {
/*
	      	    Intent i = getParent().getIntent();
	  	        if(gettypeno.equals("1")){
	    	    i.putExtra("Bbq_id", tableid); 	
	    	    i.putExtra("gettypeno", "1");
	    	    }else if (gettypeno.equals("2")){
	    	   	i.putExtra("Hotpot_id", tableid);
	    	   	i.putExtra("gettypeno", "2");
	    	    } else {
	    	  	  Toast.makeText(Createnewlist.this,"系統出現問題", Toast.LENGTH_LONG).show();
}//put recodes
	*/
	      		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

	      		if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED 
	      			    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {

	      		  
		      		   if (!itemname.getText().toString().trim().equals("") && getitemno!=null){
		      			 showDialog(0);
		      			   t2=new Thread() {
		                       public void run() {
		             
		                 		 Looper.prepare();
		                 		
		                      	 String getitemname = itemname.getText().toString().trim();
		                 		 insertItem(getitemname,getitemno);
		                 		 Looper.loop();
		                      	
		                       }

		                 };
		           t2.start();
		           
		      	    }else{
		      	    	Errorbox("你沒有輸入名稱或者選擇種類");
		      	    }
	      			}

	      		else {
	      			
	      			Errorbox("裝置沒有連線,請確保有裝置連線到網上");
	      		  mPullRefreshListView.onRefreshComplete();

	      		}//end check internet 
	      		

	 
	     
	      	  }
	      	});

	        AlertDialog alert = builder.create();
	      	alert.show();
	      	

	      }//end insert items box
	      

	      
	      //insert item to internet database
			private void insertItem(String getitemname, String getitemtype) {
				
		        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		        nameValuePairs.add(new BasicNameValuePair("Name", getitemname));
		        nameValuePairs.add(new BasicNameValuePair("Types_id", getitemtype));
		        nameValuePairs.add(new BasicNameValuePair("Foodlist_id", gettypeno));
				
		        InputStream is = null;
		 	   
		 	   String result = "";

		 	    //http post
		 	    try{
		 	            HttpClient httpclient = new DefaultHttpClient();
		 	            HttpPost httppost = new HttpPost("http://www.rapidtao.com/t/hwy/checklistphp/addfood.php");
		 	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		 	            HttpResponse response = httpclient.execute(httppost);
		 	            HttpEntity entity = response.getEntity();
		 	            is = entity.getContent();
		   
		 	    }catch(Exception e){
		 	    	removeDialog(0);
		 	    	Errorbox("連線出現問題,請稍後再試..."); 	
		 	            
		 	    }

		 	    //convert response to string
		 	    try{
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
		 	    	  Toast.makeText(this,"加入錯誤,請稍後再試", Toast.LENGTH_LONG).show();
				 	  
		 	    }
		 	    System.out.println(gettypeno+" a");
		 	   String[] msgreturn=result.split("#xy");
		       if(msgreturn.length>=1&&msgreturn[0].equals("sucess")){


		    	   laodtosql(msgreturn[1]);
		 	  
   			Message m = new Message();
     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
     		m.what = MEG_TAGCHANGE;
     		handler.sendMessage(m);
     				 	   
		 	   }else if(msgreturn.length>1&&msgreturn[0].equals("Added Error")){
		 	  Errorbox("系統出現問題,請稍後再試");

		 	   }else if(msgreturn.length>1){
		 		   
		 //		   System.out.println(result); 
		 	  laodtosql(msgreturn[1]);		 		   
		 	  Errorbox("你所加的物件已經加了在"+msgreturn[0]);
		 	   }
		 	   

			 	  Toast.makeText(this,"加入成功,自動更新完成", Toast.LENGTH_LONG).show();

			} //end insert items
			
			
			

		      //insert item to internet database
				private void updatelist() {
					
			        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		
			        
			        
			    	Cursor c = db.rawQuery("SELECT CheckList_List.* From CheckList_List where List_id="+tableid, null);	 
					c.moveToFirst();
					
		        	  nameValuePairs.add(new BasicNameValuePair("List_id", c.getString(1)));  
		        	  nameValuePairs.add(new BasicNameValuePair("List_Name", c.getString(2)));
		        	  nameValuePairs.add(new BasicNameValuePair("List_Type", c.getString(3)));
		        	  nameValuePairs.add(new BasicNameValuePair("Pw", c.getString(4)));
		        	  nameValuePairs.add(new BasicNameValuePair("Createtime", c.getString(5)));
		        	  
			    	//display all data
					 c = db.query(table_listname, null,  "List_id2='"+tableid+"'", null, null, null, null);
					
                    int count=0;
                   for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                	 //  System.out.println("id "+c.getString(0)+"FOOD ID "+c.getString(1)+"CHECK "+c.getString(2)+"PRICE "+c.getString(3)+"QTY "+c.getString(4));
 		        	  nameValuePairs.add(new BasicNameValuePair("Food_id["+count+"]", c.getString(1)));
		        	  nameValuePairs.add(new BasicNameValuePair("Checkstate["+count+"]", c.getString(2)));
		        	  nameValuePairs.add(new BasicNameValuePair("Price["+count+"]", c.getString(3)));
		        	  nameValuePairs.add(new BasicNameValuePair("Qty["+count+"]", c.getString(4)));
		        	  count++;
                   }
         
		        	  nameValuePairs.add(new BasicNameValuePair("Count",  String.valueOf(count-1)));

			        
			        InputStream is = null;
			 	   
			 	   String result = "";

			 	    //http post
			 	    try{
			 	            HttpClient httpclient = new DefaultHttpClient();
			 	            HttpPost httppost = new HttpPost("http://www.rapidtao.com/t/hwy/checklistphp/createnewlist.php");
				 	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			 	            HttpResponse response = httpclient.execute(httppost);
			 	            HttpEntity entity = response.getEntity();
			 	            is = entity.getContent();
			   
			 	           
			 	    }catch(Exception e){
			 	    	removeDialog(0);
			 	    	Errorbox("離線儲存完成,但連線出現問題,不能共享,請稍後再試!"); 	
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
			 	           
			 	          String[] msgreturn=result.split("#xy");
					
			 	          if(msgreturn.length==4&&msgreturn[0].equals("sucess")){
				 	   
			 	        	  
			 	        	 ContentValues args = new ContentValues();
			 	            args.put("Pw", msgreturn[2]);
			 	            args.put("Int_id", msgreturn[1]);
			 	            args.put("Createtime", msgreturn[3]);
			 	            db.update("CheckList_List", args, "List_id=" + tableid, null);
			 	            
				 	   Errorbox("加入成功ID:"+msgreturn[1]+"密碼:"+msgreturn[2]);
						

			 	          }else if (msgreturn.length==2&&msgreturn[0].equals("update")) {
			 	        	  
				 	        	 ContentValues args = new ContentValues();
					 	            args.put("Createtime", msgreturn[1]);
					 	            db.update("CheckList_List", args, "List_id=" + tableid, null);
					 	            
					 	           Errorbox("更新成功"+msgreturn[0]+"更新板本:"+msgreturn[1]);
									  
			 	          }else {
				 	   Errorbox("系統出現問題");
				 	   }
			 	           
			 	    }catch(Exception e){
			 	    	removeDialog(0);
			 	    	  Toast.makeText(this,"加入錯誤,請稍後再試", Toast.LENGTH_LONG).show();
				    
			 	    }
			 	    
				 	 
			 	   c.close();
			 	   
				}//end update
	    
				
				
				//add to local sqlite
		private void laodtosql (String result){
			
			  try{
			    	
			    	// Load the requested page converted to a string into a JSONObject.
			        JSONArray jArray = new JSONArray(result);
		      		if (db==null){
		         		 db = helper.getWritableDatabase();
		         		}

			    //    db.execSQL("INSERT INTO widgets (name, inventory)"+ "VALUES ('Sprocket', 5)"); 
			        
			        db.delete(table_foodtypename, null, null);
		        	ContentValues cv = new ContentValues();

			        for(int count=0;count<jArray.length();count++){
			        	JSONObject jsonResponse = jArray.getJSONObject(count);
			        	//Sqlexe+="INSERT INTO "+table_foodtypename+" (Food_id, Types_id, Name, Vote, Report) VALUES ("+jsonResponse.getInt("Food_id")+","+jsonResponse.getInt("Types_id")+","+jsonResponse.getString("Name")+","+jsonResponse.getInt("Vote")+","+jsonResponse.getInt("Report")+");";
		            	cv.put("Food_id", jsonResponse.getInt("Food_id"));
		            	cv.put("Types_id", jsonResponse.getInt("Types_id"));
						cv.put("Name", jsonResponse.getString("Name"));
						cv.put("Vote", jsonResponse.getInt("Vote"));
						cv.put("Report", jsonResponse.getInt("Report"));
						//添加方法
						db.insert(table_foodtypename, "", cv);
			        };
//			        System.out.println(Sqlexe);
			        
			  	    }catch(JSONException e){
				        System.out.println(e.toString());
			    }
		
		 	    	removeDialog(0);

		}//end load sqlite
		
		
		
		
		   //rename box
	    private void renamebox (){

	    //TODO Auto-generated method stub

	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    	builder.setTitle("重新命名");
	    	builder.setMessage("請輸入名稱");

	    	 LayoutInflater inflater = LayoutInflater.from(this);

	    	 View alertDialogView = inflater.inflate(R.layout.renamebox, null);
	       
	    	builder.setView(alertDialogView); 
	        	 
	    	 final EditText editTextRename = (EditText) alertDialogView.findViewById(R.id.editTextrename); 
	    	 	    	 
	    	 
	       builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
	      	  public void onClick(DialogInterface dialog, int whichButton) {

	      	  }
	      	});//onclick dialog
	    	
	    	
	       builder.setNegativeButton("完成", new DialogInterface.OnClickListener() {
	    	  public void onClick(DialogInterface dialog, int whichButton) {

	    		  
	    		   if (!editTextRename.getText().toString().trim().equals("")){
	    		         
	    			   String newname = editTextRename.getText().toString().trim();
	    			 
	    			   ContentValues args = new ContentValues();
		 	            args.put("List_Name", newname);
		 	            db.update("CheckList_List", args, "List_id=" + tableid, null);
		 	           
	    			   TextView textView = (TextView)findViewById(R.id.textViewnameid);    
	    			   textView.setText("名: "+newname);

	    			    
	    	    }else{
	    	    	Errorbox("你沒有輸入名稱");
	    	    }
	   
	    	  }
	    	});

	      AlertDialog alert = builder.create();
	    	alert.show();
	    	

	    }//end rename box
	    

		
		@Override
		protected void onDestroy() {
		super.onDestroy();
		if (db != null)
		{
		db.close();
		}
		}
		
		
		
	}