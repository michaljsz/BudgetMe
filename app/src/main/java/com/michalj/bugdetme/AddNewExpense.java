package com.michalj.bugdetme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddNewExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String chosenType;
    private String amountString;
    private String description;
    private String type;
    private String date;
    private int mileage;
    private double amountDouble;
    private int amountInt;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expense);

        final TextView descriptionEditText = findViewById(R.id.descriptionInput);

        final Spinner typeSpinner = findViewById(R.id.typeSpinner);
        typeSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
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
                if (!amountString.isEmpty()) {
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
                        date = formatter.format(todayDate);
                        type = typeSpinner.getSelectedItem().toString();
                        description = descriptionEditText.getText().toString();

                        new Thread(new Runnable(){
                            @Override
                            public void run(){
                                dbManager.insert(date, amountInt, type, description);
                            }
                        }).start();

                        if (chosenType.equalsIgnoreCase("Transport") || chosenType.equalsIgnoreCase("car")) {
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


    // Alert dialog with additional car data
    private void alertFormElements() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.car_data, null, false);
        final EditText carMileage = formElementsView.findViewById(R.id.carMileage);
        final CheckBox fuelCheckBox = formElementsView.findViewById(R.id.fuelCheckBox);
        final EditText fuelVolume = formElementsView.findViewById(R.id.fuelVolume);
        fuelVolume.setVisibility(View.GONE);
        final Button submitCarData = formElementsView.findViewById(R.id.submitCarData);

        fuelCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fuelCheckBox.isChecked()) {
                    fuelVolume.setVisibility(View.VISIBLE);
                } else if(!fuelCheckBox.isChecked()) {
                    fuelVolume.setVisibility(View.GONE);
                }
            }
        });


        AlertDialog alert = new AlertDialog.Builder(AddNewExpense.this).setView(formElementsView).show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        submitCarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mileage = Integer.parseInt(carMileage.getText().toString());
                Cursor getIDCursor = dbManager.getLastId();
                getIDCursor.moveToFirst();

                final int carId = Integer.parseInt(getIDCursor.getString(0));
                final double fuelInLitres = Double.parseDouble(fuelVolume.getText().toString());
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        dbManager.insertCarData(carId, date, amountInt, mileage, fuelInLitres, description);
                    }
                }).start();

                finish();
                Intent myIntent = new Intent(getBaseContext(),
                MainActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
