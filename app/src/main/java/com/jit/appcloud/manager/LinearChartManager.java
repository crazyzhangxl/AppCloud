package com.jit.appcloud.manager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TimeUtils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jit.appcloud.model.bean.IncomeBean;
import com.jit.appcloud.util.LogUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.LineChartMarkView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxl on 2018/8/7.
 *         discription:
 */

public class LinearChartManager {
    private LineChart lineChart;
    /**
     * X轴
     */
    private XAxis xAxis;
    /**
     * 左侧Y轴
     */
    private YAxis leftYAxis;
    /**
     * 右侧Y轴
     */
    private YAxis rightYAxis;
    /**
     * 图例
     */
    private Legend legend;
    /**
     * 限制线
     */
    private LimitLine limitLine;

    /**
     * 构造方法 -- 初始化
     * @param lineChart 传入lineChart对象
     */

    private List<IncomeBean> mDateList = new ArrayList<>();

    public LinearChartManager(LineChart lineChart){
        this.lineChart = lineChart;
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initChart(lineChart);
    }

    private void initChart(LineChart lineChart) {
        // 是否展示网格
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        // 是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        //lineChart.setDragEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        //是否有触摸事件
        // lineChart.setTouchEnabled(true);
        // 设置XY轴动画效果
        lineChart.animateX(500);
        lineChart.animateY(500);

        // 设置X轴图例
        Description description = new Description();
        //description.setText("需要展示的内容");
        description.setEnabled(false);
        lineChart.setDescription(description);

        // 设置无数据时的显示状态 ------
        lineChart.setNoDataText("Sorry,今日暂无数据");

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();

        xAxis.setDrawGridLines(false);
        // 设置字体斜着显示
        xAxis.setLabelRotationAngle(-60);
        rightYAxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        //设置Y轴网格线为虚线
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        rightYAxis.setEnabled(false);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
        //是否显示
        legend.setEnabled(false);


        /******根据需求的不同 在此在次设置X Y轴的显示内容******/
        // 设置显示的lable数量 X轴显示
        xAxis.setLabelCount(12);
        //是否绘制X轴线
        xAxis.setDrawAxisLine(false);
        leftYAxis.setLabelCount(8);
        // draw a zero line
        leftYAxis.setDrawZeroLine(true);
        leftYAxis.setZeroLineColor(Color.GRAY);
        leftYAxis.setZeroLineWidth(1f);
        leftYAxis.setAxisLineWidth(1f);
        leftYAxis.setAxisLineColor(Color.GRAY);
    }

    public void showLineChart(List<IncomeBean> dataList, String name, int color) {
        // 设置-----具体的点
        this.mDateList = dataList;
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            IncomeBean data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, (float) data.getValue());
            entries.add(entry);
        }


        xAxis.setValueFormatter((value, axis) -> {
            String tradeDate = mDateList.get((int) value % mDateList.size()).getTradeDate();
            return TimeUtil.formatDateToMD(tradeDate);
        });


        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        //LINEAR 折线图  CUBIC_BEZIER 圆滑曲线
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        //线条自定义内容 放在这里

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet,
                                 int color,
                                 LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);

        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView(Context context) {
        LineChartMarkView mv = new LineChartMarkView(context, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }
}
