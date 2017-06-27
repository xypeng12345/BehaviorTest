package com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.View;

/**
 * 项目名称：BehaviorTest
 * 类描述：嵌套滑动的实例View，没有实际作用
 * 创建人：xyp
 * 创建时间：2017/6/26 11:20
 * 修改人：meyki-bear
 * 修改时间：2017/6/26 11:20
 * 修改备注：
 */

public class DemoView extends View implements NestedScrollingChild {
    private NestedScrollingChildHelper helper;

    public DemoView(Context context) {
        this(context, null);
    }

    public DemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = new NestedScrollingChildHelper(this);
    }

    /**
     * 设置是否支持嵌套滑动
     *
     * @param enabled
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        helper.setNestedScrollingEnabled(enabled);
    }

    /**
     * 是否支持嵌套滑动
     *
     * @return
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return helper.isNestedScrollingEnabled();
    }

    /**
     * 开始嵌套滑动了,他会调用父布局的这个方法，递归向上查找支持这个方向嵌套滑动的父布局
     *
     * @param axes 要嵌套滑动的方向 SCROLL_AXIS_NONE 无方向，SCROLL_AXIS_HORIZONTAL 水平方向
     *             SCROLL_AXIS_VERTICAL 竖直方向，具体可以看helper的注释。
     * @return
     */
    @Override
    public boolean startNestedScroll(int axes) {
        return helper.startNestedScroll(axes);
    }


    /**
     * 停止嵌套滑动了
     */
    @Override
    public void stopNestedScroll() {
        helper.stopNestedScroll();
    }

    /**
     * 是否有支持嵌套滑动的父布局，这个值是在调用startNestedScroll确定的，控件先开始嵌套滑动寻找支持嵌套滑动的父布局
     * 如果有，就会继续后续的嵌套滑动处理
     *
     * @return
     */
    @Override
    public boolean hasNestedScrollingParent() {
        return helper.hasNestedScrollingParent();
    }


    /**
     * 控件滑动完了之后调用
     *
     * @param dxConsumed     消耗掉的dx
     * @param dyConsumed     消耗掉的dy
     * @param dxUnconsumed   剩下的dx
     * @param dyUnconsumed   剩下的dy
     * @param offsetInWindow 由于控件滑动导致控件的相对位置发生的变化尺寸 一个二维数组 0是dx，1是dy
     * @return
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return helper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     * 控件滑动前调用
     *
     * @param dx
     * @param dy
     * @param consumed
     * @param offsetInWindow
     * @return
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return helper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    /**
     * 控件快速滑动后调用
     *
     * @param velocityX X轴的滑动速度
     * @param velocityY Y轴的滑动速度
     * @param consumed  true表示这个事件已经被完全使用快速滑动效果，false表示没有完全使用
     * @return
     */
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return helper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    /**
     * 控件快速滑动前调用
     *
     * @param velocityX X轴的滑动速度
     * @param velocityY Y轴的滑动速度
     * @return 如果已经完全消耗掉了滑动事件，则返回true，如果没有，返回false
     */
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return helper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
