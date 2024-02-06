package com.tvisha.trooptime.activity.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tvisha.trooptime.R;

import java.util.ArrayList;

public class StackedBarActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private BarChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);


        mChart = findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setClickable(false);
        mChart.setTouchEnabled(true);


        mChart.getDescription().setEnabled(false);
        mChart.setScaleEnabled(true);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);


        // change the position of the y-labels
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setValueFormatter(new MyAxisValueFormatter());
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawGridLines(false);
        rightAxis.setStartAtZero(true);
        rightAxis.setAxisMaximum(24);
        rightAxis.setAxisMinimum(0);
        rightAxis.setLabelCount(24);
        rightAxis.setGranularity(1f);


        // leftAxis.setLabelCount(3, false);
        //leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(31);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(31);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);


        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);

        // setting data

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
        l.setEnabled(true);

        setData();

        // mChart.setDrawLegend(false);
    }

    private void setData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 1; i < 32; i++) {
            float mult = (10);
            float val1 = (float) (Math.random() * mult) + mult / 1;
            float val2 = (float) (Math.random() * mult) + mult / 2;
            float val3 = (float) (Math.random() * mult) + mult / 3;
            float val4 = (float) (Math.random() * mult) + mult / 4;

            yVals1.add(new BarEntry(
                    i,
                    new float[]{val1, val2, val3, val4},
                    getResources().getDrawable(R.drawable.menu)));
        }

        BarDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setDrawIcons(false);

            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(getResources().getColor(R.color.late_color));
            colors.add(getResources().getColor(R.color.work_color));
            colors.add(getResources().getColor(R.color.break_color));
            colors.add(getResources().getColor(R.color.ot_color));

            set1.setColors(colors);
            set1.setStackLabels(new String[]{"LATE", "WORK", "BREAK", "OT"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.TRANSPARENT);


            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        *//*switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                List<IBarDataSet> sets = mChart.getData()
                        .getDataSets();

                for (IBarDataSet iSet : sets) {

                    BarDataSet set = (BarDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                List<IBarDataSet> sets = mChart.getData()
                        .getDataSets();

                for (IBarDataSet iSet : sets) {

                    BarDataSet set = (BarDataSet) iSet;
                    set.setDrawIcons(!set.isDrawIconsEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleBarBorders: {
                for (IBarDataSet set : mChart.getData().getDataSets())
                    ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                mChart.invalidate();
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                mChart.animateXY(3000, 3000);
                break;
            }
            case R.id.actionSave: {
                if (mChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                    Toast.makeText(StackedBarActivity.this, "Saving SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(StackedBarActivity.this, "Saving FAILED!", Toast.LENGTH_SHORT).show();
                break;
            }
        }*//*
        return true;
    }*/


    @Override
    public void onValueSelected(Entry e, Highlight h) {

       /* BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());*/
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }


}
