package com.xugongming38.timemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xugongming38.timemanager.MainActivity;
import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.datamodel.User;
import com.xugongming38.timemanager.utils.SharePreUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mEtName;
    private EditText mEtPassword;
    private TextInputLayout mEtName_design;
    private TextInputLayout mEtPassword_design;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private String mUserName;
    private String mPassWord;
    private RelativeLayout mRelLoading;
    private Context mContext;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            login();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mContext = this;

        //if (checkLogin()) {
          //  startActivity(new Intent(LoginActivity.this, MainActivity.class));
          //  finish();
      //  } else {
            initStatusBarColor();
            bindViews();
            initListener();
       // }
    }


    /**
     * 初始化控件
     */
    private void bindViews() {
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtName_design = (TextInputLayout) findViewById(R.id.et_name_design);
        mEtPassword_design = (TextInputLayout) findViewById(R.id.et_password_design);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister =(Button)findViewById(R.id.btn_register) ;
        mRelLoading = (RelativeLayout) findViewById(R.id.loading);

        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mEtName_design.setErrorEnabled(false);
            }
        });
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mEtPassword_design.setErrorEnabled(false);
            }
        });
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelLoading.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(0,500);
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    /**
     * 登录
     */
    private void login() {
        if (checkData()) {

            List<User> persons = DataSupport.findAll(User.class);

            Boolean login=false;
            for(User ps:persons){
                if(mEtName.getText().toString().trim().equals(ps.getUsername())
                        &&(mEtPassword.getText().toString().trim().equals(ps.getPassword()))){
                    login=true;
                }

            }

            if (login) {
                //用户存在，密码正确，登录成功,先保存登录信息，然后跳转至主界面
                SharePreUtil.SetShareString(mContext, "username",mEtName.getText().toString() );
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "用户名或密码错误，登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 检查登录数据是否合法
     */
    private boolean checkData() {
        mUserName = mEtName.getText().toString().trim();
        mPassWord = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mUserName.trim())) {
            mEtName_design.setError("用户名不能为空");
            return false;
        }
        if (mUserName.trim().length() < 0 || mUserName.trim().length() > 6) {
            mEtName_design.setError("请输入6位数以内的用户名");
            return false;
        }
        if (TextUtils.isEmpty(mPassWord)) {
            mEtPassword_design.setError("密码不能为空");
            return false;
        }
        return true;
    }

    /**
     * 初始化状态栏颜色透明，和背景色一致
     */
    private void initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
