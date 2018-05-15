package com.michalj.bugdetme;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private DBManager dbManager;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_stats, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        dbManager = new DBManager(getActivity());
        dbManager.open();

        PieChart pieChart = getActivity().findViewById(R.id.typePieChart);
        pieChart.setUsePercentValues(true);

        List<Entry> typeValues = new ArrayList();
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
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(false);
        data.setValueTextSize(18f);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(1000, 1000);

    }


}
