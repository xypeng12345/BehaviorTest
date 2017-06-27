package com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xyp.meyki_bear.behaviortest.R;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 项目名称：euser4.0.0
 * 类描述：
 * 创建人：meyki-bear
 * 创建时间：2016/11/17 14:55
 * 修改人：meyki-bear
 * 修改时间：2016/11/17 14:55
 * 修改备注：
 */

public class PtrHeaderView extends RelativeLayout implements PtrUIHandler {

    private View contentView;
    private ImageView ivLoading;
    private ImageView ivPull;
    private TextView tvHint;
    private RotateAnimation loadingAnim;
    private RotateAnimation pullAnim;
    private RotateAnimation reverPullAnim;
    public PtrHeaderView(Context context) {
        super(context);
        initView(context);

    }

    private void initView(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.ptr_header, this, false);
        addView(contentView);
        ivLoading = (ImageView) findViewById(R.id.refresh_loading);
        tvHint = (TextView) findViewById(R.id.refresh_text);
        ivPull = (ImageView) findViewById(R.id.refresh_begin);


        loadingAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        loadingAnim.setInterpolator(new LinearInterpolator());
        loadingAnim.setDuration(1000);
        loadingAnim.setRepeatCount(RotateAnimation.INFINITE);
        loadingAnim.setRepeatMode(RotateAnimation.RESTART);

        pullAnim = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        pullAnim.setFillAfter(true);
        pullAnim.setDuration(200);

        reverPullAnim = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        reverPullAnim.setFillAfter(true);
        reverPullAnim.setDuration(200);
    }


    public PtrHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PtrHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }



    //================================================================PtrHandler的实现
    /**
     * 彻底释放完成才走这个回调
     *
     * @param frame
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    /**
     * 准备刷新
     *
     * @param frame
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        tvHint.setText("下拉刷新");
        ivLoading.setVisibility(View.GONE);
        ivLoading.clearAnimation();
        ivPull.setVisibility(View.VISIBLE);
    }

    /**
     * 开始刷新
     *
     * @param frame
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        tvHint.setText(R.string.xlistview_header_hint_loading);
        ivLoading.setVisibility(View.VISIBLE);
        ivLoading.clearAnimation();
        ivLoading.startAnimation(loadingAnim);
        ivPull.setVisibility(View.GONE);
        ivPull.clearAnimation();
    }

    /**
     * 刷新完成
     *
     * @param frame
     * @param isHeader
     */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {
        if (!isHeader) {
            return;
        }
        tvHint.setText("刷新完成");
        ivLoading.setVisibility(View.GONE);
        ivLoading.clearAnimation();
        ivPull.setVisibility(View.GONE);
        ivPull.clearAnimation();
    }

    /**
     * 刷新状态改变
     *
     * @param frame
     * @param isUnderTouch
     * @param status
     * @param ptrIndicator
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
                if (ivPull != null) {
                    ivPull.clearAnimation();
                    ivPull.startAnimation(reverPullAnim);
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
                if (ivPull != null) {
                    ivPull.clearAnimation();
                    ivPull.startAnimation(pullAnim);
                }
            }
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        tvHint.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            tvHint.setText("松开刷新");
        } else {
            tvHint.setText("下拉刷新");
        }
    }
    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            tvHint.setVisibility(VISIBLE);
            tvHint.setText("松开刷新");
        }
    }
}
