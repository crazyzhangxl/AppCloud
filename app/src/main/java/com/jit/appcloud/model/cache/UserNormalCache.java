package com.jit.appcloud.model.cache;

import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.util.SharePreferenceUtils;
import com.jit.appcloud.util.UIUtils;

/**
 * @author zxl on 2018/5/31.
 *         discription:用户基本信息的缓存
 *

 */

public class UserNormalCache {

    public static String getDepartment(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.DEPARTMENT,null);
    }
    public static void setDepartment(String department){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.DEPARTMENT,department);
    }

    public static String getProvince(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.PROVINCE,null);
    }
    public static void setProvince(String province){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.PROVINCE,province);
    }
    public static String getCity(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.CITY,null);
    }
    public static void setCity(String city){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.CITY,city);
    }
    public static String getCountry(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.COUNTRY,null);
    }
    public static void setCountry(String country){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.COUNTRY,country);
    }
    public static String getAddress(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.ADDRESS,null);
    }
    public static void setAddress(String address){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.ADDRESS,address);
    }
    public static String getArea(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.AREA,null);
    }
    public static void setArea(String area){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.AREA,area);
    }
    public static String getTel(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.TEL,null);
    }
    public static void setTel(String tel){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.TEL,tel);
    }
    public static String getEmail(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.EMAIL,null);
    }
    public static void setEmail(String email){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.EMAIL,email);
    }
    public static String getRealName(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.REALNAME,null);
    }
    public static void setRealName(String realname){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.REALNAME,realname);
    }
    public static String getCategory(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.CATEGORY,null);
    }
    public static void setCategory(String category){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.CATEGORY,category);
    }
    public static String getSignature(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.SIGN,null);
    }
    public static void setSignature(String sign){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.SIGN,sign);
    }
    public static String getHobby(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserNormal.HOBBY,null);
    }
    public static void setHobby(String hobby){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.HOBBY,hobby);
    }
    public static int getIncome(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getInt(AppConst.UserNormal.INCOME,0);
    }
    public static void setIncome(int income){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putInt(AppConst.UserNormal.INCOME,income);
    }

    public static UserInfoUpRequest getUserInfoUpRequest(){
        UserInfoUpRequest userInfoUpRequest = new UserInfoUpRequest();
        userInfoUpRequest.setDepartment(getDepartment());
        userInfoUpRequest.setProvince(getProvince());
        userInfoUpRequest.setCity(getCity());
        userInfoUpRequest.setCountry(getCountry());
        userInfoUpRequest.setAddress(getAddress());
        userInfoUpRequest.setArea(getArea());
        userInfoUpRequest.setTel(getTel());
        userInfoUpRequest.setEmail(getEmail());
        userInfoUpRequest.setRealname(getRealName());
        userInfoUpRequest.setCategory(getCategory());
        userInfoUpRequest.setSign(getSignature());
        userInfoUpRequest.setHobby(getHobby());
        userInfoUpRequest.setIncome(getIncome());
        return userInfoUpRequest;
    }


    public static void save(String department,String province,String city,String country,String address
    ,String area,String tel,String email,String realname,String category
            ,String sign,String hobby,int income){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.DEPARTMENT,department);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.PROVINCE,province);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.CITY,city);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.COUNTRY,country);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.ADDRESS,address);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.AREA,area);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.TEL,tel);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.EMAIL,email);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.REALNAME,realname);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.CATEGORY,category);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.SIGN,sign);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserNormal.HOBBY,hobby);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putInt(AppConst.UserNormal.INCOME,income);
    }

    public static void clear() {
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.DEPARTMENT);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.PROVINCE);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.CITY);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.COUNTRY);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.ADDRESS);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.AREA);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.TEL);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.EMAIL);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.REALNAME);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.CATEGORY);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.SIGN);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.HOBBY);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserNormal.INCOME);
    }
}
