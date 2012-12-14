package com.hwy.checklist;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.google.ads.AdRequest;
import com.google.ads.AdView;



public class CheckListActivity extends TabActivity implements OnTabChangeListener {
	
	TabHost tabHost;

	int gettypeno;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tab);

    /** TabHost will have Tabs */
    tabHost = (TabHost)findViewById(android.R.id.tabhost);
    
    /** TabSpec used to create a new tab.
    * By using TabSpec only we can able to setContent to the tab.
    * By using TabSpec setIndicator() we can set name to tab. */

    /** tid1 is firstTabSpec Id. Its used to access outside. */
    TabSpec MainTabSpec = tabHost.newTabSpec("tad1");
    TabSpec BbqTabSpec = tabHost.newTabSpec("tad2");
    TabSpec HotpotTabSpec = tabHost.newTabSpec("tad3");
    TabSpec TravelTabSpec = tabHost.newTabSpec("tad4");
    TabSpec CreatenewlistSpec = tabHost.newTabSpec("tad5");

    
    
    /** TabSpec setIndicator() is used to set name for the tab. */
    /** TabSpec setContent() is used to set content for a particular tab. */
 //   Intent intent = new Intent(this, SecondActivity.class); intent.put("name", "Something1");
    MainTabSpec.setIndicator("主頁").setContent(new Intent(this,Main.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    BbqTabSpec.setIndicator("BBQ").setContent(new Intent(this,Checkliststate.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    HotpotTabSpec.setIndicator("打邊爐").setContent(new Intent(this,Checkliststate.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    TravelTabSpec.setIndicator("旅行").setContent(new Intent(this,Checkliststate.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    CreatenewlistSpec.setIndicator("編輯清單").setContent(new Intent(this,Createnewlist.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    

    
    /** Add tabSpec to the TabHost to display. */
    tabHost.addTab(MainTabSpec);
    tabHost.addTab(BbqTabSpec);
    tabHost.addTab(HotpotTabSpec);
    tabHost.addTab(TravelTabSpec);
    tabHost.addTab(CreatenewlistSpec);

    setTabColor(tabHost);
    tabHost.setOnTabChangedListener(this);
    /*
    for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
        tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 60;
    }*/
    
    tabHost.getTabWidget().getChildAt(4).setVisibility(View.GONE);
    tabHost.setCurrentTab(0);

/*
    tabHost.setOnTabChangedListener(new OnTabChangeListener() {
        @Override
        public void onTabChanged(String arg0) {         
    	  
        	gettypeno=tabHost.getCurrentTab();
  			Message m = new Message();
	     		// 定義 Message的代號，handler才知道這個號碼是不是自己該處理的。
	     		m.what = MEG_INVALIDATE;
	     		handler.sendMessage(m);
        }       
    });  */
    

    
    // Look up the AdView as a resource and load a request.
    AdView adView = (AdView)this.findViewById(R.id.adView);
    adView.loadAd(new AdRequest());


    }
    
  
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
    	setTabColor(tabHost);
     		}

    
    public static void setTabColor(TabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF")); //unselected
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FF3232")); // selected
    }

    

    
}