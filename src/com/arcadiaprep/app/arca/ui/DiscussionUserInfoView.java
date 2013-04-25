package com.arcadiaprep.app.arca.ui;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.AsyncImageLoader;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.vo.CacheImage;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DiscussionUserInfoView extends LinearLayout {
	Context context;
	private AsyncImageLoader loader = AsyncImageLoader.getInstance();
	private ProgressBar progressBar ;
	
	private TextView txtUname;
	private TextView txtUnameSmall;
	private TextView txtPost;
	private TextView txtLocation;
	private TextView txtAboutme;
	
	private DiscussionUserVO vo;
	
	private ImageView imgPortrait;
	private View v ;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
			DiscussionUserVO vo =  (DiscussionUserVO) msg.obj;
			txtUname.setText(vo.getUname());
			txtUnameSmall.setText(vo.getUname());
			txtPost.setText("+"+vo.getPost()+"posts");
			txtLocation.setText(vo.getLocation());
			txtAboutme.setText(vo.getAboutme());
			
//			if(CacheImage.currentHead==null){
//				progressBar.setVisibility(View.GONE);
//			}else{
				progressBar.setVisibility(View.VISIBLE);
				
				Bitmap cacheImagePortrait = loader.loadDrawable(vo.getHead(), new AsyncImageLoader.ImageCallback() {
					@Override
					public void imageLoaded(Bitmap imageDrawable) {
						imgPortrait.setImageBitmap(imageDrawable);
						progressBar.setVisibility(View.GONE);
					}
				});
				if(cacheImagePortrait!=null){
					imgPortrait.setImageBitmap(cacheImagePortrait);
					progressBar.setVisibility(View.GONE);
				}
						
			
		}
		
	};
	
	public DiscussionUserInfoView(Context context,DiscussionUserVO vo) {
		super(context);
		this .context = context;
		this.vo = vo;
		init();
	}

	void init() {
		v = LayoutInflater.from(context).inflate(
				R.layout.discussion_user_fragment, null);

		imgPortrait = (ImageView) v.findViewById(R.id.imgPortrait);
		txtUname = (TextView) v.findViewById(R.id.txtUname);
		txtUnameSmall = (TextView) v.findViewById(R.id.txtUnameSmall);
		txtPost = (TextView) v.findViewById(R.id.txtPost);
		txtLocation = (TextView) v.findViewById(R.id.txtLocation);
		txtAboutme = (TextView) v.findViewById(R.id.txtAboutme);
		
     	DiscussionService.getInstance(context).getUserInfo(handler,vo);
//		DiscussionUserVO user = DiscussionService.getInstance(context)
//				.getUserInfo(1);

		
//		imgPortrait.setContentDescription(user.getHead());
		
		
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//		user.setImgAttach("http://arcadia.witagg.com/_upload/work/13477010136056549730.png");
		
		
		addView(v);
	}

}
