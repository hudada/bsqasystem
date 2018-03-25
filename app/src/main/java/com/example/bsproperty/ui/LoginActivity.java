package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.bean.UserInfoBean;
import com.example.bsproperty.eventbus.LoginEvent;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.SpUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void loadData() {
    }

    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String number = etNumber.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    showToast(LoginActivity.this, "账号不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    showToast(LoginActivity.this, "密码不能为空！");
                    return;
                }

                OkHttpTools.sendGet(LoginActivity.this, ApiManager.LOGIN)
                        .addParams("userName",number)
                        .addParams("pwd",pass)
                        .build()
                        .execute(new BaseCallBack<UserInfoBean>(LoginActivity.this, UserInfoBean.class) {
                            @Override
                            public void onResponse(UserInfoBean userInfoBean) {
                                showToast(LoginActivity.this,"登陆成功");
                                SpUtils.setUserBean(LoginActivity.this,userInfoBean.getData());
                                MyApplication.getInstance().setUserBean(userInfoBean.getData());
                                if(userInfoBean.getData().getLid()==1){
                                    jumpAct(Main2Activity.class);
                                }else{
                                    jumpAct(MainActivity.class);
                                }
                                finish();
                            }
                        });
                break;
        }
    }

}
