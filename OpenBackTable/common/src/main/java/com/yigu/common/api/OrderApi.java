package com.yigu.common.api;

import android.app.Activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yigu.common.result.MapiHistoryResult;
import com.yigu.common.result.MapiOrderResult;
import com.yigu.common.result.MapiResourceResult;
import com.yigu.common.util.DebugLog;
import com.yigu.common.util.MapiUtil;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;
import com.yigu.common.util.RequestPageCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2017/1/3.
 */
public class OrderApi extends BasicApi{

    /**
     * 获取食堂列表
     * @param activity
     * @param COMPANY
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getCanteenlist(Activity activity, String COMPANY, String PAGENO, String SIZE, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("COMPANY",COMPANY);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getCanteenlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("canteen").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 用餐时间列表
     * @param activity
     * @param SHOP
     * @param callback
     * @param exceptionCallback
     */
    public static void getDinnertime(Activity activity, String SHOP, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        MapiUtil.getInstance().call(activity,getDinnertime,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiResourceResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 上菜用餐时间列表
     * @param activity
     * @param SHOP
     * @param callback
     * @param exceptionCallback
     */
    public static void getDinnertimeay(Activity activity, String SHOP, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        MapiUtil.getInstance().call(activity,getDinnertimeay,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiResourceResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 获取菜单列表
     * @param activity
     * @param SHOP
     * @param DATE
     * @param TYPE
     * @param STYLE
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getFoodmenu(Activity activity,String SHOP,String DATE,String TYPE,String STYLE,String PAGENO,String SIZE,
                                   final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        params.put("DATE",DATE);
        params.put("TYPE",TYPE);
        params.put("STYLE",STYLE);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getFoodmenu,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("food").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 预购
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param callback
     * @param exceptionCallback
     */
    public static void preorderay(Activity activity,String appuserid,String SHOP,String money,String sales,String bz,final RequestCallback callback,
                                final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("sales",sales);
        params.put("aunt","1");
        params.put("bz",bz);
        MapiUtil.getInstance().call(activity,preorderay,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });


    }

    /**
     * 职工卡支付
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param callback
     * @param exceptionCallback
     */
    public static void balancepay(Activity activity,String appuserid,String SHOP,String money,String sales,String bz,final RequestCallback callback,
                                  final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("sales",sales);
        MapiUtil.getInstance().call(activity,balancepay,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });


    }

    /**
     * 获取订单
     * @param activity
     * @param appuserid
     * @param TYPE
     * @param created
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getSaleslist(Activity activity,String appuserid,String TYPE,String created,String PAGENO,String SIZE,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String, String> params = new HashMap<>();
        params.put("appuserid", appuserid);
        params.put("TYPE", TYPE);
        params.put("created", created);
        params.put("PAGENO", PAGENO);
        params.put("SIZE", SIZE);
        params.put("aunt", "1");
        params.put("isfinish", "0");
        MapiUtil.getInstance().call(activity, getSaleslist, params, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json=" + json);
                List<MapiHistoryResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("sales").toJSONString(), MapiHistoryResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if (null != count) {
                    callback.success(count, result);
                }
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 获取订单详情
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void getSalesdetailslist(Activity activity,String id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,getSalesdetailslist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiOrderResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 订单完成按钮
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void sendto(Activity activity,String id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,sendto,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


}
