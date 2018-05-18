package com.michalj.bugdetme;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModifyExpense extends Activity implements OnClickListener {

    private EditText amountEdit;
    private Button updateBtn, deleteBtn;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify expense");

        setContentView(R.layout.modify_expense);

        dbManager = new DBManager(this);
        dbManager.open();

        amountEdit = findViewById(R.id.amountEdit);

        updateBtn = findViewById(R.id.updateButton);
        deleteBtn = findViewById(R.id.deleteButton);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.updateButton:
//                String title = amountEdit.getText().toString();
//                String desc = descriptionEdit.getText().toString();
//
//                dbManager.update(_id, title, desc);
//                this.returnHome();
//                break;
//
            case R.id.deleteButton:
//                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}