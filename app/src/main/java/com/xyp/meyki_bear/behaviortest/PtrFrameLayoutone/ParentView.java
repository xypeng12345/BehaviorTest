package com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 项目名称：BehaviorTest
 * 类描述：嵌套滑动父布局demo，无实际效用
 * 创建人：xyp
 * 创建时间：2017/6/26 14:45
 * 修改人：meyki-bear
 * 修改时间：2017/6/26 14:45
 * 修改备注：
 */

public class ParentView extends ViewGroup implements NestedScrollingParent {
    private NestedScrollingParentHelper helper;

    public ParentView(Context context) {
        this(context, null);
    }

    public ParentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /**
     * 开始嵌套滑动事件，遍历询问子控件是否要嵌套滑动，如果有一个子控件支持，则返回false,如果没有支持则返回false
     *
     * @param child            比方说一个控件View1开始嵌套滑动了，那target是View1,而child就是view1的parent(简称vp)
     *                         但如果View1就parentView(之类支持嵌套滑动的View)，那么在vp的这个方法中child与target都是view1
     *                         ，具体原因会看源码解释
     * @param target           发起嵌套事件的View
     * @param nestedScrollAxes 嵌套事件的方向
     * @return
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //遍历所有子控件，调用子控件的嵌套开始嵌套滑动事件，看有没有子控件支持
        boolean result = false;
        for (int i = 0; i < getChildCount(); i++) {
            View parentChild = getChildAt(i);
            if (target == parentChild) { //为了防止无限递归，如果parentChild就是target就跳过
                continue;
            }
            result = ViewCompat.startNestedScroll(parentChild, nestedScrollAxes);
            if (result) { //只要有一个控件支持嵌套滑动，那么就不用遍历后面的控件了
                break;
            }
        }
        return false;
    }

    /**
     * 在接受嵌套滑动之后执行的回调，在onStartNestedScroll返回true后执行，用于更新嵌套事件的方向
     *
     * @param child            比方说一个控件View1开始嵌套滑动了，那target是View1,而child就是view1的parent(简称vp)
     *                         但如果View1就parentView(之类支持嵌套滑动的View)，那么在vp的这个方法中child与target都是view1
     *                         ，具体原因会看源码解释
     * @param target           同上
     * @param nestedScrollAxes 嵌套事件的滑动方向
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        helper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }


    /**
     * 停止滑动,嵌套滑动事件已经停止
     *
     * @param target
     */
    @Override
    public void onStopNestedScroll(View target) {
        helper.onStopNestedScroll(target);
    }

    /**
     * 目标控件嵌套事件处理后的回调
     *
     * @param target       目标控件
     * @param dxConsumed   已经消耗掉的dx
     * @param dyConsumed   已经消耗掉的dy
     * @param dxUnconsumed 剩余dx
     * @param dyUnconsumed 剩余dy
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int[] windowOffset = new int[2];
        for (int i = 0; i < getChildCount(); i++) {
            View childParent = getChildAt(i);
            windowOffset[0] = windowOffset[1] = 0;
            if (target == childParent) { //防止递归调用
                continue;
            }
            ViewCompat.dispatchNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, windowOffset);
        }
    }

    /**
     * 嵌套事件开始前的回调
     *
     * @param target   发起事件的控件
     * @param dx       将要滑动的dx
     * @param dy       将要滑动的dy
     * @param consumed 二维数组，存储消耗掉的dx与dy,方便target进行剩余距离的处理
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int[] windowOffset = new int[2];
        for (int i = 0; i < getChildCount(); i++) {
            View childParent = getChildAt(i);
            windowOffset[0] = windowOffset[1] = 0;
            if (target == childParent) { //防止递归调用
                continue;
            }
            ViewCompat.dispatchNestedPreScroll(childParent, dx, dy, consumed, windowOffset);
        }
    }

    /**
     * 快速滑动事件发生后的回调，注意，这个是事件发生后的回调，不是快速滑动事件结束的回调，可以理解为控件开始处理快速滑动后就走了这个回调
     *
     * @param target    发起嵌套滑动事件的控件
     * @param velocityX x轴的滑动速度
     * @param velocityY y轴的滑动速度
     * @param consumed  是否消耗了这个快速滑动事件
     * @return
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        int[] windowOffset = new int[2];
        boolean result = false;
        for (int i = 0; i < getChildCount(); i++) {
            View childParent = getChildAt(i);
            windowOffset[0] = windowOffset[1] = 0;
            if (target == childParent) { //防止递归调用
                continue;
            }
            result = ViewCompat.dispatchNestedFling(childParent, velocityX, velocityY, consumed);
        }
        return result;
    }

    /**
     * 快速滑动事件发生前的回调
     *
     * @param target    发起嵌套滑动事件的控件
     * @param velocityX x轴的滑动速度
     * @param velocityY y轴的滑动速度
     * @return
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        boolean result = false;
        for (int i = 0; i < getChildCount(); i++) {
            View childParent = getChildAt(i);
            if (target == childParent) { //防止递归调用
                continue;
            }
            result = ViewCompat.dispatchNestedFling(childParent, velocityX, velocityY,result);
        }
        return result;
    }

    /**
     * 获取当前滑动事件的滑动方向，在onNestedScrollAccepted后调用获取准确值
     *
     * @return
     */
    @Override
    public int getNestedScrollAxes() {
        return helper.getNestedScrollAxes();
    }
}
