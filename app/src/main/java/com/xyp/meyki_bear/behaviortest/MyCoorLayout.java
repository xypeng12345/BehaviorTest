package com.xyp.meyki_bear.behaviortest;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * 项目名称：BehaviorTest
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/19 15:02
 * 修改人：meyki-bear
 * 修改时间：2017/6/19 15:02
 * 修改备注：
 */

public class MyCoorLayout extends CoordinatorLayout {
    public MyCoorLayout(Context context) {
        super(context);
    }

    public MyCoorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCoorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }
}
