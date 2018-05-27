package com.michalj.bugdetme;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private final int[] chartColors = {Color.rgb(207, 248, 246),
            Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(15, 127, 127),
            Color.rgb(42, 109, 130),
            Color.rgb(0, 128, 128)};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_stats, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        DBManager dbManager = new DBManager(getActivity());
        dbManager.open();

        // Expenses this month by type Pie chart
        PieChart pieChart = getActivity().findViewById(R.id.typePieChart);
        pieChart.setUsePercentValues(true);

        List<Entry> typeValues = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        // Loop for aggregating sum of expanses by types
        int i=0;
        Cursor cursorInLoop;
        for ( String x : DatabaseHelper.TYPES_OF_EXPENSES) {
            cursorInLoop = dbManager.typeSumCurrentMonth(x);
            cursorInLoop.moveToFirst();
            double sum = Double.parseDouble(cursorInLoop.getString(0))/100.0;
            float sumF = (float) sum;
            if ( sumF != 0 ) {
                typeValues.add(new Entry(sumF, i));
                labels.add(x);
                i++;
            }
        }

        // Creating and configuring piechart
        PieDataSet dataSet = new PieDataSet(typeValues, "Spending by type");
        PieData data = new PieData(labels, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        dataSet.setColors(chartColors);
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(false);
        data.setValueTextSize(18f);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(1000, 1000);


        //Expenses by month Bar chart
        BarChart barChart = getActivity().findViewById(R.id.yearBarChart);
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEPT");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        BarDataSet barDataSet2 = new BarDataSet(valueSet1, "Months");
        barDataSet2.setColors(chartColors);
        barDataSet2.setValueTextColor(Color.WHITE);
        barDataSet2.setValueTextSize(12f);


        BarData barData = new BarData(xAxis, barDataSet2);
        barChart.setData(barData);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setTextColor(Color.WHITE);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.getLegend().setEnabled(false);
        barChart.setDescription("");
        barChart.animateXY(2500, 2500);
        barChart.invalidate();


    }


}
