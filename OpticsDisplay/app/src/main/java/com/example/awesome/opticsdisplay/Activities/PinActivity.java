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
import com.example.awesome.opticsdisplay.Util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PinActivity extends AppCompatActivity {

    EditText pinCode, adminName;
    Button registerUserBtn;
    Button deleteUserBtn;
    Button resetAdminPinBtn;
    Button createAdminBtn;
    TextView cancelAdmin;

    private String adminPin, admin_name;
    private DBHandlerAdmin dbHandlerAdmin;
    private List<String> passList;
    private List<String> adminList;
    private String _uname, _pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        getSupportActionBar().hide();

        adminName = (EditText) findViewById(R.id.adminNameEditText);
        pinCode = (EditText) findViewById(R.id.pinCodeEditText);
        registerUserBtn = (Button) findViewById(R.id.pinLoginBtn);
        deleteUserBtn = (Button) findViewById(R.id.pinDltUserBtn);
        resetAdminPinBtn = (Button) findViewById(R.id.resetAdminPinBtn);
        createAdminBtn = (Button) findViewById(R.id.createAdminBtn);
        cancelAdmin = (TextView) findViewById(R.id.cancelAdminTV);

        dbHandlerAdmin = new DBHandlerAdmin(this);

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextValues();
                if (_uname.isEmpty() || _pin.isEmpty()) {
                    Toast.makeText(PinActivity.this, Constants.EMPTY_FIELDS_ERR, Toast.LENGTH_LONG).show();
                } else {
                    verifyPin(RegisterActivity.class);
                }

            }
        });

        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextValues();
                if (_uname.isEmpty() || _pin.isEmpty()) {
                    Toast.makeText(PinActivity.this, Constants.EMPTY_FIELDS_ERR, Toast.LENGTH_LONG).show();
                } else {
                    verifyPin(DeleteUserActivity.class);
                }

            }
        });

        resetAdminPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextValues();
                if (_uname.isEmpty() || _pin.isEmpty()) {
                    Toast.makeText(PinActivity.this, Constants.EMPTY_FIELDS_ERR, Toast.LENGTH_LONG).show();
                } else {
                    verifyPin(ResetAdminPinActivity.class);
                }

            }
        });

        createAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = dbHandlerAdmin.getAdminCount();
                getEditTextValues();
                if (_uname.isEmpty() || _pin.isEmpty()) {
                    Toast.makeText(PinActivity.this, Constants.EMPTY_FIELDS_ERR, Toast.LENGTH_LONG).show();
                } else {
                    if (count <= 2) {
                        verifyPin(CreateAdminActivity.class);
                    } else {
                        Toast.makeText(PinActivity.this,
                                "Unable to create Admin. (" + count
                                + " " + "Admin limit)", Toast.LENGTH_LONG).show();

                        pinCode.setText(null);
                        adminName.setText(null);
                    }
                }
            }
        });

        cancelAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(StartpageActivity.class);
            }
        });


    }//end of onCreate

    private void verifyPin(Class activityClass) {
        if (checkAdminPin(_pin, _uname)) {
            if (_pin.equals(adminPin)) {
                goToActivityWithName(activityClass, _uname, _pin);
                pinCode.setText(null);
                adminName.setText(null);
            } else {
                Toast.makeText(this, getString(R.string.error_admin_pin), Toast.LENGTH_LONG).show();
                pinCode.setText(null);
            }
        } else {
            Toast.makeText(this, getString(R.string.error_admin_pin), Toast.LENGTH_LONG).show();
            pinCode.setText(null);
        }
    }


    private void getEditTextValues() {
        _uname = adminName.getText().toString().trim();
        _pin = pinCode.getText().toString().trim();
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

    private void goToActivityWithName(Class activityClass, String admin, String pass) {
        Intent intent = new Intent(PinActivity.this, activityClass);
        intent.putExtra(Constants.ADMIN_NAME_KEY, admin);
        startActivity(intent);
        finish();
    }

    private void goToActivity(Class activityClass) {
        Intent intent = new Intent(PinActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}
