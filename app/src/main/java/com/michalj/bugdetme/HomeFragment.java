package com.michalj.bugdetme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import org.w3c.dom.Text;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private DBManager dbManager;
    private SharedPreferences pref;
    private double totalAmountDouble;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        dbManager = new DBManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.expensesInCurrentMonth();
        cursor.moveToFirst();
        TextView totalAmount = view.findViewById(R.id.amountInCurrentMonth);
        if ( cursor.getString(0) != null ) {
            totalAmountDouble = Double.parseDouble(cursor.getString(0)) / 100;
            totalAmount.setText("" + totalAmountDouble);
        } else {
            totalAmount.setText(R.string.NoExpensesThisMonth);
        }

        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        double monthlyGoal = pref.getFloat(MainActivity.MONTHLY_GOAL,0);
        TextView remainingToGoal = getActivity().findViewById(R.id.remainingAmountToGoal);
        double remainingFunds = monthlyGoal-totalAmountDouble;
        remainingToGoal.setText(String.format("%.2f",(remainingFunds)));

        TextView goalIndex = getActivity().findViewById(R.id.indexToGoal);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        goalIndex.setText(String.format("%.2f",((totalAmountDouble)/(monthlyGoal/30.0*day)*100))+" %");
        if ( ((totalAmountDouble)/(monthlyGoal/30.0*day)*100) < 100 ) {
            goalIndex.setTextColor(Color.parseColor("#008000"));
        } else {
            goalIndex.setTextColor(Color.parseColor("#FF0000"));
        }


        Button addNew = view.findViewById(R.id.addNew);
        addNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().finish();
                Intent myIntent = new Intent(getActivity(),
                        AddNewExpense.class);
                startActivity(myIntent);
            }
        });

    }
}
