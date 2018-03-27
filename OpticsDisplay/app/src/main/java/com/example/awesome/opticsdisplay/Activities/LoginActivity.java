package com.example.awesome.opticsdisplay.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ScrollView;

import com.example.awesome.opticsdisplay.Data.DatabaseHelperUser;
import com.example.awesome.opticsdisplay.Helper.InputValidation;
import com.example.awesome.opticsdisplay.R;
import com.example.awesome.opticsdisplay.Util.Constants;
import com.example.awesome.opticsdisplay.Util.Session;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;

    private ScrollView scrollView;

    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUserName;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelperUser databaseHelper;

    
    public static final String PREF_NAME = "Preferences";
    public static final String PREF_KEY = "text";
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
        
        if (session.checkIfUserIsLoggedIn()) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatBtnLogin:
                verifyUserFromDB();
                break;
            case R.id.appCompatTextView_cancel_link:
                Intent intent = new Intent(LoginActivity.this, StartpageActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    private void initViews() {
        scrollView = (ScrollView) findViewById(R.id.NestedScrollViewID_login);
        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textinput_layoutname_login);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textinput_layoutpassword_login);

        textInputEditTextUserName = (TextInputEditText) findViewById(R.id.textInput_editTextName_login);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInput_editTextPassword_login);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatBtnLogin);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.appCompatTextView_cancel_link);
    }

    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelperUser(activity);
        inputValidation = new InputValidation(activity);
        session = new Session(this);
    }
    

    private void verifyUserFromDB() {

        String _user = textInputEditTextUserName.getText().toString().trim();
        String _pass = textInputEditTextPassword.getText().toString().trim();
        if (inputValidation.isInputEditTextFilled(_user, _pass)){

            if (databaseHelper.checkUser(_user, _pass)){
                Intent intent = new Intent(activity, MainActivity.class);
                clearInputEditText();
                storeToSharedPref(_user);
                startActivity(intent);
                session.setUserLoggedIn(true);
                finish();
            } else {
                Snackbar.make(scrollView, getString(R.string.error_valid_username_password),
                        Snackbar.LENGTH_LONG).show();
                clearInputEditText();
            }

        } else {
            Snackbar.make(scrollView, Constants.EMPTY_FIELDS_ERR, Snackbar.LENGTH_LONG).show();
        }

    }

    private void clearInputEditText() {
        textInputEditTextUserName.setText(null);
        textInputEditTextPassword.setText(null);
    }
    
    
    private void storeToSharedPref(String str) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY, str);
        editor.apply();
    }

    /***
     private void verifyUserFromDB() {
         if (!inputValidation.isInpuEditTextFilled(textInputEditTextUserName,
         textInputLayoutUserName, getString(R.string.error_message_name))) {
         return;
         }

         if (!inputValidation.isInpuEditTextFilled(textInputEditTextPassword,
         textInputLayoutPassword, getString(R.string.error_message_password))) {
         return;
         }


         if (databaseHelper.checkUser(textInputEditTextUserName.getText().toString().trim(),
         textInputEditTextPassword.getText().toString().trim())) {

         Intent intent = new Intent(activity, MainActivity.class);
         intent.putExtra(INTENT_KEY, textInputEditTextUserName.getText().toString().trim());
         clearInputEditText();
         startActivity(intent);
         finish();

         } else {
         Snackbar.make(scrollView, getString(R.string.error_valid_username_password),
         Snackbar.LENGTH_LONG).show();
         clearInputEditText();
         }
     }
     ***/

}
