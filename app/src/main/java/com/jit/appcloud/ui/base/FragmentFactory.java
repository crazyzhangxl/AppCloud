package com.jit.appcloud.ui.base;

import com.jit.appcloud.ui.fragment.CultivateFragment;
import com.jit.appcloud.ui.fragment.MessageFragment;
import com.jit.appcloud.ui.fragment.NewsFragment;
import com.jit.appcloud.ui.fragment.MeFragment;
import com.jit.appcloud.ui.fragment.center.CtAmUserFragment;
import com.jit.appcloud.ui.fragment.center.CtFarmLogInFragment;
import com.jit.appcloud.ui.fragment.center.CtMgUserFragment;

/**
 *
 * @author zxl
 * @date 2018/4/17
 */

public class FragmentFactory {

    public static FragmentFactory mInstance;

    private FragmentFactory(){}
    // 双重检验模式
    public static FragmentFactory getInstance() {
        if (mInstance == null) {
            synchronized (FragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new FragmentFactory();
                }
            }
        }
        return mInstance;
    }

    private MessageFragment mMessageFragment;
    private NewsFragment mNewsFragment;
    private CultivateFragment mCultivateFragment;
    private MeFragment mMeFragment;
    private CtMgUserFragment mCtMgUserFragment;
    private CtFarmLogInFragment mCtFarmLogInFragment;
    private CtAmUserFragment mCtAmUserFragment;

    public MessageFragment getMessageFragment(){
        if (mMessageFragment == null){
            synchronized (FragmentFactory.class){
                if (mMessageFragment == null){
                    mMessageFragment = new MessageFragment();
                }
            }
        }
        return mMessageFragment;
    }

    public NewsFragment getNewsFragment(){
        if (mNewsFragment == null){
            synchronized (FragmentFactory.class){
                if (mNewsFragment == null){
                    mNewsFragment = new NewsFragment();
                }
            }
        }
        return mNewsFragment;
    }



    /* 中间的界面*/
    public CtMgUserFragment getCtMgUserFragment(){
        if (mCtMgUserFragment == null){
            synchronized (FragmentFactory.class){
                if (mCtMgUserFragment == null){
                    mCtMgUserFragment = new CtMgUserFragment();
                }
            }
        }
        return mCtMgUserFragment;
    }


    public CtFarmLogInFragment getCtFarmLogInFragment(){
        if (mCtFarmLogInFragment == null){
            synchronized (FragmentFactory.class){
                if (mCtFarmLogInFragment == null){
                    mCtFarmLogInFragment = new CtFarmLogInFragment();
                }
            }
        }
        return mCtFarmLogInFragment;
    }

    public CtAmUserFragment getCtAmUserFragment(){
        if (mCtAmUserFragment == null){
            synchronized (FragmentFactory.class){
                if (mCtAmUserFragment == null){
                    mCtAmUserFragment = new CtAmUserFragment();
                }
            }
        }
        return mCtAmUserFragment;
    }

    public CultivateFragment getCultivateFragment(){
        if (mCultivateFragment == null){
            synchronized (FragmentFactory.class){
                if (mCultivateFragment == null){
                    mCultivateFragment = new CultivateFragment();
                }
            }
        }
        return mCultivateFragment;
    }

    public MeFragment getMeFragment(){
        if (mMeFragment == null){
            synchronized (FragmentFactory.class){
                if (mMeFragment == null){
                    mMeFragment = new MeFragment();
                }
            }
        }
        return mMeFragment;
    }

}
