package com.arcadiaprep.app.arca;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arcadiaprep.app.arca.service.AsyncImageLoader;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.util.http.HttpConnection;
import com.arcadiaprep.app.arca.vo.CacheImage;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;

public class DiscusstionUserActivity extends Activity{
	
	private AsyncImageLoader loader = AsyncImageLoader.getInstance();
	private ProgressBar progressBar ;
	
	private ImageView imgPortrait ;
	private TextView txtUname;
	private TextView txtUnameSmall;
	private TextView txtPost; 
	private TextView txtLocation;
	private TextView txtAboutme;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			DiscussionUserVO vo =  (DiscussionUserVO) msg.obj;
			txtUname.setText(vo.getUname());
			txtUnameSmall.setText(vo.getUname());
			txtPost.setText("+"+vo.getPost()+" Posts");
			txtLocation.setText(vo.getLocation());
//			txtLocation.setText("POST: "+vo.getPost());
			txtAboutme.setText(vo.getAboutme());
//			super.handleMessage(msg);
			
			if(CacheImage.currentHead==null){
				progressBar.setVisibility(View.GONE);
			}else{
//				progressBar.setVisibility(View.VISIBLE);
				
//				Bitmap cacheImagePortrait = loader.loadDrawable(vo.getHead(), new AsyncImageLoader.ImageCallback() {
//					@Override
//					public void imageLoaded(Bitmap imageDrawable) {
//						imgPortrait.setImageBitmap(imageDrawable);
//						progressBar.setVisibility(View.GONE);
//					}
//				});
				imgPortrait.setImageBitmap(CacheImage.currentHead);
//				imgPortrait.setImageDrawable(vo.getHead());
				progressBar.setVisibility(View.GONE);			
			}
			
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.discussion_user);
		
		Button btn = (Button) findViewById(R.id.btnDone);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		imgPortrait = (ImageView) findViewById(R.id.imgPortrait);
		txtUname = (TextView) findViewById(R.id.txtUname);
		txtUnameSmall = (TextView) findViewById(R.id.txtUnameSmall);
		txtPost = (TextView) findViewById(R.id.txtPost);
		txtLocation = (TextView) findViewById(R.id.txtLocation);
		txtAboutme = (TextView) findViewById(R.id.txtAboutme);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		DiscussionUserVO vo = (DiscussionUserVO) getIntent().getExtras().getSerializable("discussionUserVO");
		
		DiscussionService.getInstance(this).getUserInfo(handler,vo);
		
//		DiscussionUserVO user = DiscussionService.getInstance(this).getUserInfo(1);
//		imgPortrait.setContentDescription(user.getHead());
		
		
//		user.setHead("http://arcadia.witagg.com/_layouts/images/img_user65.png");
		
		
	}

}
