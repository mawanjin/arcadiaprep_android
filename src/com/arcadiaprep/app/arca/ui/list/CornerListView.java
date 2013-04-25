package com.arcadiaprep.app.arca.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arcadiaprep.app.arca.R;

public class CornerListView extends ListView {
	public CornerListView(Context context) {
        super(context);
    }
 
    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.drawable.list_item_line);
    }
 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);
 
                if (itemnum == AdapterView.INVALID_POSITION){
                    break;
                } else {
                        if (itemnum == 0){
                                if (itemnum == (getAdapter().getCount()-1)) {
                                    setSelector(R.drawable.corner_list_single_item);
                                } else {
                                    setSelector(R.drawable.corner_list_first_item);
                                }
                        } else if (itemnum==(getAdapter().getCount()-1)){
                            setSelector(R.drawable.corner_list_last_item);
                        } else {
                            setSelector(R.drawable.corner_list_item);
                        }
                }
                break;
        case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
