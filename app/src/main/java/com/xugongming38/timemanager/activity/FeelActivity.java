package com.xugongming38.timemanager.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.utils.CacheFileUtils;
import com.xugongming38.timemanager.utils.Constant;
import com.xugongming38.timemanager.utils.ImageTools;

import java.io.File;
import java.util.ArrayList;


public class FeelActivity extends BaseFragmentActivity {

    EditText title;
    EditText feeling;
    TextView save;
    TextView submint;
    private String filePath;

    public static ArrayList<String> filePaths;
    private LinearLayout gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feel);
        setTitleName("总结与感悟");
        initView();//初始化视图
        initData();//初始化数据
    }

    private void initView() {
        gallery = (LinearLayout) this.findViewById(R.id.activity_createvisit_gallery);
        title= (EditText) findViewById(R.id.activity_createvisit_shopname);
        feeling= (EditText) findViewById(R.id.activity_createvisit_et);
        save= (TextView) findViewById(R.id.activity_createvisit_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"已经临时保存",Toast.LENGTH_SHORT).show();
            }
        });
        submint= (TextView) findViewById(R.id.activity_createvisit_submit);
        submint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"已经临时保存",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initData() {

        if (filePaths == null) {
            filePaths = new ArrayList<String>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {//检验接收数据
            return;
        }
        switch (requestCode) {
            case 1001://接收拍照返回数据
                if (!TextUtils.isEmpty(filePath)) {
                    Bitmap bitmap = ImageTools.convertToBitmap(filePath, 640, 640);
                    Bitmap bitmapComp = ImageTools.comp(bitmap);//图片压缩
                    ImageTools.saveBitmap(bitmapComp, filePath);
                    if (bitmap != null) {
                        filePaths.add(filePath);
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (filePaths != null) {
            //刷新图片,先清空然后重新添加图片
            gallery.removeAllViews();
            gallery.setVisibility(View.VISIBLE);
            //根据保存的图片地址创建图片
            for (int i = 0; i <= filePaths.size(); i++) {
                //创建一个ImageView
                ImageView imageView = new ImageView(mContext);
                //设置ImageView的缩放类型
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(220, 220);
                layout.leftMargin = 10;
                layout.rightMargin = 10;
                imageView.setLayoutParams(layout);
                //设置图片
                if (i < filePaths.size()) {
                    File file = new File(filePaths.get(i));
                    if (file.exists()) {
                        //如果文件存在，则显示保存图片
                        Bitmap bitmap = BitmapFactory.decodeFile(filePaths.get(i));
                        imageView.setImageBitmap(bitmap);
                    } else {
                        //如果文件丢失，则显示默认图片
                        imageView.setImageResource(R.drawable.default_img);
                    }
                } else {
                    //最后一张是显示添加图标
                    imageView.setImageResource(R.drawable.btn_add_img);
                }
                //按顺序添加
                gallery.addView(imageView, i);
                imgClickListener(imageView, i);
            }
        }
    }

    /*
* 图片点击查看事件
*/
    private void imgClickListener(View imagView, final int position) {
        imagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePaths == null) {
                    filePaths = new ArrayList<String>();
                }
                //点击最后一张图片的时候处理拍照
                if (position == filePaths.size()) {
                    if (filePaths.size() < 4) {
                        goTakePhoto();
                    } else {
                        Toast.makeText(mContext, "最多拍摄4张照片", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //点击其他图片跳转到查看
                    Intent intent = new Intent(mContext, ImageViewActivity.class);
                    intent.putExtra("type", Constant.ShopImgUp);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 调用手机拍照功能
     */
    protected void goTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = CacheFileUtils.getUpLoadPhotosPath();
        Uri uri = Uri.fromFile(new File(filePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        startActivityForResult(intent, 1001);
    }

    public void hideKeyboard() {//隐藏软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
