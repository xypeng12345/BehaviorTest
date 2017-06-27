package com.xyp.meyki_bear.behaviortest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xyp.meyki_bear.behaviortest.PtrFrameLayoutone.PtreClassicFrameLayout;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class MainActivity extends AppCompatActivity implements PtrHandler2 {
    private RecyclerView rv;
    private MyLayout ml;
    private PtreClassicFrameLayout swipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv1);
        ml = (MyLayout) findViewById(R.id.ml);
        ml.post(new Runnable() {
            @Override
            public void run() {
                ml.setScroll(true);
            }
        });
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        swipLayout = (PtreClassicFrameLayout) findViewById(R.id.target_view);
        swipLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        swipLayout.setPtrHandler(this);
        MyAdapter adapter = new MyAdapter(this, list);
        final LinearLayoutManager layout = new LinearLayoutManager(this);
        rv.setLayoutManager(layout);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastState;
            private int dy;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (lastState == SCROLL_STATE_SETTLING) { //上一个状态是快速滑动状态，且当前状态为结束
                    switch (newState) {
                        case SCROLL_STATE_IDLE:
                            int visiblePosition = layout.findFirstCompletelyVisibleItemPosition();
                            if (visiblePosition == 0 && dy < 0) {
                                View childAt = rv.getChildAt(0);
                                if (childAt.getTop() == 0) {
                                    ml.scrollToTop();
                                }
                            }
                            break;
                    }
                }
                lastState = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                this.dy = dy;
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler2.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        swipLayout.refreshComplete1();
    }

    @Override
    public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
    }

    @Override
    public void onLoadMoreBegin(PtrFrameLayout frame) {
        swipLayout.refreshComplete1();
    }
}
