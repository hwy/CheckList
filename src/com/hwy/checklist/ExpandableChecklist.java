package com.hwy.checklist;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//自定义的ExpandListAdapter
class ExpandableChecklist extends BaseExpandableListAdapter
{
private Context context;
List<Map<String, String>> groups;
List<List<Map<String, String>>> childs;



//SQL
	//SQLiteDatabase对象
	SQLiteDatabase db;
	//数据库名
	public String db_name = "CheckList.db";
	//表名
	public String table_name;
	public String tableid;
	//辅助类名
	final DbHelper helper = new DbHelper(context, db_name, null, 1);
//end sql
/*
* 构造函数:
* 参数1:context对象
* 参数2:一级列表数据源
* 参数3:二级列表数据源
*/
public ExpandableChecklist(Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> childs, SQLiteDatabase db, String table_name, String tableid)
{
this.groups = groups;
this.childs = childs;
this.context = context;
this.db = db;
this.table_name = table_name;
this.tableid = tableid;
}

@Override
public Object getChild(int groupPosition, int childPosition)
{
return childs.get(groupPosition).get(childPosition);
}

@Override
public long getChildId(int groupPosition, int childPosition)
{
return childPosition;
}

//获取二级列表的View对象
@Override
public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
ViewGroup parent)
{
@SuppressWarnings("unchecked")
final String Food_Name = ((Map<String, String>) getChild(groupPosition, childPosition)).get("foodname");
@SuppressWarnings("unchecked")
final
String checkboxstate = ((Map<String, String>) getChild(groupPosition, childPosition)).get("checkboxstate");
@SuppressWarnings("unchecked")
final String Price = ((Map<String, String>) getChild(groupPosition, childPosition)).get("Price");
@SuppressWarnings("unchecked")
final String Qty = ((Map<String, String>) getChild(groupPosition, childPosition)).get("Qty");

final String Food_Id = ((Map<String, String>) childs.get(groupPosition).get(childPosition)).get("Food_id");			


LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//获取二级列表对应的布局文件, 并将其各元素设置相应的属性
LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.displaychildslistview, null);
CheckBox checkBox = (CheckBox) linearLayout.findViewById(R.id.checkBox1); 

//Do the String to boolean conversion
checkBox.setChecked(Boolean.parseBoolean(checkboxstate));


TextView tv = (TextView) linearLayout.findViewById(R.id.textViewchild);
tv.setText(Food_Name);


final TextView textPrice = (TextView)linearLayout.findViewById(R.id.Textprice);
textPrice.setText(Price);



final TextView textQty = (TextView)linearLayout.findViewById(R.id.Textqty);
textQty.setText(Qty);




//check box
checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {   
        if (isChecked)
        {                	
        	//ContentValues对象
			ContentValues cv = new ContentValues();
			cv.put("Checkstate", "1");
			cv.put("Send", "1");
			
        	db.update(table_name, cv, "Food_id='"+Food_Id+"' and List_id2='"+tableid+"'", null);
System.out.println("Food_id='"+Food_Id+"' and List_id2='"+tableid+"'");
				//Toast.makeText(context,	"己購買" + Food_Name , Toast.LENGTH_SHORT)		.show();   
				childs.get(groupPosition).get(childPosition).put("checkboxstate", "true");

        }else{
        	
        	//删除方法
    		ContentValues cv = new ContentValues();
			cv.put("Checkstate", "0");
			cv.put("Send", "1");
			
			db.update(table_name, cv, "Food_id='"+Food_Id+"' and List_id2='"+tableid+"'", null);

		//		Toast.makeText(context,						"取消購買" + Food_Name , Toast.LENGTH_SHORT)						.show();  

				childs.get(groupPosition).get(childPosition).put("checkboxstate", "false");
        }//end if
        
      //display all data
        
		Cursor c = db.query(table_name, null, "List_id2='"+tableid+"' and Checkstate='0'", null, null,	null, null);
		//cursor.getCount()記錄數量
		Toast.makeText(context,"你還有" + c.getCount() + "個未買",
				Toast.LENGTH_SHORT).show();
/*		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			System.out.println("Table_id"+ c.getInt(0)  +"Food_id"+ c.getInt(1)  +"Checkstate" + c.getInt(2)  + "，Price"+c.getString(3) + "，Qty"+c.getString(4));
		}
		System.out.println("完"); */
		c.close();
    }
}); //end checkbox
	



return linearLayout;
}

@Override
public int getChildrenCount(int groupPosition)
{
return childs.get(groupPosition).size();
}

@Override
public Object getGroup(int groupPosition)
{
return groups.get(groupPosition);
}

@Override
public int getGroupCount()
{
return groups.size();
}

@Override
public long getGroupId(int groupPosition)
{
return groupPosition;
}

//获取一级列表View对象
@Override
public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
{
String text = groups.get(groupPosition).get("group");
LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//获取一级列表布局文件,设置相应元素属性
RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.grouplistview, null);
TextView textView = (TextView)relativeLayout.findViewById(R.id.TextViewGroup);
textView.setText(text);

return relativeLayout;
}

// **************************************
public boolean hasStableIds() {
	return true;
}

public boolean isChildSelectable(int groupPosition, int childPosition) {
	return true;
}



}