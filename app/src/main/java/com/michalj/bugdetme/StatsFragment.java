package com.michalj.bugdetme;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.List;

public class StatsFragment extends Fragment {

    private final int[] chartColors = {Color.rgb(207, 248, 246),
            Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(15, 127, 127),
            Color.rgb(42, 109, 130),
            Color.rgb(0, 128, 128)};
    private DBManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_stats, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        dbManager = new DBManager(getActivity());
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


        // Expenses by month Bar chart
        BarChart barChart = getActivity().findViewById(R.id.yearBarChart);
        final ArrayList<String> xAxis = new ArrayList<>();
        final ArrayList<BarEntry> valueSet = new ArrayList<>();

        // Getting first month for graph
        final Calendar c = Calendar.getInstance();
        int currMonth = c.get(Calendar.MONTH) + 2;
        int currYear = c.get(Calendar.YEAR) - 1;

        Cursor monthsCursor;

        // Loop for inserting label names and sum of expenses from last 12 months
        for (int j = 0; j < 12; j++) {
            xAxis.add(theMonth(currMonth));
            if ( currMonth < 10) {
                monthsCursor = dbManager.expensesInMonth(String.valueOf(currYear),"0" + String.valueOf(currMonth));
            } else {
                monthsCursor = dbManager.expensesInMonth(String.valueOf(currYear),String.valueOf(currMonth));
            }
            monthsCursor.moveToFirst();

            String expenses = monthsCursor.getString(0);
            if ( expenses != null  ) {
                float expensesFloat = Float.parseFloat(expenses)/100;
                BarEntry value = new BarEntry(expensesFloat, j);
                valueSet.add(value);
            }
            if ( currMonth == 12 ) {
                currYear++;
            }
            if ( currMonth < 12 ) {
                currMonth ++;
            } else {
                currMonth = 1;
            }
        }

        BarDataSet barDataSet = new BarDataSet(valueSet, "Months");
        barDataSet.setColors(chartColors);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(12f);


        BarData barData = new BarData(xAxis, barDataSet);
        barChart.setData(barData);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.getLegend().setEnabled(false);
        barChart.setDescription("");
        barChart.animateXY(2500, 2500);
        barChart.invalidate();


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onDestroyView() {
        dbManager.close();
        super.onDestroyView();
    }

    /**
     * Used for getting Label names for bar chart
     * @param month - number of month from 1 to 12
     * @return Short name of month
     */
    public static String theMonth(int month){
        String[] monthNames = {"","JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEPT", "OCT", "NOV", "DEC"};
        return monthNames[month];
    }
}
