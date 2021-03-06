package com.yigu.common.api;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.yigu.common.result.MapiUserResult;
import com.yigu.common.util.DebugLog;
import com.yigu.common.util.MapiUtil;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by brain on 2016/6/16.
 */
public class UserApi extends BasicApi{

    public static void login(Activity activity, String name, String psd, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("USERNAME",name);
        params.put("PASSWORD",psd);
        MapiUtil.getInstance().call(activity,loginUrl,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiUserResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiUserResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){

            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

}
