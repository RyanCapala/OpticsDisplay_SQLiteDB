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

    EditText pinCode, adminName;
    Button registerUserBtn;
    Button deleteUserBtn;
    TextView cancelAdmin;

    private String adminPin, admin_name;
    private DBHandlerAdmin dbHandlerAdmin;
    private List<String> passList;
    private List<String> adminList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        getSupportActionBar().hide();

        adminName = (EditText) findViewById(R.id.adminNameEditText);
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
                goToActivity(StartpageActivity.class);
            }
        });


    }

    private void verifyPin(Class activityClass){
        String pin = pinCode.getText().toString().trim();
        String name = adminName.getText().toString().trim();
        if (checkAdminPin(pin, name)) {
            if (pin.equals(adminPin)) {

                goToActivity(activityClass);

            }
        } else {
            Toast.makeText(this, getText(R.string.error_admin_pin), Toast.LENGTH_LONG).show();
        }

        //Clear Edit text
        pinCode.setText(null);
        adminName.setText("");
    }

    private List<String> getAdminPinFromDB() {
        List<Admin> adminList = new ArrayList<>();
        List<String> passwordList = new ArrayList<>();
        String _pin;
        adminList = dbHandlerAdmin.getAllAdmin();
        for (Admin admin : adminList) {
            _pin = admin.get_password_();
            passwordList.add(_pin);
        }

        return passwordList;
    }
    
    private List<String> getAdminNameFromDB() {
        List<Admin> adminList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        String _name;
        adminList = dbHandlerAdmin.getAllAdmin();
        for (Admin admin : adminList) {
            _name = admin.get_user_();
            nameList.add(_name);
        }
        return  nameList;
    }

    private boolean checkAdminPin(String pin, String adminName) {
        // Get admin pin from admin database
        passList = new ArrayList<>();
        adminList = new ArrayList<>();
        adminList = getAdminNameFromDB();
        passList = getAdminPinFromDB();

        for (int i = 0; i < passList.size(); i++) {
            adminPin = passList.get(i);
            admin_name = adminList.get(i);
            if (pin.equals(adminPin) && adminName.equals(admin_name)){
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
