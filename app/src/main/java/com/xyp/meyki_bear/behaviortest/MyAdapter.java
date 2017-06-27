package com.xyp.meyki_bear.behaviortest;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

/**
 * 项目名称：BehaviorTest
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/6/16 16:05
 * 修改人：meyki-bear
 * 修改时间：2017/6/16 16:05
 * 修改备注：
 */

public class MyAdapter extends MyBaseRecyclerAdapter<String> {
    public MyAdapter(Context context, List<String> data) {
        super(context, R.layout.item_string, data);
    }

    @Override
    public int getType(int position) {
        return 0;
    }

    @Override
    public void convert(InnerBaseViewHolder holder, String s, int position, int itemType) {
        TextView view = holder.getView(R.id.tv1);
        view.setText(s);
    }
}
