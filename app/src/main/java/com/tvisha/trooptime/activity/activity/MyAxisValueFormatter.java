package com.tvisha.trooptime.activity.activity;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter extends ValueFormatter {

    String temp = "";
    private DecimalFormat mFormat;
    private float max;


    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        max = axis.getAxisMaximum();
        temp = "";

        if (value >= 1)
            temp = (int) value + " am ";


        return temp;
        //return mFormat.format(value) + " ltrs";

    }
}
