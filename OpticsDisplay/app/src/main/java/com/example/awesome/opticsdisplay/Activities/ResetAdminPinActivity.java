package com.example.awesome.opticsdisplay.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ScrollView;

import com.example.awesome.opticsdisplay.Data.DBHandlerAdmin;
import com.example.awesome.opticsdisplay.Data.DatabaseHelperUser;
import com.example.awesome.opticsdisplay.Helper.InputValidation;
import com.example.awesome.opticsdisplay.Model.Admin;
import com.example.awesome.opticsdisplay.Model.User;
import com.example.awesome.opticsdisplay.R;
import com.example.awesome.opticsdisplay.Util.Constants;

public class ResetAdminPinActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ResetAdminPinActivity";

    private final AppCompatActivity activity = ResetAdminPinActivity.this;
    private ScrollView scrollView;

    private TextInputEditText editTextUserName;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfPassword;

    private AppCompatButton appCompatButtonReset, appCompatButtonCancel;

    private DBHandlerAdmin dbHandlerAdmin;
    private InputValidation inputValidation;
    private DatabaseHelperUser databaseHelperUser;
    private Admin admin;
    private User user;
    private String passedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_admin_pin);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        passedName = intent.getStringExtra(Constants.ADMIN_NAME_KEY);

        initViews();
        initListeners();
        initObjects();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatBtnReset_reset_admin:
                storeDataToDB();
                break;

            case R.id.appCompatBtnReset_cancel:
                goToStartPage();
                break;
        }
    }

    private void initViews() {
        scrollView = (ScrollView) findViewById(R.id.ScrollViewID_reset_A_pin);
        editTextUserName = (TextInputEditText) findViewById(R.id.nameET_reset_admin);
        editTextPassword = (TextInputEditText) findViewById(R.id.passwordET_reset_admin);
        editTextConfPassword = (TextInputEditText) findViewById(R.id.passwordConfET_reset_admin);
        appCompatButtonReset = (AppCompatButton) findViewById(R.id.appCompatBtnReset_reset_admin);
        appCompatButtonCancel = (AppCompatButton) findViewById(R.id.appCompatBtnReset_cancel);
    }

    private void initListeners() {
        appCompatButtonReset.setOnClickListener(this);
        appCompatButtonCancel.setOnClickListener(this);
    }

    private void initObjects() {
        dbHandlerAdmin = new DBHandlerAdmin(activity);
        inputValidation = new InputValidation(activity);
        databaseHelperUser = new DatabaseHelperUser(activity);
        admin = new Admin();
        user = new User();
    }

    private void storeDataToDB() {
        String user = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confPass = editTextConfPassword.getText().toString().trim();

        //Check if edittext are filled
        if (inputValidation.isInputEditTextFilled(user, password, confPass)) {
                if (password.equals(confPass)) {
                    //delete current admin and user before updating/creating a new one
                    dbHandlerAdmin.deleteAdmin(passedName);
                    databaseHelperUser.deleteUser(passedName);

                    admin.set_user_(user);
                    admin.set_password_(password);
                    dbHandlerAdmin.addAdmin(admin);
                    storeAdminAsUserAlso(user, password);
                    Snackbar.make(scrollView, getString(R.string.success_message), Snackbar
                            .LENGTH_LONG).show();
                    clearInputEditText();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToStartPage();
                        }
                    }, 1500);

                } else {
                    Snackbar.make(scrollView, "Password doesn't match!", Snackbar.LENGTH_LONG).show();
                    clearPasswordEditText();
                }
        } else {
            Snackbar.make(scrollView, "Fields are empty!", Snackbar.LENGTH_LONG).show();
        }
    }

    private void storeAdminAsUserAlso(String _user, String _pass) {
        if (!databaseHelperUser.checkUser(_user)) {
            user.set_user(_user);
            user.set_password(_pass);
            databaseHelperUser.addUser(user);
        }
    }

    private void clearInputEditText() {
        editTextUserName.setText(null);
        editTextPassword.setText(null);
        editTextConfPassword.setText(null);
    }

    private void clearPasswordEditText() {
        editTextPassword.setText(null);
        editTextConfPassword.setText(null);
    }

    private void goToStartPage() {
        Intent intent = new Intent(this, StartpageActivity.class);
        startActivity(intent);
        finish();
    }

}
