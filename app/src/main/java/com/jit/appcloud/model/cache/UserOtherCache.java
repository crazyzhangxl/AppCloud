package com.jit.appcloud.model.cache;

import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.util.SharePreferenceUtils;
import com.jit.appcloud.util.UIUtils;

import retrofit2.http.PUT;

/**
 * @author zxl on 2018/6/5.
 *         discription:
 */

public class UserOtherCache {
    public static void setFarmSelectedName(String farmName){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserOther.FARMER_NAME,farmName);
    }

    public static String getFarmSelectedName(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserOther.FARMER_NAME,null);

    }

    public static void setFeedMealSelected(String mealSelected){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserOther.FEED_MEAL_SELECTED,mealSelected);
    }

    public static String getFeedMealSelected(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserOther.FEED_MEAL_SELECTED,"");
    }


    public static void setSeedMealSelected(String mealSelected){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserOther.SEED_MEAL_SELECTED,mealSelected);
    }

    public static void setNewsIsCached(boolean isCached){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putBoolean(AppConst.UserOther.NEWS_IS_CACHE,isCached);

    }

    public static boolean getNewsIsCached(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getBoolean(AppConst.UserOther.NEWS_IS_CACHE,false);
    }



    public static void setNewsPbSort(String mNewsSort){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).putString(AppConst.UserOther.NEWS_PUBLISH_SORT,mNewsSort);
    }



    public static String getNewsPbSort(){
       return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserOther.NEWS_PUBLISH_SORT,"");
    }

    public static String getSeedMealSelected(){
        return SharePreferenceUtils.getInstance(UIUtils.getContext()).getString(AppConst.UserOther.SEED_MEAL_SELECTED,"");
    }



    public static void clear(){
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserOther.FARMER_NAME);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserOther.FEED_MEAL_SELECTED);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserOther.SEED_MEAL_SELECTED);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserOther.NEWS_PUBLISH_SORT);
        SharePreferenceUtils.getInstance(UIUtils.getContext()).remove(AppConst.UserOther.NEWS_IS_CACHE);
    }
}
