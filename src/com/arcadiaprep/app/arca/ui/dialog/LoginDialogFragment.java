package com.arcadiaprep.app.arca.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.arcadiaprep.app.arca.R;

public class LoginDialogFragment extends DialogFragment{

	 /**
     * Create a new instance of MyDialogFragment, providing "question_id"
     * as an argument.
     */
   public static LoginDialogFragment newInstance() {
	   LoginDialogFragment f = new LoginDialogFragment();

//        // Supply question_id input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("question_id", question_id);
//        f.setArguments(args);

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
        View v = inflater.inflate(R.layout.login, container, false);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.y = 50;
        getDialog().getWindow().setAttributes(p);
        
        ImageButton btnClose = (ImageButton) v.findViewById(R.id.imgClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
        
//        lp.x = 100; // 新位置X坐标
//        lp.y = 100; // 新位置Y坐标
//        lp.width = 300; // 宽度
//        lp.height = 300; // 高度
//        lp.alpha = 0.7f; // 透明度

        
        
        return v;
    }

}
