package com.michalj.bugdetme;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExpensesListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final String[] from = new String[] { DatabaseHelper.DATE, DatabaseHelper.AMOUNT,
            DatabaseHelper.TYPE, DatabaseHelper.DESCRIPTION};
    private final int[] to = new int[] { R.id.dateDisplay, R.id.amountDisplay, R.id.typeIconDisplay,
            R.id.descriptionDisplay};

    static Spinner typeSpinner;
    static String chosenType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_expenses_list, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, DatabaseHelper.TYPES_OF_EXPENSES);
        DBManager dbManager = new DBManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        final ListView listView = view.findViewById(R.id.expanses_list);
        listView.setEmptyView(view.findViewById(R.id.empty));
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, cursor, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {
                if (aColumnIndex == 2) {
                    double amountInZl = Double.parseDouble(aCursor.getString(aColumnIndex))/100;
                    TextView textView = (TextView) aView;
                    textView.setText(String.format("%.2f",amountInZl) + getString(R.string.PLN));
                    return true;
                }
                if (aColumnIndex == 3) {
                    ImageView img = (ImageView) aView;
                    String type = aCursor.getString(aColumnIndex);
                    if (type.equalsIgnoreCase("fmcg")) {
                        img.setImageResource(R.drawable.fmcg);
                    } else if (type.equalsIgnoreCase("Utilities")) {
                        img.setImageResource(R.drawable.utilities);
                    } else if (type.equalsIgnoreCase("car") ||
                            type.equalsIgnoreCase("transport")) {
                        img.setImageResource(R.drawable.car);
                    } else if (type.equalsIgnoreCase("kids")) {
                        img.setImageResource(R.drawable.kids);
                    } else if (type.equalsIgnoreCase("leisure")) {
                            img.setImageResource(R.drawable.leisure);
                    } else if (type.equalsIgnoreCase("health")) {
                        img.setImageResource(R.drawable.health);
                    } else if (type.equalsIgnoreCase("clothes")) {
                        img.setImageResource(R.drawable.clothes);
                    }
                    return true;
                }
                return false;
            }
        });
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {

                LayoutInflater inflater = LayoutInflater
                        .from(getActivity());
                final View formElementsView = inflater.inflate(R.layout.modify_expense,
                        null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(formElementsView);

                typeSpinner = formElementsView.findViewById(R.id.modifyTypeSpinner);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typeSpinner.setAdapter(dataAdapter);

                Button changeDate = formElementsView.findViewById(R.id.changeDateButton);
                changeDate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "");
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateAlert() {

    }
}
