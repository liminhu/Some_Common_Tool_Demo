package com.lmhu.advancelight.book.chapter3.CustomProgressView;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by hulimin on 2018/3/12.
 */

public class DensityUtils {
    //px转dp的公式：dp = px/density
    //设备密度公式：density = PPI/160。
    //PPI是像素密度，公式：PPI = √（长度像素数² + 宽度像素数²） / 屏幕尺寸
    //后面加上0.5f是因为 咱们要的只不是那么精准，根据推理算出来的是个浮点数，而咱们程序中一般使用int类型就够了

    public static int dp2px(Context ctx, float dpVal){
        return (int)(dpVal * (ctx.getResources().getDisplayMetrics().density)+0.5f);
    }


    public static int sp2px(Context ctx, float spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, ctx.getResources().getDisplayMetrics());
    }


    public static float px2sp(Context ctx, float pxVal){
        return (pxVal / ctx.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }


    /**
     *
     * @return
     */
    public  static  float px2dp(Context ctx, int px){
        return px / (ctx.getResources().getDisplayMetrics().density + 0.5f);
    }

}
