package com.baidumapdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidumapdemo.R;
import com.baidumapdemo.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: zdj
 * created on: 2017/11/20 11:28
 * description:
 */

public class GpsRemindDialog extends Dialog {

    @BindView(R.id.cancel_btn)
    TextView cancelBtn;
    @BindView(R.id.open_btn)
    TextView openBtn;
    private Context context;
    private OnChooseClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gps_remind);
        ButterKnife.bind(this);

        getWindow().setLayout(Utils.getScreenW(context) - 40, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        initView();

    }

    private void initView() {
    }


    @OnClick({R.id.cancel_btn, R.id.open_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                if(listener!=null){
                    listener.onCancel();
                }
                break;
            case R.id.open_btn:
                if(listener!=null){
                    listener.onOpen();
                }
                break;
        }
    }


    public interface OnChooseClickListener {
        void onCancel();
        void onOpen();
    }

    public GpsRemindDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
    }

    public void show(OnChooseClickListener listener) {
        this.listener = listener;
        show();
    }
}
