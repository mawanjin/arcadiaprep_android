package com.arcadiaprep.app.arca;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.ui.dialog.DiscussionDialogFragment;
import com.arcadiaprep.app.arca.vo.DiscussionVO;

@SuppressLint({ "NewApi", "NewApi" })
public class DiscussionDialog extends Activity{
    int mStackLevel = 0;
    DiscussionService discussionService;
    List<DiscussionVO> discussions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion);
        

        // Watch for button clicks.
        ImageButton blackX = (ImageButton)findViewById(R.id.btnBack);
        blackX.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStackLevel);
    }


    void showDialog() {
        mStackLevel++;
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
        DialogFragment newFragment = DiscussionDialogFragment.newInstance(mStackLevel);
        newFragment.show(ft, "dialog");
    }



}
