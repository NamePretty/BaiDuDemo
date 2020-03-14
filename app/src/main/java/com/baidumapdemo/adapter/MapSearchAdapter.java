package com.baidumapdemo.adapter;


import android.widget.TextView;

import com.archeanx.lib.adapter.XRvPureDataAdapter;
import com.archeanx.lib.adapter.xutil.XRvViewHolder;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidumapdemo.R;


/**
 * Created by zdj on 2019/09/07 0009.
 * 地图 地址列表搜索 适配器
 * @author zdj
 */

public class MapSearchAdapter extends XRvPureDataAdapter<SuggestionResult.SuggestionInfo> {

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.item_map;
    }

    @Override
    public void onBindViewHolder(XRvViewHolder holder, int position) {
        TextView bigTv=holder.getView(R.id.im_bigtv);
        SuggestionResult.SuggestionInfo ss=mDatas.get(position);
        bigTv.setText(ss.city+ss.district+ss.key);

    }
}
