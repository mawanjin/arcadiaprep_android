package com.arcadiaprep.app.arca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.arcadiaprep.app.arca.vo.ListItemRecommendationVO;

public class PackageIntroductionActivity extends Activity{
	private String LOG_TAG = "PackageIntroductionActivity";
	ListItemRecommendationVO vo;
	TextView txtSetName;
	WebView webContainer;
	Button btnDone,btnBuy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.package_introduction);
		vo = getIntent().getParcelableExtra("package");
		txtSetName = (TextView) findViewById(R.id.packageName);
		txtSetName.setText(vo.getTitle());
		
		btnDone = (Button) findViewById(R.id.btnDone);
		btnBuy = (Button) findViewById(R.id.btnBuy_Now);
		btnDone.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PackageIntroductionActivity.this,ArcadiaprepActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}});
		
		webContainer = (WebView) findViewById(R.id.webContainer);
		//webContainer.loadData(vo.getAboutPage().getText(), "text/html", "utf-8");
		webContainer.loadDataWithBaseURL("file:///android_asset/",vo.getAboutPage().getText(), "text/html", "utf-8",null);

	}
	
}
