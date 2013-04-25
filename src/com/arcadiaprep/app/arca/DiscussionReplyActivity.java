package com.arcadiaprep.app.arca;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.arcadiaprep.app.arca.constants.ConstantDiscussion;
import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.dialog.DiscussionShowImageDialogFragment;
import com.arcadiaprep.app.arca.ui.fragment.GalleryFragment;
import com.arcadiaprep.app.arca.util.http.HttpConnection;
import com.arcadiaprep.app.arca.vo.ImageVO;
import com.arcadiaprep.app.login.common.Login;
import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.model.User;
import com.arcadiaprep.app.scratchpad.component.WorkSpace;

public class DiscussionReplyActivity extends Activity{
	
	private final int ACTIVITY_SELECT_IMAGE=1001;
	
	private ProgressDialog progressDialog;
	
	private Button btnCancel,btnPost;
	private EditText editContent;
	private TextView txtAttach,txtRemove;

	private AlertDialog  dialogAppChoose;
	private AlertDialog postErrorDialog;
	private View dialogAppChooseView;
	private ImageView imgAttach;
	
	private int screenWidth,screenHeight;
	
	private AlertDialog errorDialog;
	
	private int type;
	private int questionId;
	private int discussionId;
	
	private User mUser = null;
	
	private File file;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.discussion_reply);
		
		type = getIntent().getExtras().getInt("type");
		questionId = getIntent().getExtras().getInt("questionId");
		discussionId = getIntent().getExtras().getInt("discussionId");
		
		screenHeight = SystemService.getScreenHeight(getWindowManager());
		screenWidth = SystemService.getScreenWith(getWindowManager());
		
		
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnPost  = (Button) findViewById(R.id.btnPost);
		editContent = (EditText) findViewById(R.id.editContent);
		txtAttach = (TextView) findViewById(R.id.txtAttach);
		imgAttach = (ImageView) findViewById(R.id.imgAttach);
		txtRemove = (TextView) findViewById(R.id.txtRemove);
		
		LayoutParams params = new LayoutParams((int)(screenWidth/2),LayoutParams.FILL_PARENT );
		params.gravity=Gravity.CENTER;
		imgAttach.setLayoutParams(params);
		
		
		dialogAppChooseView = LayoutInflater.from(this).inflate(R.layout.dialog_discussion_post_choosen, null);
		
		registerListener();
		
		initDialog();
		initErrorDialog();
		initPostErrorDialog();
		
	}

	private void initDialog() {
//		make a app choose dialog
		dialogAppChoose = new AlertDialog.Builder(DiscussionReplyActivity.this,R.style.dialog)
		.setView(dialogAppChooseView)
		.create();	
		
		Button btnAlbum = (Button) dialogAppChooseView.findViewById(R.id.btnAlbum);
		Button btnWorkShot = (Button) dialogAppChooseView.findViewById(R.id.btnWorkShot);
		
		btnWorkShot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

//				File file = ConstantSystem.GET_WORK_SPACE_SHOT_IMAGE();
//				if (file.exists()) {
//		            Bitmap yourSelectedImage = BitmapFactory.decodeFile(WorkSpace.COMPOSITE_IMAGE_PATH);
//		            imgAttach.setImageBitmap(yourSelectedImage);
//				} else {
//					dialogAppChoose.dismiss();
//					AlertDialog a = new AlertDialog.Builder(v.getContext()).setMessage("No image there").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							dialogAppChoose.show();
//						}
//					}).create();
//					a.show();
//				}

				File file = ConstantSystem.GET_WORK_SPACE_SHOT_IMAGE_FOLDER();
				if(file==null||file.listFiles() == null || file.listFiles().length==0){
					dialogAppChoose.dismiss();
					AlertDialog a = new AlertDialog.Builder(v.getContext()).setMessage("No image there").setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							dialogAppChoose.show();
						}
					}).create();
					a.show();
				}else{
					dialogAppChoose.dismiss();
					showGalleryDialog();
				}
				 
				
			}
		});
		
		btnAlbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
			}
		});
		
		Button btnCancel = (Button) dialogAppChooseView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogAppChoose.dismiss();	
			}
		});
		
	}

	private void registerListener() {
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnPost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Post
				if ((mUser = UserService.getUser(v.getContext())) == null){
					errorDialog.show();
				}else{
					
					progressDialog = ProgressDialog.show(DiscussionReplyActivity.this, "", "posting");
					progressDialog.setCancelable(true);
					progressDialog.setCanceledOnTouchOutside(false);
					//Post
					switch (type) {
						
						case ConstantDiscussion.POST_DISCUSSION_TYPE_POST_NEW:
							DiscussionService.getInstance(DiscussionReplyActivity.this).postNew(handler1,mUser, editContent.getText().toString(),file);
							break;
						case ConstantDiscussion.POST_DISCUSSION_TYPE_POST_NEW_FOR_GIVEN_QUESTION:
							DiscussionService.getInstance(DiscussionReplyActivity.this).postForAGivenQuestion(handler1,mUser,questionId, editContent.getText().toString(),file);
							break;
						case ConstantDiscussion.POST_DISCUSSION_TYPE_POST_REPLY:
							DiscussionService.getInstance(DiscussionReplyActivity.this).postReply(handler1,mUser,discussionId, editContent.getText().toString(),file);
							break;
						default:
							break;

					}
				}
			}
		});
		
		txtAttach.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialogAppChoose.show();
				
			}
		});
		
		txtRemove.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				imgAttach.setImageBitmap(null);
				file=null;
			}
		});
		
		imgAttach.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(imgAttach.getDrawable()!=null&&file!=null){
					
					FragmentTransaction f = getFragmentManager().beginTransaction();
					if(getFragmentManager().findFragmentByTag("show_img")!=null){
						f.remove(getFragmentManager().findFragmentByTag("show_img"));
					}
					f.add(DiscussionShowImageDialogFragment.newInstance(imgAttach.getDrawable()), "show_img");
					f.commit();
				}
			}
		});
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
			case ACTIVITY_SELECT_IMAGE:
				if(resultCode==RESULT_OK){
					dialogAppChoose.dismiss();
					Uri selectedImage = data.getData();
		            String[] filePathColumn = {MediaStore.Images.Media.DATA};

		            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String filePath = cursor.getString(columnIndex);
		            cursor.close();

		            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
		            setThumbImage(yourSelectedImage,filePath);
		            
		            
				}
			break;
		}
	}
	
	private void setThumbImage(Bitmap yourSelectedImage, String filePath) {
		imgAttach.setImageBitmap(yourSelectedImage);
        
        file = new File(filePath);
        if(!file.exists())file=null;
        
        closeGalleryDialog();
	}

	private void initErrorDialog() {
		errorDialog = new AlertDialog.Builder(this)
				.setTitle("Login Status")
				.setMessage("You need to log into Arcadiaprep.com to post a comment.")
				.setNegativeButton("Login & Post", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						errorDialog.dismiss();
						Login.login(DiscussionReplyActivity.this);
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
				Intent i = new Intent(DiscussionReplyActivity.this,DiscussionActivity.class);
				if(getIntent().getExtras()!=null&&getIntent().getExtras().getString("from")!=null&&getIntent().getExtras().getString("from").equals("question_view")){
					i.putExtra("from", "question_view");
					i.putExtra("question_id", questionId);
				}
				startActivity(i);
				
				finish();
				break;
			default:
				progressDialog.dismiss();
				postErrorDialog.show();
				break;
			}
			
		}};
		
		
		private void initPostErrorDialog() {
			postErrorDialog = new AlertDialog.Builder(this)
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
		
	   protected void closeGalleryDialog(){
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			GalleryFragment prev = (GalleryFragment) getFragmentManager().findFragmentByTag("q_dialog");
	        if (prev != null) {
	        	prev.dismiss();
	            ft.remove(prev);
	        }
		}
		
		protected void showGalleryDialog() {
			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
	        Fragment prev = getFragmentManager().findFragmentByTag("q_dialog");
	        if (prev != null) {
	            ft.remove(prev);
	        }
	        ft.addToBackStack(null);
	        // Create and show the dialog.
	        GalleryFragment newFragment = GalleryFragment.newInstance(ConstantSystem.GET_WORK_SPACE_SHOT_IMAGE_FOLDER().getPath());
	        newFragment.setCallback(new GalleryFragment.Callback() {
				
				@Override
				public void onSelectImage(ImageVO vo) {
					setThumbImage(vo.getBitMap(),vo.getPath());
				}
			});
	        newFragment.show(ft, "q_dialog");		
		}

}
