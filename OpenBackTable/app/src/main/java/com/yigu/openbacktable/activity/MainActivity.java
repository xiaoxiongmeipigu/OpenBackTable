package com.yigu.openbacktable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yigu.common.api.CommonApi;
import com.yigu.common.result.MapiResourceResult;
import com.yigu.common.util.FileUtil;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;
import com.yigu.common.widget.MainToast;
import com.yigu.openbacktable.R;
import com.yigu.openbacktable.base.BaseActivity;
import com.yigu.openbacktable.util.ControllerUtil;
import com.yigu.openbacktable.view.HomeSliderLayout;
import com.yigu.openbacktable.widget.MainAlertDialog;
import com.yigu.updatelibrary.UpdateFunGo;
import com.yigu.updatelibrary.config.DownloadKey;
import com.yigu.updatelibrary.config.UpdateKey;
import com.yigu.updatelibrary.utils.GetAppInfo;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.homeSliderLayout)
    HomeSliderLayout homeSliderLayout;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    private DbManager db;

    MainAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!userSP.checkLogin()) {
            ControllerUtil.go2Login();
            finish();
        } else {
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            initView();
            initListener();
            load();
        }
    }

    private void initView(){
        center.setText("首页");
        tvRight.setText("退出");

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("openback").setDbDir(new File(FileUtil.getFolderPath(this, FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        if (dialog == null) {
            dialog = new MainAlertDialog(this);
            dialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认退出当前账号吗？");
        }

    }

    private void initListener(){
        dialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSP.clearUserData();
                dialog.dismiss();
                ControllerUtil.go2Login();
                finish();

            }
        });
    }

    private void load(){
        showLoading();
        CommonApi.loadResources(this, new RequestCallback<String>() {
            @Override
            public void success(String success) {
                hideLoading();
                userSP.saveResource(success);
                if(null!=userSP.getResource()){
                    JSONObject jsonObject = JSONObject.parseObject(userSP.getResource());

                    MapiResourceResult mapiResourceResult = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), MapiResourceResult.class);
                    if(null!=mapiResourceResult)
                        checkVersion(mapiResourceResult);
                   /* List<MapiResourceResult> images = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("poster").toJSONString(),  MapiResourceResult.class);
                    if(null!=images&&!images.isEmpty())
                        homeSliderLayout.load(images);*/



                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    /**
     * 检查版本，若不是最新版本则显示弹框
     *
     * @param result
     */
    private void checkVersion(MapiResourceResult result) {
        if (!GetAppInfo.getAppVersionCode(this).equals(result.getVersion())) {
            DownloadKey.version = result.getVersion();
            DownloadKey.changeLog = result.getRemark();
            DownloadKey.apkUrl = result.getUrl();
            //如果你想通过Dialog来进行下载，可以如下设置
            UpdateKey.DialogOrNotification= UpdateKey.WITH_DIALOG;
            DownloadKey.ToShowDownloadView = DownloadKey.showUpdateView;
            UpdateFunGo.init(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGo.onResume(this);//现在只能弹框下载
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGo.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int type = intent.getIntExtra("type",0);
        if(type==3){
            ControllerUtil.go2Login();
            finish();
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.tv_right, R.id.ll_buy, R.id.ll_order, R.id.ll_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                dialog.show();
                break;
            case R.id.ll_buy:
                ControllerUtil.go2UnitOrder();
                break;
            case R.id.ll_order:
                ControllerUtil.go2IntentList();
                break;
            case R.id.ll_up:
                ControllerUtil.go2Upload();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(userSP.checkLogin()){
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
