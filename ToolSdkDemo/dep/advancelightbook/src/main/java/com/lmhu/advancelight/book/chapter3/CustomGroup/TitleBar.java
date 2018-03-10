package com.lmhu.advancelight.book.chapter3.CustomGroup;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hulimin on 2018/2/28.
 */

public class TitleBar extends RelativeLayout{
    private ImageView iv_titleBar_left;
    private ImageView iv_titleBar_right;
    private TextView tv_titlebar_title;
    private RelativeLayout layout_titlebar_rootlayout;
    private int mColor= Color.WHITE;
    private int mTextColor=Color.WHITE;
    private String titlename;




    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
