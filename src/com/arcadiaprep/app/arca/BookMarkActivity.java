package com.arcadiaprep.app.arca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.constants.ConstantQuestion;
import com.arcadiaprep.app.arca.mo.BookMark;
import com.arcadiaprep.app.arca.service.BookMarkService;
import com.arcadiaprep.app.arca.ui.list.ListViewBookMarkAdapter;

public class BookMarkActivity extends Activity {

	TextView txtDate,txtSets;
	ListView list;
	Button btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);
		
		btnBack = (Button) findViewById(R.id.btnBack);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtSets = (TextView) findViewById(R.id.txtSets);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ListViewBookMarkAdapter(this, BookMarkService.findAll(this, "date")));
		
		registerListener();
	}
	
	private void registerListener() {
		txtDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ListViewBookMarkAdapter)list.getAdapter()).setListItems(BookMarkService.findAll(BookMarkActivity.this, "date"));
				((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
				changeOrderBarBgColor(txtDate);
			}
		});
		txtSets.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ListViewBookMarkAdapter)list.getAdapter()).setListItems(BookMarkService.findAll(BookMarkActivity.this, "ex_app_id"));
				((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
				changeOrderBarBgColor(txtSets);
			}
		});
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				BookMark bookMark = (BookMark) list.getAdapter().getItem(position);
				Intent i =new Intent(BookMarkActivity.this,QuestionViewActivity.class);
				i.putExtra("ex_app_id", bookMark.getExappid());
				i.putExtra("VIEW_TYPE", ConstantQuestion.QUESTION_VIEW_TYPE_BOOKMARK);
				i.putExtra("pageno", bookMark.getPosition());
				i.putExtra("ex_app_name", bookMark.getTitle());
				i.putExtra("question_id", bookMark.getQuestionId());
				
				startActivity(i);
			}});
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BookMarkActivity.this,ArcadiaprepActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}
		});
		
	}

	private void changeOrderBarBgColor(TextView view){
		if(view.getId() ==txtDate.getId()){
			txtSets.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button));
			txtSets.setTextColor(getResources().getColor(android.R.color.black));
		}else{
			txtDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button));
			txtDate.setTextColor(getResources().getColor(android.R.color.black));
		}
		view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_blue));
		view.setTextColor(getResources().getColor(android.R.color.white));
	}
	

}
