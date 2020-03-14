package com.baidumapdemo.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.archeanx.lib.adapter.XRvPureAdapter;
import com.archeanx.lib.widget.dialog.CommentInputDialog;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidumapdemo.R;
import com.baidumapdemo.adapter.MapSearchAdapter;
import com.baidumapdemo.util.AppStaticVariable;
import com.baidumapdemo.util.BaiduLocationUtils;
import com.baidumapdemo.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchCityActivity extends AppCompatActivity {
    @BindView(R.id.ams_et)
    EditText mEditText;
    @BindView(R.id.ams_back)
    TextView backTv;
    @BindView(R.id.ams_srl)
    SmartRefreshLayout ams_srl;
    @BindView(R.id.ams_rv)
    RecyclerView recyclerView;
    public SuggestionSearch mSuggestionSearch;
    private MapSearchAdapter mMapSearchAdapter;
    private BaiduLocationUtils baiduLocationUtils;
    private String mCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        ButterKnife.bind(this);
        initLocation();
        initView();
    }

    private void initView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mMapSearchAdapter = new MapSearchAdapter();
        mMapSearchAdapter.setOnItemClickListener(new XRvPureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SuggestionResult.SuggestionInfo ss = mMapSearchAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(AppStaticVariable.MAP_SEARCH_LONGITUDE, ss.pt.longitude);
                intent.putExtra(AppStaticVariable.MAP_SEARCH_LATITUDE, ss.pt.latitude);
                intent.putExtra(AppStaticVariable.MAP_SEARCH_ADDRESS, ss.city + ss.district + ss.key);
                setResult(Activity.RESULT_OK, intent);
                ToastUtil.show(SearchCityActivity.this,ss.key);
                finish();
            }
        });
        recyclerView.setAdapter(mMapSearchAdapter);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 这里的s表示改变之前的内容，通常start和count组合，可以在s中读取本次改变字段中被改变的内容。而after表示改变后新的内容的数量。
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 这里的s表示改变之后的内容，通常start和count组合，可以在s中读取本次改变字段中新的内容。而before表示被改变的内容的数量。
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 表示最终内容
                String mapInput = mEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(mapInput)) {
                    //搜索关键词
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(mapInput).city(mCity)
                    );
                }
            }
        };
        mEditText.addTextChangedListener(tw);

        initSearch();

        mEditText.setFilters(new InputFilter[]{CommentInputDialog.EMOJI_FILTER});

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEditText, 0);
    }

    private void initLocation(){
        baiduLocationUtils = new BaiduLocationUtils(SearchCityActivity.this, new BaiduLocationUtils.OnLocateCompletedListener() {

            @Override
            public void onLocateCompleted(double latitude, double longitude, BDLocation location) {
                setLocationData(latitude, longitude, location);
            }
        });
    }

    public void setLocationData(double latitude, double longitude, BDLocation location) {
        if(location!=null) {
            mCity = location.getCity();
        }
    }

    /**
     * 搜索
     */
    public void initSearch() {
        //关键词搜索
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                //未找到相关结果
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    return;
                }
                List<SuggestionResult.SuggestionInfo> ssList = suggestionResult.getAllSuggestions();

                //关键搜索时，数据有时候没有经纬度，和地址信息,需要剔除
                Iterator<SuggestionResult.SuggestionInfo> itParent = ssList.iterator();
                while (itParent.hasNext()) {
                    SuggestionResult.SuggestionInfo ss = itParent.next();
                    if (ss.pt == null || TextUtils.isEmpty(ss.district)) {
                        itParent.remove();
                    }
                }
                mMapSearchAdapter.setDatas(ssList, true);
            }
        });
    }


    @OnClick({R.id.ams_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_location:
                finish();
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放POI检索实例；
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
        }
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchCityActivity.class);
        context.startActivity(intent);
    }
}
