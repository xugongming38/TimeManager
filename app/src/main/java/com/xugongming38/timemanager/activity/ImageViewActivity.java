package com.xugongming38.timemanager.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.utils.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageViewActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView left, right;
    private PhotoView img;
    private Button delete;
    private List<String> list;
    private String[] imgs;
    private int mposition;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        setTitleName("图片查看");
        initView();//初始化视图
        initData();//初始化数据
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mposition = (Integer) getIntent().getIntExtra("position", -1);
        type = (Integer) getIntent().getIntExtra("type", -1);
        ShowImage(mposition, type);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        img = (PhotoView) this.findViewById(R.id.image_view_img);
        right = (ImageView) this.findViewById(R.id.image_view_right);
        left = (ImageView) this.findViewById(R.id.image_view_left);
        delete = (Button) this.findViewById(R.id.image_view_detele);
        right.setOnClickListener(this);
        left.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_left://浏览上一张图片
                if (mposition > 0) {
                    mposition--;
                    ShowImage(mposition, type);
                }
                break;
            case R.id.image_view_right://浏览下一张图片
                if (list == null) {
                    list = new ArrayList<String>();
                }
                if (mposition < list.size() - 1 && mposition > -1) {
                    mposition++;
                    ShowImage(mposition, type);
                }
                break;
            case R.id.image_view_detele://删除图片
                if (list != null) {
                    if (list.size() > 0) {
                        File file = new File(list.get(mposition));
                        switch (type) {
                            case Constant.PhotoUp:
                                //培训图片处理
                                break;
                            case Constant.ShopImgUp:
                                FeelActivity.filePaths.remove(mposition);
                                break;
                        }
                        if (file.exists()) {
                            file.delete();
                        }
                        if (list.size() == 0) {
                            finish();
                        }
                        if (mposition == list.size()) {
                            mposition--;
                            ShowImage(mposition, type);
                        } else {
                            ShowImage(mposition, type);
                        }
                    }

                }
                break;
        }
    }

    public void ShowImage(int positon, int type) {
        if (type > 0 && positon >= 0) {
            switch (type) {
                case Constant.ShopImgLook:
                    //巡店完成查看详情图片
                    break;
                case Constant.PhotoLook:
                    //培训图片查看
                    break;
                case Constant.PhotoUp:
                    //培训拍照上传查看图片
                    break;
                case Constant.ShopImgUp://巡店图片上传查看
                    list = FeelActivity.filePaths;
                    if (list == null) {
                        list = new ArrayList<String>();
                    }
                    //如果是最后一张图，设置右侧图标不可用
                    if (positon == list.size() - 1) {
                        right.setSelected(true);
                    } else {
                        right.setSelected(false);
                    }
                    //如果是第一张图，设置左侧图标不可用
                    if (positon == 0) {
                        left.setSelected(true);
                    } else {
                        left.setSelected(false);
                    }
                    String shopImg = list.get(positon);
                    File shopFile = new File(shopImg);
                    if (shopFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(shopImg);
                        img.setImageBitmap(bitmap);
                    } else {
                        list.remove(positon);
                        Toast.makeText(mContext, "图片已不存在", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } else {
            img.setImageResource(R.drawable.default_img);
        }
    }

}
