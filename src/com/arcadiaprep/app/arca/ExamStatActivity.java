package com.arcadiaprep.app.arca;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.arcadiaprep.app.arca.service.MainDataService;
import com.arcadiaprep.app.arca.service.MainLayoutService;
import com.arcadiaprep.app.arca.service.MainListenerService;
import com.arcadiaprep.app.arca.service.QuestionViewService;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.list.ListExamStatAdapter;
import com.arcadiaprep.app.arca.util.ListHeightUtils;

public class ExamStatActivity extends Activity{
	
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemService.setOrientation(this);
		setContentView(R.layout.exam_stat);
		
		
		Button btnDone = (Button) findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		View content = (View) findViewById(R.id.content);
		LayoutParams para = new LayoutParams(LayoutParams.FILL_PARENT,(int)(SystemService.getScreenHeight(getWindowManager())*0.7));
		para.leftMargin = (int)(SystemService.getScreenWith(getWindowManager())*0.05);
		para.rightMargin = (int)(SystemService.getScreenWith(getWindowManager())*0.05);
		para.topMargin = 5;
		para.bottomMargin = (int)(SystemService.getScreenHeight(getWindowManager())*0.05);
		content.setLayoutParams(para);
		View linearItemBar = (View) findViewById(R.id.linear_item_bar);
		android.widget.RelativeLayout.LayoutParams para1 = new android.widget.RelativeLayout.LayoutParams((int)(SystemService.getScreenWith(getWindowManager())/2),android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		linearItemBar.setLayoutParams(para1);
		
		listView=(ListView)findViewById(R.id.list);
		LinearLayout problemsetBar = (LinearLayout) findViewById(R.id.linear_main_category_bar);
//		LayoutParams params = new LayoutParams((int)(SystemService.getScreenWith(getWindowManager())*0.7), LayoutParams.WRAP_CONTENT);
//		problemsetBar.setGravity(Gravity.CENTER_HORIZONTAL);
//		problemsetBar.setLayoutParams(params);
		//category bar
        List<Button> categoryButtons = MainLayoutService.generateCategoryBar(this, MainDataService.findCategoryBars(this), problemsetBar);
        
        //item category bar
        LinearLayout itemBar = (LinearLayout) findViewById(R.id.item_category_bar);
        LayoutParams itemP = new LayoutParams((int)(SystemService.getScreenWith(getWindowManager())*0.7), LayoutParams.WRAP_CONTENT);
        itemP.rightMargin = (int)(SystemService.getScreenWith(getWindowManager())*0.05);
        itemP.topMargin = (int)(SystemService.getScreenHeight(getWindowManager())*0.08);
        itemBar.setLayoutParams(itemP);
        
        List<Button> itemCategoryButtons = MainLayoutService.generateCategoryBar(this, itemBar);
        
        MainListenerService.getInstance().registerCategoryBarForExamStat(this, categoryButtons,itemCategoryButtons,listView);
        listView.setAdapter(new ListExamStatAdapter(this, QuestionViewService.getExamStatsBySectionAndType(this, categoryButtons.get(0).getText().toString(), 1)));
        ListHeightUtils.setListViewHeightBasedOnChildren(listView);
        
        categoryButtons.get(0).performClick();
        itemCategoryButtons.get(0).performClick();
	}
	
	@Override
	public void onBackPressed() {
		Intent i =  new Intent(this,ArcadiaprepActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);
		finish();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		SystemService.setOrientation(this);
	}

}
