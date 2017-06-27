package com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 项目名称：user-4.5.0-1
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/15 15:14
 * 修改人：meyki-bear
 * 修改时间：2017/6/15 15:14
 * 修改备注：
 */

public class TestFramLayout extends PtrClassicFrameLayout implements NestedScrollingChild {
    private int[] mScrollConsumed = new int[2];
    private int[] mScrollOffset = new int[2];

    private NestedScrollingChildHelper childHelper;

    public TestFramLayout(Context context) {
        this(context, null);
    }

    public TestFramLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestFramLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        childHelper=new NestedScrollingChildHelper(this);
    }

    private float lastX;
    private float lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        float moveX = x - lastX;
        float moveY = y - lastY;
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                break;
            case MotionEvent.ACTION_DOWN:
                int axes = ViewCompat.SCROLL_AXIS_VERTICAL;
                startNestedScroll(axes);
                break;
            case MotionEvent.ACTION_MOVE:
                if (dispatchNestedPreScroll((int)moveX,(int)moveY,mScrollConsumed,mScrollOffset)) {//如果返回true就说明事件自己拦截了
                    super.dispatchTouchEvent(e);
                }
                break;
        }
        return super.dispatchTouchEvent(e);
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return childHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        //在这里消耗么
        Log.d("yupeng", "触发了这个方法" + dyConsumed);
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
