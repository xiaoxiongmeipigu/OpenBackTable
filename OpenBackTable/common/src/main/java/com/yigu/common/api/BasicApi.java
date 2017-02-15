package com.yigu.common.api;

/**
 * Created by brain on 2016/6/14.
 */
public class BasicApi {
    public static String BASIC_URL = "http://122.225.92.10:8081";//http://115.159.118.182:8081/dinner
    public static String BASIC_IMAGE = "http://122.225.92.10:8081/uploadFiles/uploadImgs/";//http://115.159.118.182:8081/dinner
    /**首页*/
    public static String getmain= "/appuser/getmainay";
    public static String loginUrl = "/appuser/getAuntLogin";
    /**获取食堂列表*/
    public static String getCanteenlist = "/appuser/getCanteenlist";
    /**用餐时间列表*/
    public static String getDinnertime = "/appuser/getDinnertime";
    /**上菜用餐时间列表*/
    public static String getDinnertimeay = "/appuser/getDinnertimeay";
    /**获取菜单列表*/
    public static String getFoodmenu = "/appuser/getCFoodmenu";
    /**预购*/
    public static String preorderay = "/appuser/preorderay";
    /**职工卡支付*/
    public static String balancepay = "/appuser/balancepay";
    /**获取订单*/
    public static String getSaleslist = "/appuser/getSaleslist";
    /**商户地址填写说明备注*/
    public static String getRemark = "/appuser/getRemark";
    /**获取订单详情*/
    public static String getSalesdetailslist = "/appuser/getSalesdetailslist";
    /**图片新增*/
    public static String saveImages= "/appuser/saveImages";
    /**新增菜*/
    public static String shelves= "/appuser/shelves";
    /**订单完成按钮*/
    public static String sendto= "/appuser/sendto";
}
