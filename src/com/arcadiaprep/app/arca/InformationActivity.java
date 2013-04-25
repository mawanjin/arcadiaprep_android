package com.arcadiaprep.app.arca;

import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.list.ListInformationAdapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class InformationActivity extends Activity{
	int screenWidth,screenHeight;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		
		screenWidth = SystemService.getScreenWith(getWindowManager());
		screenHeight = SystemService.getScreenHeight(getWindowManager());
		 
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		View list_container = findViewById(R.id.list_container);
		LayoutParams p = new LayoutParams((int)(screenWidth*0.8),LayoutParams.WRAP_CONTENT);
		p.gravity = Gravity.CENTER;
		list_container.setLayoutParams(p);
		
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ListInformationAdapter(this, SystemService.parseInformation(this).getItemName()));
		registerListener();
	}

	private void registerListener() {
		list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long arg3) {
				Intent i;
				ListInformationAdapter a =  (ListInformationAdapter) adapter.getAdapter();
				if(position==a.getCount()-1){
					i = new Intent(Intent.ACTION_VIEW,Uri.parse(ConstantSystem.application_http_portal));
				    startActivity(i);
				    finish();
					
				}else{
					i = new Intent(InformationActivity.this,CourseInformationActivity.class);
					i.putExtra("name", a.getItem(position).toString());
					startActivity(i);
				}
				
			}});
	}
	
}
