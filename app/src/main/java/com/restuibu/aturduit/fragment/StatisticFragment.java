package com.restuibu.aturduit.fragment;

import java.util.ArrayList;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.restuibu.aturduit.R;
import com.restuibu.aturduit.custom.DayAxisValueFormatter;
import com.restuibu.aturduit.custom.MonthAxisValueFormatter;
import com.restuibu.aturduit.custom.MyAxisValueFormatter;
import com.restuibu.aturduit.custom.XYMarkerView;

import com.restuibu.aturduit.custom.YearAxisValueFormatter;
import com.restuibu.aturduit.model.History;
import com.restuibu.aturduit.model.MySQLiteHelper;
import static com.restuibu.aturduit.util.Util.helper;

import static com.restuibu.aturduit.util.Util.getDayOfYear;

public class StatisticFragment extends Fragment implements OnChartValueSelectedListener {

    private ArrayList<History> listHistory = new ArrayList<History>();
    private BarChart mChart;
    private LineChart mChart2;
    private Button showChart;

    private int jenisChart;
    private int jenisWaktu;
    private XYSeries incomeSeries;
    private XYMultipleSeriesDataset dataset;
    private XYSeriesRenderer incomeRenderer;
    private XYMultipleSeriesRenderer multiRenderer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_statistic_new,
                container, false);


        mChart = (BarChart) rootView.findViewById(R.id.chart);
        mChart2 = (LineChart) rootView.findViewById(R.id.chart2);
        showChart = (Button) rootView.findViewById(R.id.bShow);

        mChart.setOnChartValueSelectedListener(this);

        // add interstitial
