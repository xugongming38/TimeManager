package com.xugongming38.timemanager.fragment;

/**
 * Created by dell on 2018/4/17.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.base.SetItemView;
import com.xugongming38.timemanager.utils.SharePreUtil;

/**
 * 个人中心-主界面
 */
public class Me extends BaseFragment {
    private View mView;
    private SetItemView mMeItem;
    private SetItemView mAboutItem;
    private SetItemView mExitItem;
    private SetItemView mClearItem;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        initView();

        return mView;
    }

    /**
     * 初始化控件信息
     */
    private void initView() {
        mExitItem=(SetItemView) mView.findViewById(R.id.rl_exit);
        mMeItem = (SetItemView) mView.findViewById(R.id.rl_me);
        mAboutItem = (SetItemView) mView.findViewById(R.id.rl_about);
        mClearItem = (SetItemView) mView.findViewById(R.id.rl_clear);

        mMeItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Toast.makeText(mActivity, "点击了个人资料", Toast.LENGTH_SHORT).show();
            }
        });
        mAboutItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Toast.makeText(mActivity, "点击了关于", Toast.LENGTH_SHORT).show();
            }
        });
        mExitItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Toast.makeText(mActivity, "点击了退出", Toast.LENGTH_SHORT).show();
            }
        });
        mClearItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                SharePreUtil.SetShareString(getActivity(), "username","");
                Toast.makeText(mActivity, "清除数据成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

}