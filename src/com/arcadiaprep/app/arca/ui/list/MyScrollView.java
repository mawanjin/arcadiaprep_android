package com.arcadiaprep.app.arca.ui.list;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * ScrollView
 */
public class MyScrollView extends ScrollView {
    private View inner;
    private static final int DEFAULT_POSITION = -1;
    private float y = DEFAULT_POSITION;
    private Rect normal = new Rect();
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        System.out.println("onTouchEvent");
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            y = ev.getY();
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            if (isNeedAnimation()) {
                animation();
            }
            y = DEFAULT_POSITION;
            break;
        case MotionEvent.ACTION_MOVE:
            final float nowY = ev.getY();
            float preY = y;
            if (isDefaultPosition(y)) {
                preY = nowY;
            }
            int deltaY = (int) (preY - nowY);
            
            scrollBy(0, deltaY);
            y = nowY;
            
            if (isNeedMove()) {
                if (normal.isEmpty()) {
            
                    normal.set(inner.getLeft(), inner.getTop(),
                            inner.getRight(), inner.getBottom());
                }
            
                inner.layout(inner.getLeft(), inner.getTop() - deltaY,
                        inner.getRight(), inner.getBottom() - deltaY);
            }
            break;
        default:
            break;
        }
    }

    private boolean isDefaultPosition(float position) {
        return position == DEFAULT_POSITION;
    }

    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);

        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();

        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }
}
