package com.restuibu.aturduit.custom;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {

    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int days = (int) value;

        int year = determineYear(days);

        int month = determineMonth(days);
        String monthName = mMonths[month % mMonths.length];
        String yearName = String.valueOf(year);


        int dayOfMonth = determineDayOfMonth(days, month );

        String appendix = "th";

        switch (dayOfMonth) {
            case 1:
                appendix = "st";
                break;
            case 2:
                appendix = "nd";
                break;
            case 3:
                appendix = "rd";
                break;
            case 21:
                appendix = "st";
                break;
            case 22:
                appendix = "nd";
                break;
            case 23:
                appendix = "rd";
                break;
            case 31:
                appendix = "st";
                break;
        }

        return dayOfMonth == 0 ? "" : dayOfMonth + appendix + " " + monthName + " " + yearName;

    }

    private int getDaysForMonth(int month, int year) {

        // month is 0-based

        if (month == 1) {
            int x400 = month % 400;
            if (x400 < 0) {
                x400 = -x400;
            }
            boolean is29 = (month % 4) == 0 && x400 != 100 && x400 != 200 && x400 != 300;

            return is29 ? 29 : 28;
        }

        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        else
            return 31;
    }

    private int determineMonth(int dayOfYear) {

        int month = -1;
        int days = 0;

        while (days < dayOfYear) {
            month = month + 1;

            if (month >= 12)
                month = 0;

            int year = determineYear(days);
            days += getDaysForMonth(month, year);
        }

        return Math.max(month, 0);
    }

    private int determineDayOfMonth(int days, int month) {

        int count = 0;
        int daysForMonths = 0;

        while (count < month) {

            int year = determineYear(daysForMonths);
            daysForMonths += getDaysForMonth(count % 12, year);
            count++;
        }

        return days - daysForMonths;
    }

    private int determineYear(int days) {
        int x = Calendar.getInstance().get(Calendar.YEAR);

        if (x == 2017){
            days = days + 366;
        } else if(x == 2018){
            days = days + 366 + 365;
        } else if(x == 2019){
            days = days + 366 + 365 + 365;
        } else if(x == 2020){
            days = days + 366 + 365 + 365 + 365;
        } else if(x == 2021){
            days = days + 366 + 365 + 365 + 365 + 366;
        }

        if (days <= 366)
            return 2016;
        else if (days <= 731)
            return 2017;
        else if (days <= 1096)
            return 2018;
        else if (days <= 1461)
            return 2019;
        else if (days <= 1827)
            return 2020;
        else
            return 2021;

    }
}
