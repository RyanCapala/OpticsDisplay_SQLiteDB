package com.example.awesome.opticsdisplay.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awesome.opticsdisplay.Data.DBHandlerAdmin;
import com.example.awesome.opticsdisplay.Model.Admin;
import com.example.awesome.opticsdisplay.R;

import java.util.ArrayList;
import java.util.List;

public class PinActivity extends AppCompatActivity {

    EditText pinCode;
    Button registerUserBtn;
    Button deleteUserBtn;
    TextView cancelAdmin;
    private String mainPin1 = "0911";
    private String mainPin2 = "7024749000";
    private String adminPin;
    private DBHandlerAdmin dbHandlerAdmin;
    private List<String> passList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        getSupportActionBar().hide();

        pinCode = (EditText) findViewById(R.id.pinCodeEditText);
        registerUserBtn = (Button) findViewById(R.id.pinLoginBtn);
        deleteUserBtn = (Button) findViewById(R.id.pinDltUserBtn);
        cancelAdmin = (TextView) findViewById(R.id.cancelAdminTV);

        dbHandlerAdmin = new DBHandlerAdmin(this);

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPin(RegisterActivity.class);
            }
        });

        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPin(DeleteUserActivity.class);
            }
        });

        cancelAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PinActivity.this, StartpageActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void verifyPin(Class activityClass){
        String pin = pinCode.getText().toString().trim();
        if (checkAdminPin(pin)) {
            if (pin.equals(adminPin)) {

                goToActivity(activityClass);

            }
        } else if (pin.equals(mainPin1) || pin.equals(mainPin2)) {

            goToActivity(activityClass);

        } else {
            Toast.makeText(this, getText(R.string.error_admin_pin), Toast.LENGTH_LONG).show();
        }

        //Clear Edit text
        pinCode.setText(null);
    }

    private List<String> getAdminPinFromDB() {
        List<Admin> adminList = new ArrayList<>();
        List<String> passwordList = new ArrayList<>();
        String password;
        adminList = dbHandlerAdmin.getAllAdmin();
        for (Admin admin : adminList) {
            password = admin.get_password_();
            passwordList.add(password);
        }

        return passwordList;
    }

    private boolean checkAdminPin(String pin) {
        // Get admin pin from admin database
        passList = new ArrayList<>();
        passList = getAdminPinFromDB();

        for (int i = 0; i < passList.size(); i++) {
            adminPin = passList.get(i);
            if (pin.equals(adminPin)){
                return true;
            }
        }
        return false;
    }

    private void goToActivity(Class activityClass) {
        Intent intent = new Intent(PinActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}
