package com.hlm.xposed.common.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlm.xposed.common.ui.util.LayoutUtil;
import com.hlm.xposed.common.ui.util.ViewUtil;
import com.hlm.xposed.common.util.DisplayUtil;


/**
 * Created by sky on 2018/8/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TitleView extends FrameLayout implements View.OnClickListener {

    private ImageButton ivBack;
    private TextView tvTitle;
    private LinearLayout mMoreLayout;
    private OnBackEventListener mOnBackEventListener;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int height = getTitleHeight();

        setLayoutParams(LayoutUtil.newViewGroupParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        setBackgroundColor(0xFF161823);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(6);
        }

        LinearLayout tLayout = new LinearLayout(getContext());
        tLayout.setGravity(Gravity.CENTER_VERTICAL);
        tLayout.setOrientation(LinearLayout.HORIZONTAL);

        ivBack = new ImageButton(getContext());
        ivBack.setLayoutParams(LayoutUtil.newViewGroupParams(height, height));
        ivBack.setTag("back");
        ivBack.setBackground(ViewUtil.newTitleBackgroundDrawable());
        ivBack.setOnClickListener(this);

        tvTitle = new TextView(getContext());
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(18);
        tvTitle.getPaint().setFakeBoldText(true);

        tLayout.addView(ivBack);
        tLayout.addView(tvTitle);

        LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;

        // 更多的Layout
        mMoreLayout = new LinearLayout(getContext());
        mMoreLayout.setGravity(Gravity.CENTER_VERTICAL);
        mMoreLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams moreParams = LayoutUtil.newWrapFrameLayoutParams();
        moreParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

        addView(tLayout, params);
        addView(mMoreLayout, moreParams);

        hideBack();
    }

    public int getTitleHeight() {
        return DisplayUtil.dip2px(getContext(), 50);
    }

    public void addMoreView(View view) {
        mMoreLayout.addView(view);
    }

    public ImageButton addMoreImageButton() {

        int height = getTitleHeight();

        ImageButton newImageButton = new ImageButton(getContext());
        newImageButton.setLayoutParams(LayoutUtil.newViewGroupParams(height, height));
        newImageButton.setBackground(ViewUtil.newTitleBackgroundDrawable());

        addMoreView(newImageButton);

        return newImageButton;
    }

    public TextView addMoreTextView(String text) {

        int height = getTitleHeight();
        int left = DisplayUtil.dip2px(getContext(), 5);

        TextView newTextView = new TextView(getContext());
        newTextView.setText(text);
        newTextView.setTextSize(15);
        newTextView.getPaint().setFakeBoldText(true);
        newTextView.setTextColor(Color.WHITE);
        newTextView.setPadding(left, 0, left, 0);
        newTextView.setGravity(Gravity.CENTER);
        newTextView.setLayoutParams(LayoutUtil.newViewGroupParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        newTextView.setMinWidth(height);
        newTextView.setBackground(ViewUtil.newTitleBackgroundDrawable());

        addMoreView(newTextView);

        return newTextView;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void showBack() {
        ViewUtil.setVisibility(ivBack, View.VISIBLE);
        tvTitle.setPadding(0, 0, 0, 0);
    }

    public void hideBack() {
        ViewUtil.setVisibility(ivBack, View.GONE);
        tvTitle.setPadding(DisplayUtil.dip2px(getContext(), 15), 0, 0, 0);
    }

    public ImageButton getBackView() {
        return ivBack;
    }

    public OnBackEventListener getOnBackEventListener() {
        return mOnBackEventListener;
    }

    public void setOnBackEventListener(OnBackEventListener onBackEventListener) {
        mOnBackEventListener = onBackEventListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnBackEventListener != null) mOnBackEventListener.onEvent(v);
    }

    public interface OnBackEventListener {

        void onEvent(View view);
    }
}
