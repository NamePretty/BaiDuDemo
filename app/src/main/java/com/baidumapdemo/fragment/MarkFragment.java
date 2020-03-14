package com.baidumapdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidumapdemo.R;
import com.baidumapdemo.ui.MarkPolymerizationActivity;
import com.baidumapdemo.ui.RegionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkFragment extends Fragment {
    @BindView(R.id.tv_region)
    TextView tv_region;
    @BindView(R.id.tv_mark_polymerization)
    TextView tv_mark_polymerization;

    private View mView;
    public static MarkFragment getInstance() {
        return new MarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mark, container,
                false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView(){

    }

    @OnClick({R.id.tv_region,R.id.tv_mark_polymerization})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_region:
                RegionActivity.startActivity(getActivity());
                break;
            case R.id.tv_mark_polymerization:
                MarkPolymerizationActivity.startActivity(getActivity());
                break;
        }
    }
}
