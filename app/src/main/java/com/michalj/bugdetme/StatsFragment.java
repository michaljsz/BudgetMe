package com.michalj.bugdetme;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

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
//        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setColors(chartColors);
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(false);
        data.setValueTextSize(18f);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(1000, 1000);

    }


}
