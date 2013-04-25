package com.arcadiaprep.app.arca;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.constants.ConstantDiscussion;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.ui.list.ListExamDiscussionAdapter;
import com.arcadiaprep.app.arca.util.http.HttpConnection;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;
import com.arcadiaprep.app.arca.vo.DiscussionVO;

public class DiscussionActivity extends Activity {

	private ProgressDialog progressDialog;
	private List<DiscussionVO> discussions = new ArrayList<DiscussionVO>();
	ListView list;
	WebView webview;
	String webdata="<font color='red'>123</font>";
	DiscussionService discussionService = DiscussionService
			.getInstance(this);
	ImageView imgPortrait;
	boolean fromQuestionView=false;
	private int questionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discussion);
		
		if(getIntent().getExtras()!=null&&getIntent().getExtras().getString("from")!=null&&getIntent().getExtras().getString("from").equals("question_view")){
			fromQuestionView = true;
			questionId = getIntent().getExtras().getInt("question_id");
		}
		
		list = (ListView) findViewById(R.id.list);
		
		TextView editTextPost = (TextView) findViewById(R.id.txtPost);
		editTextPost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(DiscussionActivity.this,DiscussionReplyActivity.class);
				if(fromQuestionView){
					i.putExtra("questionId", questionId);
					i.putExtra("type", ConstantDiscussion.POST_DISCUSSION_TYPE_POST_NEW_FOR_GIVEN_QUESTION);
					i.putExtra("from", "question_view");
				}
				else
					i.putExtra("type", ConstantDiscussion.POST_DISCUSSION_TYPE_POST_NEW);
				startActivity(i);
				finish();
			}
		});
		
		Button btnBack = (Button) findViewById(R.id.btnBack); 
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fromQuestionView){finish();return;}
				Intent i = new Intent(DiscussionActivity.this,ArcadiaprepActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(i);
				finish();
			}
		});
		
		list.setAdapter(new ListExamDiscussionAdapter(this,
				discussions, new ListExamDiscussionAdapter.Callback() {

					@Override
					public void onReply(int position) {
						Intent i = new Intent(DiscussionActivity.this,DiscussionReplyActivity.class);
						i.putExtra("type", ConstantDiscussion.POST_DISCUSSION_TYPE_POST_REPLY);
						
						if(fromQuestionView)
							i.putExtra("questionId", questionId);
						else
						i.putExtra("questionId", discussions.get(position).getQuestionId());
						
						i.putExtra("discussionId", discussions.get(position).getId());
						if(fromQuestionView)
						i.putExtra("from", "question_view");
						startActivity(i);
						finish();
					}

					@Override
					public void onUserInfo(int position,DiscussionUserVO vo) {
						
						Intent i = new Intent(DiscussionActivity.this,DiscusstionUserActivity.class);
						
//						DiscussionUserVO vo = new DiscussionUserVO();
//						vo.setHead(imgPortrait.getDrawable());
//						i.putExtra("userId", discussions.get(position).getUserId());
						vo.setUserID(discussions.get(position).getUserId());
						
//						vo.setImgAttach(attachImg);
						i.putExtra("discussionUserVO", vo);
						startActivity(i);
						finish();
					}
				}));
		
		downloadListData();
		
	}

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
				case HttpConnection.DID_START: {
					Log.d("Image", "Starting Connection");
					break;
				}
				case HttpConnection.DID_SUCCEED: {
					discussions = (List<DiscussionVO>) message.obj;
					initList();
					progressDialog.dismiss();
					break;
				}case 4:{
					discussions = (List<DiscussionVO>) message.obj;
					initList();
					progressDialog.dismiss();
					break;
				}
				case HttpConnection.DID_ERROR: {
					Exception e = (Exception) message.obj;
					e.printStackTrace();
					break;
				}
			}
		}
	};
	
	private void downloadListData() {
		
		progressDialog = ProgressDialog.show(this, "", 
				"waiting...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		if(fromQuestionView){
			discussionService.findDiscussionsByQuestionid(questionId,handler);
		}else{
			discussionService.findDiscussionsByExamType(handler);
		}
		
		
	}

	protected void initList() {
		
//		discussions = new ArrayList<DiscussionVO>();
//		DiscussionVO o = new DiscussionVO();
//		o.setId(1);
//		o.setDiscusstionId(1);
//		o.setMessage("message");
//		o.setUname("uname");
//		discussions.add(o);
		((ListExamDiscussionAdapter)list.getAdapter()).setListItems(discussions);
		
		((BaseAdapter)list.getAdapter()).notifyDataSetChanged();
	}

}
