package com.arcadiaprep.app.arca.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcadiaprep.app.arca.QuestionViewActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.service.IDiscussionFragment;
import com.arcadiaprep.app.arca.ui.list.ListExamDiscussionFragmentAdapter;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;
import com.arcadiaprep.app.arca.vo.DiscussionVO;

public class DiscussionDetailFragment extends Fragment implements Serializable{
	
	

	private int index;
	private IDiscussionFragment activity;
	
	
	public DiscussionDetailFragment(){
		
	}
	
	public void setListener(IDiscussionFragment impl){
		activity = impl;
	}


	public static DiscussionDetailFragment newInstance(int index,IDiscussionFragment activity) {
		DiscussionDetailFragment details = new DiscussionDetailFragment();
		Bundle b = new Bundle();
		b.putSerializable("activity", activity);
		b.putInt("index", 111);
        details.setArguments(b);
		
		return details;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		Bundle args = getArguments();
        if (args != null) {
        	activity = (IDiscussionFragment) args.getSerializable("activity");
        	index = args.getInt("args");
        	
        }
		View v = inflater.inflate(R.layout.discussion_exam, container, false);
		
		
		TextView post = (TextView) v.findViewById(R.id.txtPost);
		post.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				activity.showDiscussionReplyDialog();
			}
		});
		
		DiscussionService discussionService = DiscussionService.getInstance(getActivity());
//		List<DiscussionVO> discussions = discussionService.findDiscussionsByQuestionid(index,null);
		List<DiscussionVO> discussions= new ArrayList<DiscussionVO>();
        TextView tv = (TextView) v.findViewById(R.id.txtCommentCount);
        tv.setText(discussions.size()+" comments");
        
        ListView list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(new ListExamDiscussionFragmentAdapter(getActivity(),discussions,new ListExamDiscussionFragmentAdapter.Callback() {
			
			@Override
			public void onReply(int position) {
//				((QuestionViewActivity)getActivity()).discussionFragment.showDiscussionReplyDialog();
			}

			@Override
			public void onUserInfo(int position,DiscussionUserVO vo ) {
				
				((QuestionViewActivity)getActivity()).discussionFragment.showDiscussionUserInfoDialog(vo);
			}
		}));
		
        return v;
    }

}
