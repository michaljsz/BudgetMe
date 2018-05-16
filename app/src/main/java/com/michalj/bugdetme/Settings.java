package com.michalj.bugdetme;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Settings extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button backupButton = findViewById(R.id.dbBackupButton);
        backupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                backUpToSD();
                Toast.makeText(Settings.this,getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME).toString(),Toast.LENGTH_SHORT).show();

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
