package com.arcadiaprep.app.arca;

import java.net.URLEncoder;

import com.arcadiaprep.app.arca.service.MainDataService;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.vo.MinItemVO;
import com.arcadiaprep.app.arca.vo.ModuleInfoVO;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class CourseInformationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_info);
		
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		String name = getIntent().getExtras().get("name").toString();
		MinItemVO vo = SystemService.parseInformation(this).getItemByItemName(name);
		((TextView)findViewById(R.id.txtTitle)).setText(name);
		
		WebView web = (WebView) findViewById(R.id.webview);
		web.setWebViewClient(new WebViewClient());
		//web.loadData(vo.getText(), "text/html", "utf-8");
		web.loadDataWithBaseURL("file:///android_asset/",vo.getText(), "text/html","utf-8", null);
 
	}

}
