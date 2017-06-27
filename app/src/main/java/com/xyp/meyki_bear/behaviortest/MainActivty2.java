package com.xyp.meyki_bear.behaviortest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * 项目名称：BehaviorTest
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/19 14:02
 * 修改人：meyki-bear
 * 修改时间：2017/6/19 14:02
 * 修改备注：
 */

public class MainActivty2 extends AppCompatActivity {
    private RecyclerView rv;
    private MyLayout ml;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.layout_slide);
        super.onCreate(savedInstanceState);
        rv = (RecyclerView) findViewById(R.id.rv1);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        MyAdapter adapter = new MyAdapter(this, list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }
}
