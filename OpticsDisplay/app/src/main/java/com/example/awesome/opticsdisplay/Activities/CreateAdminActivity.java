package com.example.awesome.opticsdisplay.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.example.awesome.opticsdisplay.Data.DBHandlerAdmin;
import com.example.awesome.opticsdisplay.Data.DatabaseHelperUser;
import com.example.awesome.opticsdisplay.Helper.InputValidation;
import com.example.awesome.opticsdisplay.Model.Admin;
import com.example.awesome.opticsdisplay.Model.User;
import com.example.awesome.opticsdisplay.R;

public class CreateAdminActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "CreateAdminActivity";

    private final AppCompatActivity activity = CreateAdminActivity.this;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);
        getSupportActionBar().hide();


        initViews();
        initListeners();
        initObjects();
        bypassActivity();

    }//End of onCreate


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appCompatBtnRegister_create_admin:
                storeDataToDB();
                goToStartPage();
                break;
        }
    }


    private void initViews() {
        scrollView = (ScrollView) findViewById(R.id.ScrollViewID_create_admin);
        editTextUserName = (TextInputEditText) findViewById(R.id.editTextName_create_admin);
        editTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword_create_admin);
        editTextConfirmPassword = (TextInputEditText) findViewById(R.id.editTextPasswordConf_create_admin);
        appCompatButtonCreate = (AppCompatButton) findViewById(R.id.appCompatBtnRegister_create_admin);
    }

    private void initListeners() {
        appCompatButtonCreate.setOnClickListener(this);
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
        String confPass = editTextConfirmPassword.getText().toString().trim();

        //Check if editText are filled
        if (inputValidation.isInputEditTextFilled(user, password, confPass)) {
            if (!dbHandlerAdmin.checkAdmin(user)) {
                //check if password match
                if (password.equals(confPass)) {
                    admin.set_user_(user);
                    admin.set_password_(password);
                    dbHandlerAdmin.addAdmin(admin);
                    storeAdminAsUserAlso(user, password);
                    Snackbar.make(scrollView, getString(R.string.success_message),
                            Snackbar.LENGTH_LONG).show();
                    clearInputEditText();
                } else {
                    Snackbar.make(scrollView, "Password doesnt match!", Snackbar.LENGTH_LONG).show();
                    clearPasswordEditText();
                }


            } else {
                Snackbar.make(scrollView, getString(R.string.error_user_exists),
                        Snackbar.LENGTH_LONG).show();
                clearInputEditText();
            }
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
        editTextConfirmPassword.setText(null);
    }

    private void clearPasswordEditText() {
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
    }

    private void goToStartPage() {
        Intent intent = new Intent(this, StartpageActivity.class);
        startActivity(intent);
        finish();
    }

    private void bypassActivity() {
        if (dbHandlerAdmin.getAdminCount() > 0) {
            Log.d(TAG, "bypassActivity: ===adminCount " + dbHandlerAdmin.getAdminCount());
            startActivity(new Intent(CreateAdminActivity.this, StartpageActivity.class));
            finish();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(scrollView.getWindowToken(), 0);
    }
}
