package com.yigu.common.api;

import android.app.Activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yigu.common.util.DebugLog;
import com.yigu.common.util.MapiUtil;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/7/22.
 */
public class CommonApi extends BasicApi{

    //导入资源
    public static void loadResources(final Activity act,String appuserid, final RequestCallback callback,
                                     final RequestExceptionCallback exceptionCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("appuserid",appuserid);
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

}
