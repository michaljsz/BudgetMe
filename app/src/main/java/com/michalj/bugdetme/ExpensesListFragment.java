package com.michalj.bugdetme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
    static ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_expenses_list, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, DatabaseHelper.TYPES_OF_EXPENSES);
        listView = view.findViewById(R.id.expanses_list);
        listView.setEmptyView(view.findViewById(R.id.empty));

        final DBManager dbManager = new DBManager(getActivity());
        dbManager.open();
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, dbManager.fetch(), from, to, 0);
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
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {

                LayoutInflater inflater = LayoutInflater
                        .from(getActivity());
                final View updateView = inflater.inflate(R.layout.modify_expense,
                        null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(updateView);
                final Cursor currentCursor = (Cursor) listView.getItemAtPosition(position);

                typeSpinner = updateView.findViewById(R.id.modifyTypeSpinner);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typeSpinner.setAdapter(dataAdapter);

                final long _id = Long.parseLong(currentCursor.getString(0));

                int locationIdForSpinner = DatabaseHelper.TYPES_OF_EXPENSES.indexOf(currentCursor.getString(3));
                typeSpinner.setSelection(locationIdForSpinner);

                final EditText changeAmount = updateView.findViewById(R.id.amountEdit);
                changeAmount.setText(String.valueOf(Double.parseDouble(currentCursor.getString(2))/100));

                final EditText changeDescription = updateView.findViewById(R.id.descriptionEdit);
                changeDescription.setText(currentCursor.getString(4));

                final Button changeDate = updateView.findViewById(R.id.changeDateButton);
                ((MainActivity)getActivity()).setChosenData(currentCursor.getString(1));
                changeDate.setText(currentCursor.getString(1));
                changeDate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        DialogFragment newDateFragment = new DatePickerFragment();
                        newDateFragment.show(getActivity().getSupportFragmentManager(), "");
                    }
                });
                final AlertDialog alert = builder.create();


                Button update = updateView.findViewById(R.id.updateButton);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String date = ((MainActivity)getActivity()).getChosenDate();
                        final double amount = Double.parseDouble(changeAmount.getText().toString())*100;
                        final String type = typeSpinner.getSelectedItem().toString();
                        final String desc = changeDescription.getText().toString();
                        new Thread(new Runnable(){
                            @Override
                            public void run(){
                                dbManager.update(_id, date, amount, type, desc);
                            }
                        }).start();
//                        dbManager.update(_id, date, amount, type, desc);
                        alert.dismiss();
                        adapter.changeCursor(dbManager.fetch());
                    }
                });

                Button delete = updateView.findViewById(R.id.deleteButton);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long _id = Long.parseLong(currentCursor.getString(0));
                        dbManager.delete(_id);
                        alert.dismiss();
                        adapter.changeCursor(dbManager.fetch());
                    }
                });
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

    @Override
    public void onDestroyView() {
        ((SimpleCursorAdapter) listView.getAdapter()).getCursor().close();
        super.onDestroyView();
    }

}
