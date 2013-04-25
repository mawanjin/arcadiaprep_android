package com.arcadiaprep.app.arca.ui.dialog;

import android.app.DialogFragment;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.SystemService;

public class DiscussionShowImageDialogFragment extends DialogFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}

	ImageView img;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		resize();
	}

	private void resize() {
		int width = ((int)(SystemService.getScreenWith(getActivity().getWindowManager())*0.8));
		int height = (int)(width*0.75);
		LayoutParams params = new LayoutParams(width,height);
		img.setLayoutParams(params);
	}

	Drawable d;
	
	public DiscussionShowImageDialogFragment(){
	}
	
	private DiscussionShowImageDialogFragment(Drawable d){
		this.d = d;
	}
	
	public static DiscussionShowImageDialogFragment newInstance(Drawable d) {
		DiscussionShowImageDialogFragment f = new DiscussionShowImageDialogFragment(d);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	
    	
		View v = inflater.inflate(R.layout.discussion_show_image, container, false);
		
		img = (ImageView) v.findViewById(R.id.img);
		resize();
		
		img.setImageDrawable(d);
		
		img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		
		return v;
	}
}
