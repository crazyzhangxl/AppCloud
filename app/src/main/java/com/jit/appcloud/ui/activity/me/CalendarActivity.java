package com.jit.appcloud.ui.activity.me;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jit.appcloud.R;
import com.jit.appcloud.ui.activity.calendar.LunarDecorator;
import com.jit.appcloud.ui.activity.calendar.TodayDecorator;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.Lunar;
import com.jit.appcloud.util.TimeUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import java.util.Date;
import butterknife.BindView;
public class CalendarActivity extends BaseActivity implements OnDateSelectedListener, OnMonthChangedListener {


    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.flToolBar)
    FrameLayout mFlToolBar;
    @BindView(R.id.calendarView_calendar)
    MaterialCalendarView mCalendarViewCalendar;
    @BindView(R.id.tv_time)
    TextView mTvTime;

    private CalendarDay today;
    public String year;
    public String month;
    public LunarDecorator mLunarDecorator;
    @Override
    protected void init() {
        today = CalendarDay.today();
        year = TimeUtil.date2String(today.getDate(), "yyyy");
        month = TimeUtil.date2String(today.getDate(), "MM");
        mLunarDecorator = new LunarDecorator(year,month);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText("日历");
        initTodayInfo(today.getDate());
        mCalendarViewCalendar.setCurrentDate(today);
        mCalendarViewCalendar.setShowOtherDates(MaterialCalendarView.SHOW_DEFAULTS);

        mCalendarViewCalendar.addDecorators(new TodayDecorator(),mLunarDecorator);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> finish());
        mCalendarViewCalendar.setAllowClickDaysOutsideCurrentMonth(true);
        mCalendarViewCalendar.setOnDateChangedListener(this);
        mCalendarViewCalendar.setOnMonthChangedListener(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @SuppressLint("SetTextI18n")
    private void initTodayInfo(Date selectDate){
        Lunar lunar = new Lunar(selectDate);
        mTvTime.setText("农历 "+lunar.toString());
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        initTodayInfo(date.getDate());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
