package com.baidumapdemo.listener;

import android.view.View;

/**
 * <p>作者：zdj<p>
 * <p>创建时间：2018/11/2<p>
 * <p>更改时间：2018/11/2<p>
 * <p>版本号：1<p>
 */
public abstract class MyClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        MyClickListeners(v,(Integer)v.getTag());
    }

    public abstract void MyClickListeners(View v, int position);

}
