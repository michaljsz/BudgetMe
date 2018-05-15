package com.michalj.bugdetme;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String chosenType, amountString;
    private double amountDouble;
    private int amountInt;
    private DBManager dbManager;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expense);

        final Spinner typeSpinner = findViewById(R.id.typeSpinner);
        typeSpinner.setOnItemSelectedListener(this);

        final TextView descriptionEditText = findViewById(R.id.descriptionInput);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DatabaseHelper.TYPES_OF_EXPENSES);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(dataAdapter);

        final EditText amountEditText = findViewById(R.id.amountInput);

        dbManager = new DBManager(this);
        dbManager.open();

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                amountString = amountEditText.getText().toString();
                if (!amountString.isEmpty() && amountString.length() > 0) {
                    try {
                        amountDouble = Double.parseDouble(amountString)*100;
                        amountInt = (int) amountDouble;
                    } catch (NumberFormatException e1) {
                        e1.printStackTrace();
                    }
                    if ( amountDouble <= 0 ) {
                        Toast.makeText(AddNewExpense.this, "You have to enter positive amount", Toast.LENGTH_LONG).show();
                    } else {
                        Date todayDate = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        final String date = formatter.format(todayDate);
                        final String type = typeSpinner.getSelectedItem().toString();
                        description = descriptionEditText.getText().toString();
                        dbManager.insert(date, amountInt, type, description);

                        if (chosenType == "Transport") {
                            alertFormElements();
                        } else {
                            finish();
                            Intent myIntent = new Intent(getBaseContext(),
                                    MainActivity.class);
                            startActivity(myIntent);
                        }
                    }
                } else {
                    Toast.makeText(AddNewExpense.this, "You have to enter amount", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent myIntent = new Intent(getBaseContext(),
                MainActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void alertFormElements() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.car_data,
                null, false);
        final EditText carMileage = formElementsView
                .findViewById(R.id.carMileage);
        final CheckBox fuelCheckBox = formElementsView
                .findViewById(R.id.myCheckBox);
        final RadioGroup fuelTypeGroup = formElementsView
                .findViewById(R.id.fuelRadioGroup);
        fuelTypeGroup.setVisibility(View.GONE);

        fuelCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fuelCheckBox.isChecked()) {
                    fuelTypeGroup.setVisibility(View.VISIBLE);
                } else if(!fuelCheckBox.isChecked()) {
                    fuelTypeGroup.setVisibility(View.GONE);
                }
            }
        });

        new AlertDialog.Builder(AddNewExpense.this).setView(formElementsView)
                .setTitle("Vehicle data")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {


                        int selectedId = fuelTypeGroup
                                .getCheckedRadioButtonId();

                        RadioButton selectedRadioButton =  formElementsView
                                .findViewById(selectedId);

                        dialog.cancel();
                        finish();
                        Intent myIntent = new Intent(getBaseContext(),
                                HomeFragment.class);
                        startActivity(myIntent);
                    }

                }).show();
    }
}
