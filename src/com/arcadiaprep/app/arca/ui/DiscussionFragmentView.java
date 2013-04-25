package com.arcadiaprep.app.arca.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arcadiaprep.app.arca.DiscussionReplyActivity;
import com.arcadiaprep.app.arca.QuestionViewActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.constants.ConstantDiscussion;
import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.service.IDiscussionFragment;
import com.arcadiaprep.app.arca.util.http.HttpConnection;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;
import com.arcadiaprep.app.arca.vo.ImageVO;
import com.arcadiaprep.app.login.common.Login;
import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.model.User;

public class DiscussionFragmentView extends RelativeLayout implements
		IDiscussionFragment {

	Context context;
	DiscussionReplyView discussionReplyView;
	AlertDialog errorDialog;
	AlertDialog postErrorDialog;

	Activity parent;
	View dialogContainer;
	Button btnLeft;
	Button btnRight;
	ImageButton ibtnClose;
	LinearLayout contentContainer;
	Callback callback;
	int questionId;
	int discussionId;
	int discussionPostType;
	
	User mUser = null;
	File file;
	
	ProgressDialog progressDialog;  
	
	public DiscussionFragmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parent = (Activity) context;
		this.context = context;

		init();
	}
	
	public DiscussionFragmentView(Context context,Callback callback) {

		super(context);
		this.context = context;
		parent = (Activity) context;
		init();
		this.callback = callback;
		initErrorDialog();
		postErrorDialog();
	}

	

	private void init() {
		View v = LayoutInflater.from(context).inflate(
				R.layout.discussion_fragment_view, null);

		dialogContainer = v.findViewById(R.id.dialogContainer);

		ibtnClose = (ImageButton) v.findViewById(R.id.ibtnClose);
		ibtnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				dialogContainer.setVisibility(View.GONE);
				((QuestionViewActivity)context).setShowDiscussion(false);
				callback.onClose();
			}
		});

		btnLeft = (Button) v.findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDiscussionDialog(questionId);
			}
		});

		btnRight = (Button) v.findViewById(R.id.btnRight);

		contentContainer = (LinearLayout) v.findViewById(R.id.content_container);
		
		
		addView(v);

	}

	public void showDiscussionUserInfoDialog(DiscussionUserVO vo) {
		resetButton(2);
		dialogContainer.setVisibility(View.VISIBLE);
		contentContainer.removeAllViews();
		contentContainer.addView(new DiscussionUserInfoView(context,vo));
	}

	public void showDiscussionReplyDialog(int postDiscussionType, int discussionId,int questionId) {
		resetButton(1);
		dialogContainer.setVisibility(View.VISIBLE);
		this.discussionPostType = postDiscussionType;
		this.discussionId = discussionId;
		if(questionId!=-1)
		this.questionId = questionId;
		contentContainer.removeAllViews();
		discussionReplyView = new DiscussionReplyView(context,new DiscussionReplyView.Callback() {
			@Override
			public void onRemoveAttach() {
				file = null;
			}

			@Override
			public void onSelectAttach(ImageVO vo) {
				file = new File(vo.getPath());
				if(file==null||!file.exists())file = null;
			}
		});
		contentContainer.addView(discussionReplyView);
	}

	public void showDiscussionDialog(int questionId) {
		this.questionId = questionId;
		resetButton(0);
		dialogContainer.setVisibility(View.VISIBLE);
		contentContainer.removeAllViews();
		contentContainer.addView(new DiscussionListView(context,this,questionId));
	}

	private void resetButton(int which) {
		switch (which) {
		case 0:
			ibtnClose.setVisibility(View.VISIBLE);
			btnLeft.setVisibility(View.GONE);
			btnRight.setVisibility(View.GONE);
			break;

		case 1:
			ibtnClose.setVisibility(View.GONE);
			btnLeft.setVisibility(View.VISIBLE);
			btnLeft.setText("Cancel");
			btnRight.setVisibility(View.VISIBLE);
			btnRight.setText("Post");
			btnRight.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if ((mUser = UserService.getUser(v.getContext())) == null){
						errorDialog.show();
					}else{
						progressDialog = ProgressDialog.show(context, "", "posting");
						progressDialog.setCancelable(true);
						progressDialog.setCanceledOnTouchOutside(false);
//						Toast.makeText(context, discussionPostType+";did="+discussionId+";pid="+questionId, 1000).show();
						//Post
						switch (discussionPostType) {
							
							case ConstantDiscussion.POST_DISCUSSION_TYPE_POST_NEW:
								DiscussionService.getInstance(context).postNew(handler1, mUser,discussionReplyView.editContent.getText().toString(),file);
								break;
							case ConstantDiscussion.POST_DISCUSSION_TYPE_POST_NEW_FOR_GIVEN_QUESTION:
								DiscussionService.getInstance(context).postForAGivenQuestion(handler1,mUser,questionId, discussionReplyView.editContent.getText().toString(),file);
								break;
							case ConstantDiscussion.POST_DISCUSSION_TYPE_POST_REPLY:
								DiscussionService.getInstance(context).postReply(handler1,mUser,discussionId, discussionReplyView.editContent.getText().toString(),file);
								break;
							default:
								break;

						}
						
					}
				}
			});
			
			break;

		case 2:
			ibtnClose.setVisibility(View.VISIBLE);
			btnRight.setVisibility(View.GONE);

			btnLeft.setVisibility(View.VISIBLE);
			btnLeft.setText("Done");
			break;

		default:
			break;
		}

	}


	public void setImagAttach(String filePath,Bitmap yourSelectedImage) {
		discussionReplyView.imgAttach.setImageBitmap(yourSelectedImage);
		discussionReplyView.filePath = filePath;
		file = new File(filePath);
		if(!file.exists())file=null;
	}
	
	public interface Callback{
		public void onClose();
	}
	
	private void postErrorDialog() {
		postErrorDialog = new AlertDialog.Builder(context)
		.setTitle("Post Error")
		.setMessage("Please try later.")
		.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				postErrorDialog.dismiss();
			}
		})
		.create();
	}
	
	private void initErrorDialog() {
				errorDialog = new AlertDialog.Builder(context)
				.setTitle("Login Status")
				.setMessage("You need to log into Arcadiaprep.com to post a comment.")
				.setNegativeButton("Login & Post", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						errorDialog.dismiss();
						Login.login(((Activity)context));
					}
				})
				.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						errorDialog.dismiss();
					}
				}).create();
	}
	
	
	Handler handler1 = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HttpConnection.DID_SUCCEED:
				progressDialog.dismiss();
				((QuestionViewActivity)context).getDiscussion();
				showDiscussionDialog(questionId);
				break;
			default:
				progressDialog.dismiss();
				postErrorDialog.show();
				break;
			}
			
		}};
}


