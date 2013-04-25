package com.arcadiaprep.app.arca.ui.dialog;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.ui.list.ListExamDiscussionAdapter;
import com.arcadiaprep.app.arca.util.ListHeightUtils;
import com.arcadiaprep.app.arca.vo.DiscussionVO;

@SuppressLint("NewApi")
public class DiscussionDialogFragment extends DialogFragment{

	private DiscussionService discussionService;
	private List<DiscussionVO> discussions;
	

    /**
     * Create a new instance of MyDialogFragment, providing "question_id"
     * as an argument.
     */
   public static DiscussionDialogFragment newInstance(int question_id) {
    	DiscussionDialogFragment f = new DiscussionDialogFragment();

        // Supply question_id input as an argument.
        Bundle args = new Bundle();
        args.putInt("question_id", question_id);
        f.setArguments(args);

        return f;
    }
   
   public static DiscussionDialogFragment newInstance(List<DiscussionVO> discussions) {
   		DiscussionDialogFragment f = new DiscussionDialogFragment();
   		f.discussions = discussions;
       return f;
   }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(discussions==null){
        	discussionService = DiscussionService.getInstance(getActivity());
//            discussions = discussionService.findDiscussionsByQuestionid(getArguments().getInt("question_id"));
        }
        
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discussion, container, false);
        
        TextView tv = (TextView) v.findViewById(R.id.txtCommentCount);
        tv.setText(discussions.size()+" comments");
        
        ListView list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(new ListExamDiscussionAdapter(getActivity(),discussions,null));
        ListHeightUtils.setListViewHeightBasedOnChildren(list);
        
        return v;
    }
}
