package com.arcadiaprep.app.arca.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.dialog.DiscussionShowImageDialogFragment;
import com.arcadiaprep.app.arca.ui.fragment.GalleryFragment;
import com.arcadiaprep.app.arca.vo.ImageVO;
import com.arcadiaprep.app.scratchpad.component.WorkSpace;

public class DiscussionReplyView extends LinearLayout{
	private final int ACTIVITY_SELECT_IMAGE=ConstantSystem.ACTIVITY_SELECT_IMAGE;
	Context context;
	public EditText editContent;
	private TextView txtAttach,txtRemove;

	private AlertDialog  dialogAppChoose;
	private View dialogAppChooseView;
	public ImageView imgAttach;
	public String filePath;
	
	private int screenWidth,screenHeight;
	
	Callback callback;
	
	public DiscussionReplyView(Context context,Callback callback) {
		super(context);
		this.context = context;
		this.callback = callback;
		init();
		
	}

	private void init() {
		View v = LayoutInflater.from(context).inflate(
				R.layout.discussion_reply_fragment, null);
		
		
		screenHeight = SystemService.getScreenHeight(((Activity)context).getWindowManager());
		screenWidth = SystemService.getScreenWith(((Activity)context).getWindowManager());
		
		editContent = (EditText) v.findViewById(R.id.editContent);
		txtAttach = (TextView) v.findViewById(R.id.txtAttach);
		imgAttach = (ImageView) v.findViewById(R.id.imgAttach);
		txtRemove = (TextView) v.findViewById(R.id.txtRemove);
		
		LayoutParams params = new LayoutParams((int)(screenWidth/2),LayoutParams.FILL_PARENT );
		params.gravity=Gravity.CENTER;
		imgAttach.setLayoutParams(params);
		
		dialogAppChooseView = LayoutInflater.from(context).inflate(R.layout.dialog_discussion_post_choosen, null);
		registerListener();
		initDialog();
		LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		p.leftMargin=8;
		p.rightMargin=15;
		p.bottomMargin=16;
		
		addView(v, p);
	}
	
	private void initDialog() {
//		make a app choose dialog
		dialogAppChoose = new AlertDialog.Builder(context,R.style.dialog)
		.setView(dialogAppChooseView)
		.create();	
		
		Button btnAlbum = (Button) dialogAppChooseView.findViewById(R.id.btnAlbum);
		Button btnWorkShot = (Button) dialogAppChooseView.findViewById(R.id.btnWorkShot);
		
		btnWorkShot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
//				File file = ConstantSystem.GET_WORK_SPACE_SHOT_IMAGE();
//				if (file.exists()) {
//					dialogAppChoose.dismiss();
//		            Bitmap yourSelectedImage = BitmapFactory.decodeFile(file.getAbsolutePath());
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
				if(file==null||file.listFiles().length==0){
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
				((Activity)context).startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
				dialogAppChoose.dismiss();
			}
		});
		
		Button cancel = (Button) dialogAppChooseView.findViewById(R.id.btnCancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogAppChoose.dismiss();
			}
		});
		
	}
	
	private void registerListener() {
		
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
				callback.onRemoveAttach();
			}
		});
		
		imgAttach.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				if(imgAttach.getDrawable()!=null&&filePath!=null){
					
					FragmentTransaction f = ((Activity)context).getFragmentManager().beginTransaction();
					if(((Activity)context).getFragmentManager().findFragmentByTag("show_img")!=null){
						f.remove(((Activity)context).getFragmentManager().findFragmentByTag("show_img"));
					}
					f.add(DiscussionShowImageDialogFragment.newInstance(imgAttach.getDrawable()), "show_img");
					f.commit();
				}
			}
		});
		
		
		
	}
	
	protected void showGalleryDialog() {
		
		FragmentTransaction ft = ((Activity)context).getFragmentManager().beginTransaction();
		Fragment prev = ((Activity)context).getFragmentManager().findFragmentByTag("q_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        GalleryFragment newFragment = GalleryFragment.newInstance(ConstantSystem.GET_WORK_SPACE_SHOT_IMAGE_FOLDER().getPath());
        newFragment.setCallback(new GalleryFragment.Callback() {
			
        	@Override
			public void onSelectImage(ImageVO vo) {
        		callback.onSelectAttach(vo);
        		imgAttach.setImageBitmap(vo.getBitMap());
        		closeGalleryDialog();
			}
		});
        newFragment.show(ft, "q_dialog");		
	}
	
	public interface Callback{
		public void onRemoveAttach();
		public void onSelectAttach(ImageVO vo);
	}
	
	protected void closeGalleryDialog(){
		FragmentTransaction ft = ((Activity)context).getFragmentManager().beginTransaction();
		GalleryFragment prev = (GalleryFragment) ((Activity)context).getFragmentManager().findFragmentByTag("q_dialog");
		
        if (prev != null) {
        	prev.dismiss();
            ft.remove(prev);
        }
	}
	
}


