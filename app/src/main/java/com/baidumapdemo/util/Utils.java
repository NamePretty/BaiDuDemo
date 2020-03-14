package com.baidumapdemo.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    public static final DateFormat DATE_TIME_FORMATER = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.CHINA);


    /**
     * 将字符串转换成浮点型，如果格式错误，将转成0
     *
     * @param str
     * @return
     */
    public static float toFloat(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }

        str = str.trim();
        float i = 0;
        try {
            i = Float.parseFloat(str);
        } catch (Exception e) {

        }
        LogUtil.e(i + "");
        return i;
    }

    /**
     * @param str
     * @return int
     * @throws
     * @author chenzheng
     * @Description: 转double
     * @since 2015-6-7
     */
    public static double toDouble(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }

        str = str.trim();
        double i = 0;
        try {
            i = Double.parseDouble(str);
        } catch (Exception e) {

        }
        return i;
    }

    /**
     * @param context
     * @return int
     * @throws
     * @author chenzheng
     * @Description: 获取屏幕宽度
     * @since 2014-5-9
     */
    public static int getScreenW(Context context) {
        return getScreenSize(context, true);
    }

    /**
     * @param context
     * @return int
     * @throws
     * @author chenzheng
     * @Description: 获取屏幕高度
     * @since 2014-5-9
     */
    public static int getScreenH(Context context) {
        return getScreenSize(context, false);
    }

    private static int getScreenSize(Context context, boolean isWidth) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return isWidth ? dm.widthPixels : dm.heightPixels;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
