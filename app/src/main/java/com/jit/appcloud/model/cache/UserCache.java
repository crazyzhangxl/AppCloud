package com.jit.appcloud.model.cache;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.db.DBManager;
import com.jit.appcloud.util.SharePreferenceUtils;
import com.jit.appcloud.util.UIUtils;

/**
 * 持久化用户基本信息
 * @author zxl
 * @date 2018/5/10
 */

public class UserCache {

    /**
     * @return 用户 id
     */
    public static String getId(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.ID,"");
    }

    /**
     * @return 用户的登陆名 username
     */
    public static String getName(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.Name,"");
    }

    /**
     * @return 维持用户登陆的token
     */
    public static String getToken(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.Token,"");
    }

    public static String getRole(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.ROLE,"");
    }

    public static String getRongToken(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.RONG_TOKEN,"");
    }

    public static void setRongToken(String rongToken){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.RONG_TOKEN,rongToken);
    }

    public static void setHeadImage(String headPath){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.IMAGE_HEAD,headPath);
    }

    public static String getHeadImage(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.IMAGE_HEAD,null);
    }

    public static void setRegisterTime(String time){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.REGISTER_TIME,time);
    }

    public static String getRegisterTime(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.REGISTER_TIME,null);
    }

    public static void setPassword(String pwd){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.PASSWORD,pwd);
    }

    public static String getPassword(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.PASSWORD,null);
    }

    public static void setMsgNotify(boolean isNotify){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putBoolean(AppConst.User.NOTIFY_MESSAGE,isNotify);

    }

    public static  boolean getMsgNotify(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getBoolean(AppConst.User.NOTIFY_MESSAGE,true);

    }

    public static void save(String id,String name,String role,String token,String rongToken,String registerTime,String password,String image){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.ID,id);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.Name,name);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.ROLE,role);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.Token,token);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.RONG_TOKEN,rongToken);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.REGISTER_TIME,registerTime);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.PASSWORD,password);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.IMAGE_HEAD,image);
    }

    public static void clear(){
        // 清除sharedPreference
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.ID);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.Name);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.ROLE);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.Token);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.RONG_TOKEN);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.IMAGE_HEAD);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.REGISTER_TIME);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.PASSWORD);
            SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.NOTIFY_MESSAGE);

        // 清除本地数据数据库 删除所有养殖户的信息,增加塘口时相关
        DBManager.getInstance().deleteAllUserInfo();
    }

}
