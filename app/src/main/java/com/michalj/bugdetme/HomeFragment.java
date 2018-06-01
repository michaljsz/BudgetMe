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


import java.util.Calendar;

public class HomeFragment extends Fragment {

    private double totalAmountDouble;
    private DBManager dbManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        dbManager = new DBManager(getActivity());
        dbManager.open();

        // Getting total expenses in current month
        Cursor cursor = dbManager.expensesInCurrentMonth();
        cursor.moveToFirst();
        TextView totalAmount = view.findViewById(R.id.amountInCurrentMonth);
        if ( cursor.getString(0) != null ) {
            totalAmountDouble = Double.parseDouble(cursor.getString(0)) / 100;
            totalAmount.setText(String.format("%.2f",totalAmountDouble)+getString(R.string.PLN));
        } else {
            totalAmount.setText("0");
        }

        // Getting remaining funds in current month
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(DatabaseHelper.SHARED_PREFERENCES_NAME, 0);
        double monthlyGoal = pref.getFloat(DatabaseHelper.MONTHLY_BUDGET,0);
        TextView remainingToGoal = getActivity().findViewById(R.id.remainingAmountToGoal);
        double remainingFunds = monthlyGoal-totalAmountDouble;
        remainingToGoal.setText(String.format("%.2f",(remainingFunds))+getString(R.string.PLN));


        // Setting goal index
        TextView goalIndex = getActivity().findViewById(R.id.indexToGoal);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (totalAmountDouble != 0 ) {
            goalIndex.setText(String.format("%.2f",((totalAmountDouble)/(monthlyGoal/30.0*day)*100))+getString(R.string.percent));
        } else {
            goalIndex.setText("0");

        }
        // Setting green or red color for index
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
}
