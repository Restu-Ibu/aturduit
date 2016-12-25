package com.restuibu.aturduit.custom;

import android.widget.Toast;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.restuibu.aturduit.model.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by philipp on 02/06/16.
 */
public class MonthAxisValueFormatter implements IAxisValueFormatter
{

    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private BarLineChartBase<?> chart;

    public MonthAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String date = Integer.toString((int)Math.round(value));

        int month = Integer.parseInt(date.substring(4));
        String year = date.substring(0,4);
        String month_str = mMonths[month - 1];

        return month_str+" "+year;
    }


}
