package com.xugongming38.timemanager.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xugongming38.timemanager.R;

/**
 * Created by dell on 2018/4/18.
 */

public class SetItemView extends RelativeLayout {
    //页面对象
    private View mView;
    //页面跟布局
    private RelativeLayout mRootLayout;
    //左侧文字
    private TextView mTvLeftText;
    //左侧图标
    private ImageView mIvLeftIcon;
    //右侧图标
    private ImageView mIvRightIcon;
    //下划线
    private View mUnderLine;
    //显示文字
    private String mText;
    //文字颜色
    private int mTextColor;
    //文字大小
    private int mTextSize;
    //左侧图标
    private Drawable mLeftIcon;
    // 右侧图标
    private Drawable mRightIcon;
    //左侧边距
    private int mMarginLeft;
    //右侧边距
    private int mMarginRight;
    //定义监听器
    private OnSetItemClick mOnSetItemClick;

    public SetItemView(Context context) {
        this(context, null);
    }

    public SetItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        getCustomStyle(context, attrs);
        //设置点击监听事件
        mRootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSetItemClick) {
                    mOnSetItemClick.click();
                }
            }
        });
    }

    /**
     * 初始化控件自定义属性信息
     *
     * @param context
     * @param attrs
     */
    private void getCustomStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SetItemView);
        int num = typedArray.getIndexCount();
        for (int i = 0; i < num; i++) {
            int arr = typedArray.getIndex(i);
            if (arr == R.styleable.SetItemView_leftText) {
                mText = typedArray.getString(arr);
                mTvLeftText.setText(mText);
            } else if (arr == R.styleable.SetItemView_leftIcon) {
                mLeftIcon = typedArray.getDrawable(arr);
                mIvLeftIcon.setImageDrawable(mLeftIcon);
            } else if (arr == R.styleable.SetItemView_rightIcon) {
                mRightIcon = typedArray.getDrawable(arr);
                mIvRightIcon.setImageDrawable(mRightIcon);
            } else if (arr == R.styleable.SetItemView_textSize) {
                // 默认设置为16sp
                float textSize = typedArray.getFloat(arr, 16);
                mTvLeftText.setTextSize(textSize);
            } else if (arr == R.styleable.SetItemView_textColor) {
                //文字默认灰色
                mTextColor = typedArray.getColor(arr, Color.GRAY);
                mTvLeftText.setTextColor(mTextColor);
            } else if (arr == R.styleable.SetItemView_isShowUnderLine) {
                boolean flag = typedArray.getBoolean(arr, true);
                if (!flag) {
                    mUnderLine.setVisibility(View.GONE);
                }
            }
        }
        typedArray.recycle();
    }

    /**
     * 初始化控件对象
     */
    private void initView(Context context) {
        mView = View.inflate(context, R.layout.settingitem, this);
        mRootLayout = (RelativeLayout) mView.findViewById(R.id.rootLayout);
        mTvLeftText = (TextView) mView.findViewById(R.id.tv_lefttext);
        mIvLeftIcon = (ImageView) mView.findViewById(R.id.iv_lefticon);
        mIvRightIcon = (ImageView) mView.findViewById(R.id.iv_righticon);
        mUnderLine = mView.findViewById(R.id.underline);
    }

    public void setmOnSetItemClick(OnSetItemClick mOnSetItemClick) {
        this.mOnSetItemClick = mOnSetItemClick;
    }

    public interface OnSetItemClick {
        void click();
    }
}
