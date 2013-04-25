package com.arcadiaprep.app.arca.ui.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.arcadiaprep.app.arca.R;

public class DiscussionFragment extends DialogFragment {
	

	public static DiscussionFragment newInstance(int questionId) {
		DiscussionFragment f = new DiscussionFragment();
		Bundle args = new Bundle();
		args.putInt("questionId", questionId);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.discussion_exam_main, container, false);
		
		ImageButton close = (ImageButton) v.findViewById(R.id.ibtnClose);
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				
			}
		});
		
		return v;
	}
}
