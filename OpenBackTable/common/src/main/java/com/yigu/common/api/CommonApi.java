package com.yigu.common.api;

import android.app.Activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yigu.common.result.MapiImageResult;
import com.yigu.common.util.DebugLog;
import com.yigu.common.util.MapiUtil;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/7/22.
 */
public class CommonApi extends BasicApi{

    //导入资源
    public static void loadResources(final Activity act, final RequestCallback callback,
                                     final RequestExceptionCallback exceptionCallback) {
        Map<String, String> params = new HashMap<>();
        MapiUtil.getInstance().call(act,getmain, params,
                new MapiUtil.MapiSuccessResponse() {
                    @Override
                    public void success(JSONObject json) {
                        DebugLog.i("resourcejson"+json);
                       /* Map<String, ArrayList<MapiResourceResult>> userBean = JSON.parseObject(json
                                        .getJSONObject("data").toJSONString(),
                                new TypeReference<Map<String, ArrayList<MapiResourceResult>>>() {
                                });*/
                        callback.success(json.toJSONString());
                    }
                }, new MapiUtil.MapiFailResponse() {
                    @Override
                    public void fail(String code,String message) {
                        exceptionCallback.error(code,message);
                    }
                });
    }

    /**
     *商户地址填写说明备注
     * @param activity
     * @param SHOP
     * @param callback
     * @param exceptionCallback
     */
    public static void getRemark(Activity activity,String SHOP,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        MapiUtil.getInstance().call(activity,getRemark,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                String tip = json.getString("data");
                callback.success(tip);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


    /**
     * 上传图片
     * @param activity
     * @param file
     * @param callback
     * @param exceptionCallback
     */
    public static void uploadImage(Activity activity, File file, final RequestCallback callback, final RequestExceptionCallback exceptionCallback) {
        MapiUtil.getInstance().uploadFile(activity, saveImages, file, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i(json.toString());
                MapiImageResult imageResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiImageResult.class);
                callback.success(imageResult);
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 新增菜品
     * @param activity
     * @param SHOP
     * @param type
     * @param food
     * @param price
     * @param pic
     * @param callback
     * @param exceptionCallback
     */
    public static void shelves(Activity activity,String SHOP,String type,String food,String price,String pic,String stardate,String dinnertime,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        params.put("type",type);
        params.put("food",food);
        params.put("price",price);
        params.put("pic",pic);
        params.put("amount","999");
        params.put("stardate",stardate);
        params.put("dinnertime",dinnertime);
        MapiUtil.getInstance().call(activity,shelves,params,new MapiUtil.MapiSuccessResponse(){
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
