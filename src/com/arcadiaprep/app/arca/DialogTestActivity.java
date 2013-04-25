package com.arcadiaprep.app.arca;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.ui.dialog.LoginDialogFragment;
import com.arcadiaprep.app.arca.ui.dialog.QuestionDialogFragment;
import com.arcadiaprep.app.arca.ui.fragment.DiscussionDetailFragment;
import com.arcadiaprep.app.arca.ui.fragment.DiscussionReplyFragment;
import com.arcadiaprep.app.arca.ui.fragment.DiscussionUserInfoFragment;
import com.arcadiaprep.app.arca.ui.fragment.GalleryFragment;
import com.arcadiaprep.app.arca.ui.fragment.VideoDialogFragment;

public class DialogTestActivity extends Activity {
	
	View dialogContainer;
	
	Button btnLeft;
	Button btnRight;
	ImageButton ibtnClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.test_dialog);
		
		Button btn = (Button) findViewById(R.id.btnLogin);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
		
		Button btnQuestion = (Button) findViewById(R.id.btnQuestion);
		btnQuestion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showQuestionDialog();
			}
		});
		
		Button btnDiscussion= (Button) findViewById(R.id.btnDiscussion);
		btnDiscussion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDiscussionDialog();
			}
		});
		
		dialogContainer = findViewById(R.id.dialogContainer);
		
		ibtnClose = (ImageButton) findViewById(R.id.ibtnClose);
		ibtnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogContainer.setVisibility(View.GONE);
			}
		});
		
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDiscussionDialog();
			}
		});
		btnRight = (Button) findViewById(R.id.btnRight);
		
		Button btnGallery= (Button) findViewById(R.id.btnGallery);
		btnGallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showGalleryDialog();
			}
		});
		
		Button btnVideo= (Button) findViewById(R.id.btnVideo);
		btnVideo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showVideoDialog();
			}
		});
		
		
		
	}
	
	
	protected void showVideoDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("q_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        VideoDialogFragment newFragment = VideoDialogFragment.newInstance();
        newFragment.show(ft, "q_dialog");			
	}


	public void showDiscussionUserInfoDialog() {
		resetButton(2);
		dialogContainer.setVisibility(View.VISIBLE);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		
        Fragment newFragment = DiscussionUserInfoFragment.newInstance(1);
        ft.replace(R.id.dialog, newFragment);
        ft.commit();
	}
	
	
	public void showDiscussionReplyDialog() {
		resetButton(1);
		dialogContainer.setVisibility(View.VISIBLE);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		
        Fragment newFragment = DiscussionReplyFragment.newInstance(1);
        ft.replace(R.id.dialog, newFragment);
        ft.commit();
	}
	
	public void showDiscussionDialog() {
		resetButton(0);
		dialogContainer.setVisibility(View.VISIBLE);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment newFragment = DiscussionDetailFragment.newInstance(1,null);
        ft.replace(R.id.dialog, newFragment);
//        ft.add(R.id.dialog, newFragment);
        ft.commit();
	}
	
	protected void showQuestionDialog() {
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("q_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        QuestionDialogFragment newFragment = QuestionDialogFragment.newInstance(true,null);
        newFragment.show(ft, "q_dialog");		
	}

	void showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        LoginDialogFragment newFragment = LoginDialogFragment.newInstance();
        
        newFragment.show(ft, "dialog");
    }
	
	private void resetButton(int which){
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
	
	protected void showGalleryDialog() {
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("q_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        GalleryFragment newFragment = GalleryFragment.newInstance(ConstantSystem.GET_WORK_SPACE_SHOT_IMAGE_FOLDER().getPath());
        
        newFragment.show(ft, "q_dialog");		
	}

}
