package com.xyp.meyki_bear.behaviortest;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone.PtrFameLayout;

/**
 * 项目名称：BehaviorTest
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/16 15:36
 * 修改人：meyki-bear
 * 修改时间：2017/6/16 15:36
 * 修改备注：
 */

public class MyLayout extends LinearLayout implements NestedScrollingParent {
    private boolean disallowInterceptTouchEvent = false;

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private NestedScrollingParentHelper parentHelper;
    private View scrollView;
    private View targetView;
    private Scroller scroller;

    public MyLayout(Context context) {
        this(context, null);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parentHelper = new NestedScrollingParentHelper(this);
        scroller = new Scroller(context);
    }

    public void scrollToTop() {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        scroller.startScroll(scrollX, scrollY, -scrollX, -scrollY);
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        scrollView = findViewById(R.id.my_scroll_view);
        targetView = findViewById(R.id.target_view);
    }

    private float lastX;
    private float lastY;
    private boolean isOutTarget;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private boolean isAllowScroll = false;

    public void setScroll(boolean isAllowScroll) {
        this.isAllowScroll = isAllowScroll;
        changeHeight();
    }

    public void changeHeight() {
        if (!isAllowScroll) {
            ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
            layoutParams.height = targetViewHeight + getScrollY();
            targetView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
            layoutParams.height = targetViewHeight + scrollViewHeight;
            targetView.setLayoutParams(layoutParams);
        }
    }

    private boolean isScroll;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final MotionEvent vtev = MotionEvent.obtain(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                int top = targetView.getTop() - getScrollY();
                if (ev.getY() < top) {
                    isOutTarget = true;
                } else {
                    isOutTarget = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutTarget || disallowInterceptTouchEvent || !isAllowScroll) {
                    break;
                }
                float x = ev.getX();
                float y = ev.getY();
                if (lastY - y != 0&&Math.abs(lastY-y)>Math.abs(lastX-x)) {
                    onScroll(lastX, lastY, x, y);
                    lastX = x;
                    lastY = y;
                    isScroll = true;
                    return true;
                }else{
                    isScroll = false;
                }

            case MotionEvent.ACTION_UP:
                if (isScroll) {
                    isScroll=false;
                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
                isScroll=false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    private void onScroll(float lastX, float lastY, float currX, float currY) {
        float distanceY = lastY - currY;//滑动方向与手指方向相反
        int moveY = (int) distanceY;
        int top = scrollView.getTop() - getScrollY();
        int bottom = scrollView.getBottom() - getScrollY();
        if (distanceY > 0) { //上滑
            if (bottom > 0) { //如果bottom已经小于0了，则不在滑动
                if (bottom - distanceY < 0) { //滑动后下边小于0，则最多滑动bottom的距离
                    moveY = bottom;
                }
                scrollBy(0, moveY);
            }
        } else { //下滑
            if (top < 0) {
                if (top - distanceY > 0) { //一旦滑动后top超过0，则最多滑动top本身的距离
                    moveY = top;
                }
                scrollBy(0, moveY);
            }
        }
    }

    /**
     * 只能记录一次onMeasure的结果，因为下拉刷新控件会重写布局的高，这会导致布局尺寸计算错误
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private int targetViewHeight = 0;
    private int scrollViewHeight = 0;
    private int heightMeasureSpec = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //部分手机测量会出现第一次测量不准确的BUG，但是setLayoutParams又会影响getMeasureHeight的值，
        // 如果每次onMeasure都重新获取的话就会倒置measureHeight不停的变高，但如果只在第一次赋值又会导致赋值不准确，
        // 所以只有在测量模式发生改变即父布局不清楚子布局尺寸的时候重新赋值
        if (this.heightMeasureSpec != heightMeasureSpec) {
            scrollViewHeight = scrollView.getMeasuredHeight();
            targetViewHeight = targetView.getMeasuredHeight();
            this.heightMeasureSpec = heightMeasureSpec;
        }
        ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
        if (isAllowScroll) {//ViewPager滑动时会不断调用父类的onMeasure导致控件高度重新绘制，所以如果高度定死就不允许重新绘制了
            layoutParams.height = targetViewHeight + scrollViewHeight;
        }
    }

    //开始滑动之前，判断子View有没有支持该方向嵌套滑动，如果不支持则无效
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean result = false;
        if (!isAllowScroll) {
            return false;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View moveChild = getChildAt(i);
            if (moveChild == target) { //如果是同一个View，则不必继续判断了
                continue;
            }
            result = ViewCompat.startNestedScroll(moveChild, nestedScrollAxes);
            if (result) { //如果有一个支持了嵌套滑动，则跳出循环
                break;
            }
        }
        return result;
    }

    //在onStartNestedScroll之后调用，表名这个滑动方向已经有控件受理了,至于参数，一会断点跟踪
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        parentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    /**
     * 当目标View的嵌套滑动结束时调用
     *
     * @param target
     */
    public void onStopNestedScroll(View target) {
        parentHelper.onStopNestedScroll(target);
    }

    /**
     * 嵌套滑动后
     *
     * @param target       目标View
     * @param dxConsumed   消耗的距离X
     * @param dyConsumed   消耗的距离Y
     * @param dxUnconsumed 未消耗的距离X
     * @param dyUnconsumed 未消耗的距离Y
     */
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        int[] offsetInWindow = new int[2];
        for (int i = 0; i < getChildCount(); i++) {
            View moveChild = getChildAt(i);
            if (moveChild == target) { //如果是同一个View，则不必继续判断了
                continue;
            }
            //让控件在滑动后使用
            ViewCompat.dispatchNestedScroll(moveChild, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
    }

    /**
     * 目标开始滑动之前
     *
     * @param target
     * @param dx       要滑动的距离X
     * @param dy       要滑动的距离Y
     * @param consumed 使用了多少距离，就把多少距离放进来
     */
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int[] offsetInWindow = new int[2];
        for (int i = 0; i < getChildCount(); i++) {
            View moveChild = getChildAt(i);
            if (moveChild == target) { //如果是同一个View，则不必继续判断了
                continue;
            }
            if (target instanceof PtrFameLayout && moveChild instanceof MyBarView) { //如果是下拉刷新控件，则做一个特殊标记
                consumed[0] = Integer.MAX_VALUE;
                consumed[1] = Integer.MAX_VALUE;
                ViewCompat.dispatchNestedPreScroll(moveChild, dx, dy, consumed, offsetInWindow);
            } else if (moveChild instanceof PtrFameLayout && target instanceof RecyclerView) {
                continue;
            } else {
                ViewCompat.dispatchNestedPreScroll(moveChild, dx, dy, consumed, offsetInWindow);
                dy -= consumed[1];
                dx -= consumed[0];
            }
        }
    }


    /**
     * 控件抛滑之后剩下的速度
     *
     * @param target
     * @param velocityX
     * @param velocityY
     * @param consumed
     * @return
     */
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    /**
     * 控件抛滑之前
     *
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) { //快速滑动前先把布局滑到顶
        if (velocityY > 0) { //下滑
            View childAt = getChildAt(0);
            int bottom = childAt.getBottom() - getScrollY();
            int top = childAt.getTop() - getScrollY();
            if (bottom > 0) { //如果没到顶
                //int startX, int startY, int dx, int dy
                scroller.startScroll(0, -top, 0, childAt.getHeight() + top);
                invalidate();
                return false;
            }
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}
