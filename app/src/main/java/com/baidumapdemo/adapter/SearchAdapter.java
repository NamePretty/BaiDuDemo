package com.baidumapdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidumapdemo.R;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements View.OnClickListener {

    private List<PoiInfo> mList = new ArrayList<>();

    private Context mContext;

    public List<PoiInfo> getItems() {
        return mList;
    }

    public SearchAdapter(Context context) {
        this.mContext = context;
    }

    public void setItems(List<PoiInfo> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, PoiInfo data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final PoiInfo poiInfo = mList.get(position);
        viewHolder.tv_adress.setText(poiInfo.address);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(poiInfo);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (PoiInfo) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_adress)
        TextView tv_adress;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
