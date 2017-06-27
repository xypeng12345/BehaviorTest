package com.xyp.meyki_bear.behaviortest;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 项目名称：BehaviorTest
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/16 16:11
 * 修改人：meyki-bear
 * 修改时间：2017/6/16 16:11
 * 修改备注：
 */

public class MyBarView extends LinearLayout implements NestedScrollingChild {
    private NestedScrollingChildHelper childHelper;
    private int[] offsetInWindow = new int[2];

    public MyBarView(Context context) {
        this(context, null);
    }

    public MyBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        childHelper = new NestedScrollingChildHelper(this);
        childHelper.setNestedScrollingEnabled(true);
    }

    //子嵌套滑动的接口实现
    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return childHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        int moveY = dyUnconsumed;
        ViewGroup parent = (ViewGroup) getParent();
        if (dyUnconsumed > 0) { //如果是上滑,暂时不做逻辑处理，要确保上滑的时候bottom>0
            int bottom = getBottom() - parent.getScrollY();
        } else { //如果是下滑
            int top = getTop() - parent.getScrollY();
            if (top - dyUnconsumed > 0) { //一旦滑动后top超过0，则最多滑动top本身的距离
                moveY = getTop();
            }
            parent.scrollBy(0, moveY);
        }
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed + moveY, dxUnconsumed, dyUnconsumed - moveY, offsetInWindow);
    }

    /**
     * 通过margintTop来造成控件的滑动，下滑就是top-(dy)的marginTop，上滑就是top
     *
     * @param dx
     * @param dy             滑动尺寸，向下为负，向上为正,
     * @param consumed
     * @param offsetInWindow
     * @return
     */
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        int moveY;
        ViewGroup parent = (ViewGroup) getParent();
        if (dy >= 0) { //如果是上滑，则处理，下滑则后处理
            moveY = dy;
            int bottom = getBottom() - parent.getScrollY();
            if (bottom > 0) { //如果bottom已经小于0了，则不在滑动
                if (bottom - dy < 0) { //滑动后下边小于0，则最多滑动bottom的距离
                    moveY = bottom;
                }
                parent.scrollBy(0, moveY);
                consumed[0] = 0;
                consumed[1] = moveY;
            }
        }
        if (dy < 0 && consumed[0] == Integer.MAX_VALUE) { //如果是下滑，则看这个控件是否是下拉刷新控件(ptr会在下拉时拦截recyclerView的事件分发)
            moveY = dy;
            parent = (ViewGroup) getParent();
            int top = getTop() - parent.getScrollY();
            if (top - dy > 0) { //一旦滑动后top超过0，则最多滑动top本身的距离
                moveY = getTop();
            }
            parent.scrollBy(0, moveY);
            consumed[0] = 0;
            consumed[1] = moveY;
        }
        return true;
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return false;
//        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return false;
    }


}
