package com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone;


import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 项目名称：user-4.5.0-1
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/15 11:25
 * 修改人：meyki-bear
 * 修改时间：2017/6/15 11:25
 * 修改备注：
 */

public interface PtrUIHandler {
    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param frame
     */
    public void onUIReset(PtrFrameLayout frame);

    /**
     * prepare for loading
     *
     * @param frame
     */
    public void onUIRefreshPrepare(PtrFrameLayout frame);

    /**
     * perform refreshing UI
     */
    public void onUIRefreshBegin(PtrFrameLayout frame);

    /**
     * perform UI after refresh
     */
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader);

    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);
}
