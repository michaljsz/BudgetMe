package com.michalj.bugdetme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Settings extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Random r = new Random();
        final DBManager dbManager = new DBManager(this);
        dbManager.open();
        final SharedPreferences pref = this.getApplicationContext().getSharedPreferences(DatabaseHelper.SHARED_PREFERENCES_NAME, 0);
        final EditText monthlyBudget = findViewById(R.id.editTextMonthlyBudget);
        final EditText savingsGoal = findViewById(R.id.editTextSavingsGoal);
        monthlyBudget.setText(String.valueOf(pref.getFloat(DatabaseHelper.MONTHLY_BUDGET,0)));
        savingsGoal.setText(String.valueOf(pref.getFloat(DatabaseHelper.SAVINGS_GOAL,0)));

        Button submitButton = findViewById(R.id.submitBudgetAndGoal);
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putFloat(DatabaseHelper.MONTHLY_BUDGET,Float.parseFloat(monthlyBudget.getText().toString()));
                editor.putFloat(DatabaseHelper.SAVINGS_GOAL,Float.parseFloat(savingsGoal.getText().toString()));
                editor.apply();
                Toast.makeText(Settings.this,"Changes saved",Toast.LENGTH_SHORT).show();
            }
        });

        Button backupButton = findViewById(R.id.dbBackupButton);
        backupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                backUpToSD();
                Toast.makeText(Settings.this,getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME).toString(),Toast.LENGTH_SHORT).show();

            }

        });

        Button fillDb = findViewById(R.id.fillDBWithSampleData);
        fillDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        String dt = "2017-05-19";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();

                        for (int i = 0; i < 360; i++) {
                            int amountToInsert = r.nextInt(1000);
                            try {
                                c.setTime(sdf.parse(dt));
                            } catch (ParseException e) {
                            }
                            c.add(Calendar.DATE, 1);  // number of days to add
                            dt = sdf.format(c.getTime());  // dt is now the new date
                            String type = DatabaseHelper.TYPES_OF_EXPENSES.get(r.nextInt(DatabaseHelper.TYPES_OF_EXPENSES.size()-1));
                            String description = String.valueOf(i);
                            dbManager.insert(dt, amountToInsert, type, description);
                        }
                    }
                }).start();
            }
        });
    }

    public void backUpToSD(){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME)+"";
                String backupDBPath = "BudgetMeBackup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getApplicationContext(), "Import Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Import Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
