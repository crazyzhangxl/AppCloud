package com.jit.appcloud.ui.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lqr.recyclerview.LQRRecyclerView;

import org.w3c.dom.Text;

/**
 *
 * @author zxl
 * @date 2018/4/17
 *  dexcription: 巡视检查界面的View层
 */

public interface ICultivateFgView {
    /**
     * 经销商身份登录下的用户
     * @return
     */
    RecyclerView getAgencyListRv();

    /**
     * 经销商列表信息 ---
     * @return
     */
    RecyclerView getFarmerListRV();

    /**
     * 经销商下拉选择
     * @return
     */
    MaterialSpinner getSpFarm();

    /**
     * 经销商下列表下拉刷新
     * @return
     */
    SwipeRefreshLayout getAgRefreshLayout();


    /**
     * 经销商时间
     * @return
     */
    TextView tvTimeNow();



    /**
     * 经销商的预览
     */
    TextView employeeDeviceTotal();


    /**
     * 塘口下拉选择
     * @return
     */
    MaterialSpinner getPondSp();


    /**
     * 养殖户的设备
     * @return
     */
    RecyclerView getPondDvList();


    /**
     * 养殖户时间
     * @return
     */
    TextView tvPondTimeNow();

    /**
     * 养殖户 设备总览
     * @return
     */
    TextView farmDeviceTotal();

    /**
     * 养殖户设备下拉刷新
     * @return
     */
    SwipeRefreshLayout getFmSwipeRefreshLayout();


}
