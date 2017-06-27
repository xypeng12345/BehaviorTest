package com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 项目名称：user-4.5.0-1
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/15 14:32
 * 修改人：meyki-bear
 * 修改时间：2017/6/15 14:32
 * 修改备注：
 */

public class PtreClassicFrameLayout extends PtrFameLayout{
    private PtrHeaderView mPtrClassicHeader;
    private PtrFooterView mPtrClassicFooter;

    public PtreClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtreClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtreClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PtrHeaderView(getContext());

        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
        mPtrClassicFooter = new PtrFooterView(getContext());
        setFooterView(mPtrClassicFooter);
        addPtrUIHandler(mPtrClassicFooter);
    }

    public PtrHeaderView getHeader() {
        return mPtrClassicHeader;
    }


}
