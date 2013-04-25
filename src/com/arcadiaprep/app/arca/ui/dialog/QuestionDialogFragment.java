package com.arcadiaprep.app.arca.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.arcadiaprep.app.arca.QuestionViewActivity;
import com.arcadiaprep.app.arca.R;

public class QuestionDialogFragment extends DialogFragment{
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		getActivity().setFinishOnTouchOutside(true);
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		
		Dialog d = super.onCreateDialog(savedInstanceState);
		WindowManager.LayoutParams p = d.getWindow().getAttributes();
//		p.width = LayoutParams.WRAP_CONTENT; // 宽度
//		p.height = LayoutParams.WRAP_CONTENT; // 高度
//		p.alpha = 0.8f; // 透明度
		
	    d.getWindow().setAttributes(p);
		return d;
	}

	static ButtonClickCallBack callBack;

	 /**
     * Create a new instance of MyDialogFragment, providing "question_id"
     * as an argument.
     * 
     * @param last To indicate  whether the current question is the last one of the question set. 
     */
   public static QuestionDialogFragment newInstance(boolean last,ButtonClickCallBack _callBack) {
	   QuestionDialogFragment f = new QuestionDialogFragment();

        // Supply question_id input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("last", last);
        f.setArguments(args);
	   callBack = _callBack;

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
        View v = inflater.inflate(R.layout.alert_dialog, container, false);
        
        final Button btnShowCorrect = (Button) v.findViewById(R.id.btnShowCorrect);
        if (QuestionViewActivity.timerOn == true) 
            btnShowCorrect.setVisibility(View.INVISIBLE);
        else  
            btnShowCorrect.setVisibility(View.VISIBLE);
            
		final Button btnNxtQuestion = (Button) v.findViewById(R.id.btnNxtQuestion);
		
		if(getArguments().getBoolean("last")){
			btnNxtQuestion.setText("Finish & Review");
			btnNxtQuestion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					callBack.onNextQuestionButtonClick(true);
					getDialog().dismiss();
				}
			});
		}else{
			btnNxtQuestion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					callBack.onNextQuestionButtonClick(false);
					getDialog().dismiss();
				}
			});
		}
		
		
		
		btnShowCorrect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onShowSolutionButtonClick(getArguments().getBoolean("last"));
				getDialog().dismiss();
			}
		});
		
		
		WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
//		p.width = 450; // 宽度
//		p.height = 250; // 高度
//		p.alpha = 0.8f; // 透明度
//	    getDialog().getWindow().setAttributes(p);
//        lp.x = 100; // 新位置X坐标
//        lp.y = 100; // 新位置Y坐标
       
        
        return v;
    }
    
    public interface ButtonClickCallBack{
    	public void onNextQuestionButtonClick(boolean last);
    	public void onShowSolutionButtonClick(boolean last);
    }

}
