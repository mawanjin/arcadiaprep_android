package com.arcadiaprep.app.arca.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;

public class DiscussionUserInfoFragment extends Fragment{
	
	public static DiscussionUserInfoFragment newInstance(int index) {
		DiscussionUserInfoFragment details = new DiscussionUserInfoFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		details.setArguments(args);
		return details;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.discussion_user_fragment, container, false);
		
		
		ImageView imgPortrait = (ImageView) v.findViewById(R.id.imgPortrait);
		TextView txtUname = (TextView) v.findViewById(R.id.txtUname);
		TextView txtUnameSmall = (TextView) v.findViewById(R.id.txtUnameSmall);
		TextView txtPost = (TextView) v.findViewById(R.id.txtPost);
		TextView txtLocation = (TextView) v.findViewById(R.id.txtLocation);
		TextView txtAboutme = (TextView) v.findViewById(R.id.txtAboutme);

//		DiscussionUserVO user = DiscussionService.getInstance(this).getUserInfo(getIntent().getExtras().getInt("userId"));
//		DiscussionUserVO user = DiscussionService.getInstance(getActivity()).getUserInfo(1);
		
//		txtUname.setText(user.getUname());
//		txtUnameSmall.setText(user.getUname());
//		txtPost.setText(user.getExtra());
//		txtLocation.setText(user.getLocation());
//		txtAboutme.setText(user.getAboutme());
//		imgPortrait.setContentDescription(user.getHead());
		return v;
	}

}
