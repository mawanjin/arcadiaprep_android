package com.arcadiaprep.app.arca.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.SystemService;

public class DiscussionReplyFragment extends Fragment {
	
	
	private final int ACTIVITY_SELECT_IMAGE=1001;
	
	private TextView txtAttach,txtRemove;

	private AlertDialog  dialogAppChoose;
	private View dialogAppChooseView;
	private ImageView imgAttach;
	
	private int screenWidth,screenHeight;
	

	public static DiscussionReplyFragment newInstance(int index) {
		DiscussionReplyFragment details = new DiscussionReplyFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		details.setArguments(args);
		return details;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.discussion_reply_fragment, container, false);
		screenHeight = SystemService.getScreenHeight(getActivity().getWindowManager());
		screenWidth = SystemService.getScreenWith(getActivity().getWindowManager());
		
		
		txtAttach = (TextView) v.findViewById(R.id.txtAttach);
		imgAttach = (ImageView) v.findViewById(R.id.imgAttach);
		txtRemove = (TextView) v.findViewById(R.id.txtRemove);
		
		LayoutParams params = new LayoutParams((int)(screenWidth/2),LayoutParams.FILL_PARENT );
		params.gravity=Gravity.CENTER;
		imgAttach.setLayoutParams(params);
		
		
		dialogAppChooseView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_discussion_post_choosen, null);
		
		registerListener();
		
		initDialog();
		return v;
	}
	
	private void initDialog() {
//		make a app choose dialog
		dialogAppChoose = new AlertDialog.Builder(getActivity(),R.style.dialog)
		.setView(dialogAppChooseView)
		.create();	
		
		Button btnWorkShot = (Button) dialogAppChooseView.findViewById(R.id.btnWorkShot);
		btnWorkShot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
			}
		});
		
		
		Button btnAlbum = (Button) dialogAppChooseView.findViewById(R.id.btnAlbum);
		btnAlbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
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
				
			}
		});
		
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
			case ACTIVITY_SELECT_IMAGE:
				if(resultCode==getActivity().RESULT_OK){
					dialogAppChoose.dismiss();
					Uri selectedImage = data.getData();
		            String[] filePathColumn = {MediaStore.Images.Media.DATA};

		            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String filePath = cursor.getString(columnIndex);
		            cursor.close();

		            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
		            imgAttach.setImageBitmap(yourSelectedImage);
				}
			break;
		}
	}
}
