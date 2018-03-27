package com.example.awesome.opticsdisplay.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ScrollView;

import com.example.awesome.opticsdisplay.Data.DBHandlerAdmin;
import com.example.awesome.opticsdisplay.Data.DatabaseHelperUser;
import com.example.awesome.opticsdisplay.Helper.InputValidation;
import com.example.awesome.opticsdisplay.Helper.StoreToDBHelper;
import com.example.awesome.opticsdisplay.Model.Admin;
import com.example.awesome.opticsdisplay.Model.User;
import com.example.awesome.opticsdisplay.R;

public class InitialStartActivity extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = InitialStartActivity.this;

    private ScrollView scrollView;

    private TextInputEditText editTextUserName;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;

    private AppCompatButton appCompatButtonCreate;

    private DBHandlerAdmin dbHandlerAdmin;
    private InputValidation inputValidation;
    private DatabaseHelperUser databaseHelperUser;
    private Admin admin;
    private User user;
    private StoreToDBHelper storeToDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_initial_start);
        setContentView(R.layout.activity_create_admin);

        initViews();
        initListeners();
        initObjects();
        bypassActivity();

    }//end onCreate


    //--------------------------------------------------------------------------------------------//
    // for 'Implements View.OnClickListener'
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatBtnRegister_create_admin:
                storeDataToDB();
                break;
        }
    }

    private void initListeners() {
        appCompatButtonCreate.setOnClickListener(this);
    }
    //--------------------------------------------------------------------------------------------//

    private void initViews() {
        scrollView = (ScrollView) findViewById(R.id.ScrollViewID_create_admin);
        editTextUserName = (TextInputEditText) findViewById(R.id.editTextName_create_admin);
        editTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword_create_admin);
        editTextConfirmPassword = (TextInputEditText) findViewById(R.id.editTextPasswordConf_create_admin);
        appCompatButtonCreate = (AppCompatButton) findViewById(R.id.appCompatBtnRegister_create_admin);
    }



    private void initObjects() {
        dbHandlerAdmin = new DBHandlerAdmin(activity);
        inputValidation = new InputValidation(activity);
        databaseHelperUser = new DatabaseHelperUser(activity);
        admin = new Admin();
        user = new User();
        storeToDBHelper = new StoreToDBHelper(admin, user, activity);
    }

    private void storeDataToDB() {
        String user = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confPass = editTextConfirmPassword.getText().toString().trim();

        if (storeToDBHelper.isStoredToDB(scrollView, user, password, confPass)) {
            clearInputEditText();
            //to delay startactivity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToStartPage();
                }
            }, 1500);
        } else {
            clearInputEditText();
        }
    }



    private void clearInputEditText() {
        editTextUserName.setText(null);
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
    }

    private void clearPasswordEditText() {
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
    }

    private void goToStartPage() {
        startActivity(new Intent(activity, StartpageActivity.class));
        finish();
    }

    private void bypassActivity() {
        if (dbHandlerAdmin.getAdminCount() > 0) {
            startActivity(new Intent(activity, StartpageActivity.class));
            finish();
        }
    }


}
