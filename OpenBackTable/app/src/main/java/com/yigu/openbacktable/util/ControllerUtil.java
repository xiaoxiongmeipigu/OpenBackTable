package com.yigu.openbacktable.util;

import android.content.Intent;

import com.yigu.common.application.AppContext;
import com.yigu.common.result.MapiHistoryResult;
import com.yigu.common.result.MapiOrderResult;
import com.yigu.openbacktable.activity.LoginActivity;
import com.yigu.openbacktable.activity.MainActivity;
import com.yigu.openbacktable.activity.intent.IntentDetailActivity;
import com.yigu.openbacktable.activity.intent.IntentListActivity;
import com.yigu.openbacktable.activity.order.OrderListActivity;
import com.yigu.openbacktable.activity.order.UnitOrderActivity;
import com.yigu.openbacktable.activity.pay.PaymentActivity;
import com.yigu.openbacktable.activity.upload.UploadActivity;
import com.yigu.openbacktable.activity.webview.WebviewControlActivity;

import java.util.ArrayList;


/**
 * Created by brain on 2016/6/22.
 */
public class ControllerUtil {

    /**
     * 主页
     */
    public static void go2Main() {
        Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * h5页面
     */
    public static void go2WebView(String url, String title, boolean isShare) {
        Intent intent = new Intent(AppContext.getInstance(), WebviewControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isShare", isShare);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 登录
     */
    public static void go2Login() {
        Intent intent = new Intent(AppContext.getInstance(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-列表
     */
    public static void go2UnitOrder() {
        Intent intent = new Intent(AppContext.getInstance(), UnitOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-列表
     */
    public static void go2OrderList(MapiOrderResult mapiOrderResult) {
        Intent intent = new Intent(AppContext.getInstance(), OrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", mapiOrderResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-结算
     */
    public static void go2Payment(String SHOP) {
        Intent intent = new Intent(AppContext.getInstance(),PaymentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SHOP",SHOP);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 订单管理
     */
    public static void go2IntentList() {
        Intent intent = new Intent(AppContext.getInstance(), IntentListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 订单详情
     */
    public static void go2IntentDetail(MapiHistoryResult mapiHistoryResult) {
        Intent intent = new Intent(AppContext.getInstance(), IntentDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item",mapiHistoryResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 上传菜品
     */
    public static void go2Upload() {
        Intent intent = new Intent(AppContext.getInstance(), UploadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }


}
