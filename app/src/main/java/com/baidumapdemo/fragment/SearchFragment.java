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
import com.baidumapdemo.ui.AdressChooseActivity;
import com.baidumapdemo.ui.SearchCityActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment {

    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.tv_adress)
    TextView tv_adress;

    private View mView;

    public static SearchFragment getInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container,
                false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView(){

    }

    @OnClick({R.id.tv_search,R.id.tv_adress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                SearchCityActivity.startActivity(getActivity());
                break;
            case R.id.tv_adress:
                AdressChooseActivity.startActivity(getActivity());
                break;

        }
    }
}
