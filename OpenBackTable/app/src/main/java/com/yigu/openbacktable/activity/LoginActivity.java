package com.yigu.openbacktable.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yigu.common.api.UserApi;
import com.yigu.common.result.MapiUserResult;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;
import com.yigu.common.widget.MainToast;
import com.yigu.openbacktable.R;
import com.yigu.openbacktable.base.BaseActivity;
import com.yigu.openbacktable.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.psd)
    EditText psd;
    @Bind(R.id.center)
    TextView center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        center.setText("登录");
    }

    @OnClick({R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String nameStr = name.getText().toString();
                String psdStr = psd.getText().toString();
                if(TextUtils.isEmpty(nameStr)) {
                    MainToast.showShortToast("请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(psdStr)) {
                    MainToast.showShortToast("请输入密码");
                    return;
                }
                UserApi.login(this, nameStr, psdStr, new RequestCallback<MapiUserResult>() {
                    @Override
                    public void success(MapiUserResult success) {
                        MainToast.showShortToast("登录成功");
                        userSP.saveUserBean(success);
                        ControllerUtil.go2Main();
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        MainToast.showShortToast(message);
                    }
                });
                break;
        }
    }
}
