package com.jit.appcloud.ui.base;

import com.jit.appcloud.ui.fragment.message.MsgContactsFragment;
import com.jit.appcloud.ui.fragment.message.MsgRecentFragment;
import com.jit.appcloud.ui.fragment.message.MsgCrowdFragment;
import com.jit.appcloud.ui.fragment.message.MsgFindFragment;

/**
 *
 * @author 张先磊
 * @date 2018/4/27
 */

public class MsgFragmentFactory {

    private static MsgFragmentFactory mInstance;
    private MsgFragmentFactory(){}

    public static MsgFragmentFactory getInstance(){
        if (mInstance == null){
            synchronized (MsgFragmentFactory.class){
                if (mInstance == null){
                    mInstance = new MsgFragmentFactory();
                }
            }
        }
        return mInstance;
    }

    private MsgContactsFragment mMsgContactsFragment;
    private MsgCrowdFragment mMsgCrowdFragment;
    private MsgRecentFragment mMsgRecentFragment;
    private MsgFindFragment mMsgFindFragment;

    public MsgContactsFragment getMsgContactsFragment(){
        if (mMsgContactsFragment == null){
            synchronized (MsgFragmentFactory.class){
                if (mMsgContactsFragment == null){
                    mMsgContactsFragment = new MsgContactsFragment();
                }
            }
        }
        return mMsgContactsFragment;
    }

    public MsgCrowdFragment getMsgCrowdFragment(){
        if (mMsgCrowdFragment == null){
            synchronized (MsgFragmentFactory.class){
                if (mMsgCrowdFragment == null){
                    mMsgCrowdFragment = new MsgCrowdFragment();
                }
            }
        }
        return mMsgCrowdFragment;
    }

    public MsgRecentFragment getMsgRecentFragment(){
        if (mMsgRecentFragment == null){
            synchronized (MsgFragmentFactory.class){
                if (mMsgRecentFragment == null){
                    mMsgRecentFragment = new MsgRecentFragment();
                }
            }
        }
        return mMsgRecentFragment;
    }

    public MsgFindFragment getMsgFindFragment(){
        if (mMsgFindFragment == null){
            synchronized (MsgFragmentFactory.class){
                if (mMsgFindFragment == null){
                    mMsgFindFragment = new MsgFindFragment();
                }
            }
        }
        return mMsgFindFragment;
    }

}
