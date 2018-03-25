package com.example.bsproperty.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by John on 2018/1/30.
 */

public class DenstityUtils {
    /**
     * dp ---> px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        // > 公式: 1px = 1dp * (dpi / 160)

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;

        return (int) (dp * (dpi / 160f) + 0.5f);

    }

    /**
     * px --> dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        // > 公式: 1dp = 1px * 160 / dpi
        return (int) (px * 160f / dpi + 0.5f);
    }

    public static int screenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int screenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static String getTeamStr(int team) {
        //0=技术部，1=销售部，2=人事部
        String str = "";
        switch (team) {
            case 0:
                str = "技术部";
                break;
            case 1:
                str = "销售部";
                break;
            case 2:
                str = "人事部";
                break;
        }
        return str;
    }

    public static String getLimitStr(int limit) {
        //0=员工，1=经理
        String str = "";
        switch (limit) {
            case 0:
                str = "员工";
                break;
            case 1:
                str = "经理";
                break;
        }
        return str;
    }

    public static String getTypeStr(int type) {
        //0=工作，1=绩效考核，2=考勤，3=其它
        String str = "";
        switch (type) {
            case 0:
                str = "工作";
                break;
            case 1:
                str = "绩效考核";
                break;
            case 2:
                str = "考勤";
                break;
            case 3:
                str = "其它";
                break;
        }
        return str;
    }
}
