package com.arcadiaprep.app.arca.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.constants.ConstantDiscussion;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.service.IDiscussionFragment;
import com.arcadiaprep.app.arca.ui.list.ListExamDiscussionFragmentAdapter;
import com.arcadiaprep.app.arca.util.http.HttpConnection;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;
import com.arcadiaprep.app.arca.vo.DiscussionVO;

public class DiscussionListView extends LinearLayout{
	
	Context context;
	IDiscussionFragment impl;
	int questionId;
	ProgressDialog progressDialog;
	ListView list;
	List<DiscussionVO> discussions = new ArrayList<DiscussionVO>();
	
	TextView tv;
	
	DiscussionService discussionService;
	
	public DiscussionListView(Context context,IDiscussionFragment impl,int questionId) {

		super(context);
		this.context = context;
		this.impl = impl;
		this.questionId = questionId;
		init();

	}

	void init(){
		View v = LayoutInflater.from(context).inflate(
				R.layout.discussion_exam, null);
		
		tv = (TextView) v.findViewById(R.id.txtCommentCount);
		
		TextView post = (TextView) v.findViewById(R.id.txtPost);
		post.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				impl.showDiscussionReplyDialog(ConstantDiscussion.POST_DISCUSSION_TYPE_POST_NEW_FOR_GIVEN_QUESTION,-1,questionId);
			}
		});
		
		discussionService = DiscussionService.getInstance(context);
        
        list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(new ListExamDiscussionFragmentAdapter(context,discussions,new ListExamDiscussionFragmentAdapter.Callback() {
			
			@Override
			public void onReply(int position) {
				
				impl.showDiscussionReplyDialog(ConstantDiscussion.POST_DISCUSSION_TYPE_POST_REPLY,discussions.get(position).getId(),-1);
			}

			@Override
			public void onUserInfo(int position,DiscussionUserVO vo) {
				impl.showDiscussionUserInfoDialog(vo);
			}
		}));
		
		addView(v);
		
		downloadListData();
	}
	
	private void downloadListData() {
		
		progressDialog = ProgressDialog.show(context, "", 
				"waiting...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		discussionService.findDiscussionsByQuestionid(questionId,handler);
		
	}
	
	protected void initList() {
		tv.setText(discussions.size()+" comments");
		((ListExamDiscussionFragmentAdapter)list.getAdapter()).setListItems(discussions);
		((ListExamDiscussionFragmentAdapter)list.getAdapter()).notifyDataSetChanged();
	}
	
	
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
				case HttpConnection.DID_START: {
					Log.d("Image", "Starting Connection");
					break;
				}
				case 4: {
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

}
