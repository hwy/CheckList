package com.hwy.checklist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Main extends Activity {

	
	
	 public static final int MEG_INVALIDATE = 1110; //自訂一個號碼

	//SQL
	//SQLiteDatabase对象
	SQLiteDatabase db;
	//数据库名
	public String db_name = "CheckList.db";
	//表名

	//辅助类名
	final DbHelper helper = new DbHelper(this, db_name, null, 2);
	
	String gettypeno;
		
	Button buttonDellist;
	
	GridView gridview;
	SimpleAdapter saImageItems;
	ArrayList<HashMap<String, Object>> lstImageItem;
	ProgressDialog dialog;
	Thread t;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         gridview = (GridView) findViewById(R.id.gridview);
        
         
         //讀取表
         Button buttonLoadlist = (Button) findViewById(R.id.buttonloadlist);
         buttonLoadlist.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {
            	   
            	   loadlistbox();
               }
           });
         
         
         
        //新增表
        Button buttonNewlist = (Button) findViewById(R.id.buttonnewlist);
        buttonNewlist.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            	  newlistbox();
              }
          });
        
      //冊除表
        buttonDellist = (Button) findViewById(R.id.buttoneditlist);
        
        buttonDellist.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            		 dellistbox();
                      
              }});
        
  
       
      lstImageItem = new ArrayList<HashMap<String, Object>>();
       
        
        
        
      //display all data
      
      		if (db==null){
      		 db = helper.getWritableDatabase();
      		}
      		
      		Cursor c = db.query("CheckList_List", null, null, null, null,	null, "List_id DESC");

      		//cursor.getCount()記錄數量
      		//Toast.makeText(this,"你已經選擇了" + c.getCount() + "個記錄：", Toast.LENGTH_SHORT).show();
      		
      	    
      		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
      			//System.out.println("Table_id"+ c.getInt(0)  +"Food_id"+ c.getInt(1)  +"Checkstate" + c.getInt(2)  + "，Price"+c.getString(3) + "，Qty"+c.getString(4));
      		
          		System.out.println("Table_id "+ c.getInt(0) +"Int_id "+ c.getInt(1) +"Name "+ c.getString(2)  +"Type " + c.getInt(3)  + "，PW "+c.getInt(4)+ "，Date "+c.getInt(5));

        	HashMap<String, Object> map = new HashMap<String, Object>();
        	if(c.getInt(3)==1){
        	map.put("List_Img", R.drawable.bbqicon );//添加图像资源的ID
        	map.put("List_type", "CheckList_BbqListitems");
        	
        	}else if(c.getInt(3)==2){
            	map.put("List_Img", R.drawable.hotpoticon );//添加图像资源的ID
            	map.put("List_type", "CheckList_HotListitems");
            	
            	}else
            	{
        		System.out.println(c.getInt(3));
             	map.put("List_Img", R.drawable.travelicon);//添加图像资源的ID
        		map.put("List_type", "CheckList_TravelListitems");
        	}
        	
			map.put("List_Name", c.getString(2));//TABLE名
			map.put("Table_id", c.getInt(0));//TABLE_ID
        	lstImageItem.add(map);
        }
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        saImageItems = new SimpleAdapter(this, //没什么解释
        		                                    lstImageItem,//数据来源 
        		                                    R.layout.maingridview,//night_item的XML实现
        		                                    
        		                                    //动态数组与ImageItem对应的子项        
        		                                    new String[] {"List_Name", "List_Img"}, 
        		                                    
        		                                    //ImageItem的XML文件里面的一个ImageView,两个TextView ID
        		                                    new int[] {R.id.ItemText, R.id.ItemImage});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new ItemClickListener());
    
	
	


	
	}//end create
    
    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class  ItemClickListener implements OnItemClickListener
    {
		public void onItemClick(final AdapterView<?> arg0,//The AdapterView where the click happened 
				                          View arg1,//The view within the AdapterView that was clicked
				                          final int arg2,//The position of the view in the adapter
				                          long arg3//The row id of the item that was clicked
				                          ) {
			//在本例中arg2=arg3
			final HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			//显示所选Item的ItemText
			//setTitle((String)item.get("List_Name"));
		
			
			if(buttonDellist.getText().toString().trim().equals("完成")){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
				builder.setMessage("確認刪除"+item.get("List_Name")+"?");
				builder.setTitle("提示");
				builder.setPositiveButton("取消", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					
						
					}});
				builder.setNegativeButton("確認", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						
						
						System.out.println(db.delete("CheckList_List",	//資料表名稱
								"List_id=" + item.get("Table_id"),			//WHERE
								null				//WHERE的參數
								));
						
						System.out.println(db.delete(item.get("List_type").toString(),	//資料表名稱
								"List_id2=" + item.get("Table_id"),			//WHERE
								null				//WHERE的參數
								));
						
						lstImageItem.remove(arg0.getItemAtPosition(arg2));
						saImageItems.notifyDataSetChanged();
						}			
						});
				  builder.create().show();
			
				
	        }else{
	        	//LOAD LIST
	        	
	        	LoadList(item.get("Table_id").toString(),item.get("List_type").toString());      	
	        	
	        } //end if
			
		}
    	
    }
    
    
    //load existing list
	private void LoadList (String Table_id, String List_type){
    	
	    Intent i = getParent().getIntent();
	      
	    TabActivity ta = (TabActivity) Main.this.getParent();
	    if(List_type.equals("CheckList_BbqListitems")){
	    	
	    i.putExtra("Bbq_id", Table_id); 	
	    i.putExtra("gettypeno", "1");
	    ta.getTabHost().setCurrentTab(1);
	    }else if(List_type.equals("CheckList_HotListitems")){
	    	i.putExtra("Hotpot_id", Table_id);
	    	i.putExtra("gettypeno", "2");
	    	ta.getTabHost().setCurrentTab(2);
		    }else
		    {
	    	
	    	i.putExtra("Travel_id", Table_id);
	    	i.putExtra("gettypeno", "3");
	    	ta.getTabHost().setCurrentTab(3);
	    }
    	
    } //end load list
    
    
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
                 gettypeno=String.valueOf(position);
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

    		  
    		   if (!editTextnewlist.getText().toString().trim().equals("")&& gettypeno!=null){
    		         
    			   String temNewlistname = editTextnewlist.getText().toString().trim();
    			   
    			    Intent i = getParent().getIntent();
    			    i.putExtra("newListname", temNewlistname);
    			    i.putExtra("gettypeno", gettypeno);
    			    i.removeExtra("tableid");
    			    TabActivity ta = (TabActivity) Main.this.getParent();
    			    ta.getTabHost().setCurrentTab(4);
    			    
    			    
    	    }else{
    	    	Errorbox("你沒有輸入名稱或選擇類別");
    	    }
   
    	  }
    	});

      AlertDialog alert = builder.create();
    	alert.show();
    	

    }//end insert items box
    

    
    
    // load list
    private void loadlistbox (){


    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	builder.setTitle("讀取清單");
    	builder.setMessage("請輸入清單ID及PW,如果沒有PW,只會讀取清單,不能同步清單");

    	 LayoutInflater inflater = LayoutInflater.from(this);

    	 View alertDialogView = inflater.inflate(R.layout.loadlist, null);
       
    	builder.setView(alertDialogView); 
        	 
    	 final EditText editTextloadid = (EditText) alertDialogView.findViewById(R.id.editTextloadid); 
    	 final EditText editTextloadpw = (EditText) alertDialogView.findViewById(R.id.editTextloadpw);
    	 
       builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
      	  public void onClick(DialogInterface dialog, int whichButton) {

      	  }
      	});//onclick dialog
    	
    	
       builder.setNegativeButton("完成", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {

    		   final String loadlistid = editTextloadid.getText().toString().trim();
			   final String loadlistpw = editTextloadpw.getText().toString().trim();
			
    		   if (loadlistid.length()>=6){

    			   

    			   Cursor dataCount = db.rawQuery("select count(*) from CheckList_List where Int_id='" + loadlistid+"'", null);
    			   dataCount.moveToFirst();

    			   if(dataCount.getInt(0)<1){
    			   t=new Thread() {
	     	           public void run() {
	     	          	 
	     	     		Looper.prepare();  
    			   showDialog(0);
    			   LoadOnList(loadlistid,loadlistpw);
    			   //Load online list 
    			   Looper.loop();
    	          	
	     	           }
	     	     };      
	     	     t.start();
	     	     
    			   }else{Errorbox("你已經有此ID");}//CHECK have id or not
    	    } else{
    	    	Errorbox("你沒有輸入ID號,或字數不夠");
    	    }
   
    	  }
    	});

      AlertDialog alert = builder.create();
    	alert.show();
    	

    }//end load list
    
    
    private void dellistbox(){
    	
    	final int size = gridview.getChildCount();
    	
        if(buttonDellist.getText().toString().trim().equals("刪除")){
            buttonDellist.setText("完成");   
            
         	for(int i = 0; i < size; i++) {
          	  ViewGroup gridChild = (ViewGroup) gridview.getChildAt(i);
         
          	  ImageView ItemImagedek = (ImageView) gridChild.findViewById(R.id.ItemImagedek);
         		  ItemImagedek.setVisibility(View.VISIBLE);    		        		   
          	}
        }else{
      	  buttonDellist.setText("刪除");   
      	  
       	for(int i = 0; i < size; i++) {
        	  ViewGroup gridChild = (ViewGroup) gridview.getChildAt(i);
       
        	  ImageView ItemImagedek = (ImageView) gridChild.findViewById(R.id.ItemImagedek);
       		  ItemImagedek.setVisibility(View.GONE);    		        		   
        	}
        }
        
      	
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
	  
	  
	  //LOAD ONLINE LIST
	  
	  public void LoadOnList(String loadlistid, String loadlistpw){
		  
			
	      List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
	        
      	  nameValuePairs.add(new BasicNameValuePair("loadlistid", loadlistid));  
      	  nameValuePairs.add(new BasicNameValuePair("loadlistpw", loadlistpw));
      	  	        
	        InputStream is = null;
	 	   
	 	   String result = "";

	 	    //http post
	 	    try{
	 	            HttpClient httpclient = new DefaultHttpClient();
	 	            HttpPost httppost = new HttpPost("http://www.rapidtao.com/t/hwy/checklistphp/loadlist.php");
		 	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
	 	            HttpResponse response = httpclient.execute(httppost);
	 	            HttpEntity entity = response.getEntity();
	 	            is = entity.getContent();
	   
	 	           
	 	    }catch(Exception e){
	 	    	removeDialog(0);
	 	    	Errorbox("連線出現問題,請稍後再試!"); 	
	 	         System.out.println(e);   
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
	 	           
	 	          String[] msgreturn=result.split("#xy");
	 	          String msgtext;
	 	          if(msgreturn.length>=7&&msgreturn[0].equals("sucess")){

	 	        	ContentValues args = new ContentValues();
	 	        	
	 	        	msgtext="讀取成功,但沒有密碼或密碼錯誤.不能共享,需重新發佈";
	 	            args.put("List_Type", msgreturn[3]);
	 	            args.put("List_Name", msgreturn[2]);
	 	            System.out.println(msgreturn[4]+loadlistpw);
	 	            if(msgreturn[4].equals(loadlistpw)){
	 	            args.put("Int_id", msgreturn[1]);
	 	            args.put("Pw", msgreturn[4]);
	 	            args.put("Createtime", msgreturn[5]);
	 	            msgtext="讀取成功,你可以線上編輯這個ID";
	 	            }
	 	            long tableid=db.insert("CheckList_List", "", args);
	 	            

	 	            JSONArray jArray = new JSONArray(msgreturn[6]);

		        	ContentValues cv = new ContentValues();

		        	 
			        for(int count=0;count<jArray.length();count++){
			        	JSONObject jsonResponse = jArray.getJSONObject(count);
			        	//Sqlexe+="INSERT INTO "+table_foodtypename+" (Food_id, Types_id, Name, Vote, Report) VALUES ("+jsonResponse.getInt("Food_id")+","+jsonResponse.getInt("Types_id")+","+jsonResponse.getString("Name")+","+jsonResponse.getInt("Vote")+","+jsonResponse.getInt("Report")+");";
		            	cv.put("List_id2", tableid);
		            	cv.put("Food_id", jsonResponse.getInt("Food_id"));
						cv.put("Checkstate", jsonResponse.getString("Checkstate"));
						cv.put("Price", jsonResponse.getInt("Price"));
						cv.put("Qty", jsonResponse.getInt("Qty"));
						//添加方法
						
						String table_listypes;
						if (msgreturn[3].equals("1")){
							table_listypes = "CheckList_BbqListitems";	
						}else{
							table_listypes = "CheckList_HotListitems";
						}
						
						db.insert(table_listypes, "", cv);

			        };

			        HashMap<String, Object> map = new HashMap<String, Object>();
		        	if(msgreturn[3].equals("1")){
		        	map.put("List_Img", R.drawable.bbqicon );//添加图像资源的ID
		        	map.put("List_type", "CheckList_BbqListitems");
		        	
		        	}else if(msgreturn[3].equals("2")) {
		             	map.put("List_Img", R.drawable.hotpoticon);//添加图像资源的ID
		        		map.put("List_type", "CheckList_HotListitems");
		        	} else {
		             	map.put("List_Img", R.drawable.travelicon);//添加图像资源的ID
		        		map.put("List_type", "CheckList_TravelListitems");
		        	}
		        	
					map.put("List_Name", msgreturn[2]);//TABLE名
					map.put("Table_id", tableid);//TABLE_ID
					lstImageItem.add(0,map);
					
			        Errorbox(msgtext);
				
 	     			Message m = new Message();
     	     		m.what = MEG_INVALIDATE;
     	     		handler.sendMessage(m);
	 	     }else {
		 	   Errorbox("沒有此ID");
		 	   }
		 	    	removeDialog(0);
  
	 	    }catch(Exception e){
	 	    	removeDialog(0);
	 	    	  Toast.makeText(this,"讀取錯誤,請稍後再試", Toast.LENGTH_LONG).show();
		 	    	removeDialog(0);

	 	    }
	 	    
		 	 
	 	   
	 	     
	  }
	  
	  
		@Override
		protected void onDestroy() {
		super.onDestroy();
		if (db != null)
		{
		db.close();
		}
		}
	  
		
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
	    }//end dialog
	    
	    
	    //handler msg
	    Handler handler = new Handler() 
	    {

	          public void handleMessage(Message msg) 
	    {
	     switch (msg.what) 
	  {
	  case MEG_INVALIDATE:
			saImageItems.notifyDataSetChanged();	
    break;
            }
	      super.handleMessage(msg);
	          }

	      };//end handler msg
	      
}