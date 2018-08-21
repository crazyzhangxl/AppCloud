package com.jit.appcloud.ui.view;

import com.jit.appcloud.model.response.HeWeatherResponse;

/**
 *
 * @author zxl
 * @date 2018/4/23
 */

public interface IWeatherAtView {
    void querySuccessful(HeWeatherResponse heWeatherResponse);
    void queryFailed();
}