//		if (SplashActivity.mInterstitialAd.isLoaded()) {
//			SplashActivity.mInterstitialAd.show();
//		}
//		SplashActivity.mInterstitialAd.setAdListener(new AdListener() {
//			@Override
//			public void onAdClosed() {
//				MainActivity
//						.loadInterstitial(getActivity());
//			}
//		});


        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //helper = new MySQLiteHelper(getActivity());

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.statistic_array,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.option_chart_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                switch (arg2) {
                    case 1:
                        jenisWaktu = 1;
                        break;

                    case 2:

                        jenisWaktu = 2;

                        break;

                    case 3:
                        jenisWaktu = 3;
                        //chart.addView(createGraph());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                switch (arg2) {
                    case 1:
                        jenisChart = 1;
                        break;

                    case 2:
                        jenisChart = 2;
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        showChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IAxisValueFormatter xAxisFormatter;

                switch (jenisWaktu) {
                    case 1:
                        listHistory = helper.getAllHistory();

                        if (jenisChart == 1) {
                            xAxisFormatter = new DayAxisValueFormatter(mChart);
                            mChart.setVisibility(View.VISIBLE);
                            mChart2.setVisibility(View.GONE);
                            createGraph("daily", xAxisFormatter);
                        } else {
                            xAxisFormatter = new DayAxisValueFormatter(mChart2);
                            mChart.setVisibility(View.GONE);
                            mChart2.setVisibility(View.VISIBLE);
                            createGraphLine("daily", xAxisFormatter);
                        }
                        break;
                    case 2:

                        listHistory = helper.getAllMonthlyHistory();

//                        Toast.makeText(getActivity(), listHistory.get(0).getTanggal(), Toast.LENGTH_SHORT).show();
                        if (jenisChart == 1) {
                            xAxisFormatter = new MonthAxisValueFormatter(mChart);
                            mChart.setVisibility(View.VISIBLE);
                            mChart2.setVisibility(View.GONE);
                            createGraph("monthly", xAxisFormatter);
                        } else {
                            xAxisFormatter = new MonthAxisValueFormatter(mChart2);
                            mChart.setVisibility(View.GONE);
                            mChart2.setVisibility(View.VISIBLE);
                            createGraphLine("monthly", xAxisFormatter);
                        }
                        break;
                    case 3:
                        listHistory = helper.getAllYearlyHistory();

//                        Toast.makeText(getActivity(), listHistory.get(0).getTanggal(), Toast.LENGTH_SHORT).show();
                        if (jenisChart == 1) {
                            xAxisFormatter = new YearAxisValueFormatter(mChart);
                            mChart.setVisibility(View.VISIBLE);
                            mChart2.setVisibility(View.GONE);
                            createGraph("yearly", xAxisFormatter);
                        } else {
                            xAxisFormatter = new YearAxisValueFormatter(mChart2);
                            mChart.setVisibility(View.GONE);
                            mChart2.setVisibility(View.VISIBLE);
                            createGraphLine("yearly", xAxisFormatter);
                        }
                        break;

                }


            }
        });

        return rootView;
    }

    private void createGraph(String x, IAxisValueFormatter AxisValueFormatter) {
        // We start creating the XYSeries to plot the temperature
        // Creating an XYSeries for Income
        // CategorySeries incomeSeries = new CategorySeries("Income");

        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(1);
        xAxis.setValueFormatter(AxisValueFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(getActivity(), AxisValueFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        setData(x, listHistory);

    }

    private void setData(String flag, ArrayList<History> histories) {
        //int count, float range
        int count = histories.size();
        //double range =

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float val = Float.parseFloat(histories.get(i).getTotal());

            if (flag.equals("daily")) {

                yVals1.add(new BarEntry(getDayOfYear(histories.get(i).getTanggal()), val));
            } else if (flag.equals("monthly")) {
                String[] x = histories.get(i).getTanggal().split("/");
                String y = x[1] + x[0];
//                Toast.makeText(getActivity(), y, Toast.LENGTH_SHORT).show();
                float z = Float.parseFloat(y);

                yVals1.add(new BarEntry(z, val));
            } else if (flag.equals("yearly")) {
                float z = Float.parseFloat(histories.get(i).getTanggal());
//                Toast.makeText(getActivity(), Float.toString(z), Toast.LENGTH_SHORT).show();
                yVals1.add(new BarEntry(z, val));
            }

            //Toast.makeText(getActivity(), day[0], Toast.LENGTH_SHORT).show();
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);


        } else {
            set1 = new BarDataSet(yVals1, "The year 2016");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);

        }

        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }


    private void createGraphLine(String x, IAxisValueFormatter AxisValueFormatter) {
        // We start creating the XYSeries to plot the temperature
        // Creating an XYSeries for Income
        // CategorySeries incomeSeries = new CategorySeries("Income");

        mChart2.setOnChartValueSelectedListener(this);
        mChart2.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart2.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart2.setPinchZoom(false);

        mChart2.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(1);
        xAxis.setValueFormatter(AxisValueFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart2.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart2.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);

        Legend l = mChart2.getLegend();
        l.setEnabled(false);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(getActivity(), AxisValueFormatter);
        mv.setChartView(mChart2); // For bounds control
        mChart2.setMarker(mv); // Set the marker to the chart

        setDataLine(x, listHistory);

    }

    private void setDataLine(String flag, ArrayList<History> histories) {
        //int count, float range
        int count = histories.size();
        //double range =

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = Float.parseFloat(histories.get(i).getTotal());

            if (flag.equals("daily")) {
                yVals1.add(new BarEntry(getDayOfYear(histories.get(i).getTanggal()), val));
            } else if (flag.equals("monthly")) {
                String[] x = histories.get(i).getTanggal().split("/");
                String y = x[1] + x[0];
                float z = Float.parseFloat(y) + (float)0.4;

                yVals1.add(new BarEntry(z, val));
            } else if (flag.equals("yearly")) {
                float z = Float.parseFloat(histories.get(i).getTanggal()) + (float)0.4;

                yVals1.add(new BarEntry(z, val));
            }

            //Toast.makeText(getActivity(), day[0], Toast.LENGTH_SHORT).show();
        }

        LineDataSet set1;

        if (mChart2.getData() != null &&
                mChart2.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart2.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);


        } else {
            set1 = new LineDataSet(yVals1, "The year 2016");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            mChart2.setData(data);
        }

        mChart2.getData().notifyDataChanged();
        mChart2.notifyDataSetChanged();
        mChart2.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
