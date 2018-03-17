package com.example.awesome.opticsdisplay.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.example.awesome.opticsdisplay.Data.DatabaseHelperUser;
import com.example.awesome.opticsdisplay.Helper.InputValidation;
import com.example.awesome.opticsdisplay.Model.User;
import com.example.awesome.opticsdisplay.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "RegisterActivity";

    private final AppCompatActivity activity = RegisterActivity.this;

    private ScrollView scrollView;

    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextUserName;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private DatabaseHelperUser databaseHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatBtnRegister_reg:
                storeDataToDB();
                break;
            case R.id.appCompatTextView_login_link:
                gotoActivity();
                break;
        }
    }


    private void initViews() {
        //scrollView = (NestedScrollView) findViewById(R.id.NestedScrollViewID);
        scrollView = (ScrollView) findViewById(R.id.NestedScrollViewID);

        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textinput_layoutname_reg);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textinput_layoutpassword_reg);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textinput_layoutpasswordConf_reg);

        textInputEditTextUserName = (TextInputEditText) findViewById(R.id.textInput_editTextName_reg);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInput_editTextPassword_reg);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInput_editTextPasswordConf_reg);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatBtnRegister_reg);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextView_login_link);
    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelperUser(activity);
        user = new User();
    }

    private void storeDataToDB() {

        String _user = textInputEditTextUserName.getText().toString().trim();
        String _pass = textInputEditTextPassword.getText().toString().trim();
        String _conf = textInputEditTextConfirmPassword.getText().toString().trim();

        //check if edit text are filled
        if(inputValidation.isInputEditTextFilled(_user, _pass, _conf)){

            if (!databaseHelper.checkUser(_user)) {

                //check if password match
                if (_pass.equals(_conf)){
                    user.set_user(textInputEditTextUserName.getText().toString().trim());
                    user.set_password(textInputEditTextPassword.getText().toString().trim());

                    databaseHelper.addUser(user);

                    Snackbar snackbar = Snackbar.make(scrollView, getString(R.string
                            .success_message), Snackbar.LENGTH_LONG);
                    snackbar.setCallback(new Snackbar.Callback(){
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            gotoActivity();
                        }
                    });
                    snackbar.show();
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

        hideKeyboard();
    }


    private void clearInputEditText() {
        textInputEditTextUserName.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }

    private void clearPasswordEditText() {
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(scrollView.getWindowToken(), 0);
    }
    
    private void gotoActivity() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    /***
     private void storeDataToDB() {
     if (!inputValidation.isInpuEditTextFilled(textInputEditTextUserName,
     textInputLayoutUserName, getString(R.string.error_message_name))) {
     String result = String.valueOf(!inputValidation.isInpuEditTextFilled(textInputEditTextUserName,
     textInputLayoutUserName, getString(R.string.error_message_name)));
     Log.d(TAG, "storeDataToDB: =====user " + result);
     return;
     }

     if (!inputValidation.isInpuEditTextFilled(textInputEditTextPassword,
     textInputLayoutPassword, getString(R.string.error_message_password))) {
     String result = String.valueOf(!inputValidation.isInpuEditTextFilled(textInputEditTextUserName,
     textInputLayoutUserName, getString(R.string.error_message_name)));
     Log.d(TAG, "storeDataToDB: =====pass " + result);
     return;
     }

     if (!inputValidation.isInpuEditTextFilled(textInputEditTextPassword,
     textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
     String result = String.valueOf(!inputValidation.isInpuEditTextFilled(textInputEditTextUserName,
     textInputLayoutUserName, getString(R.string.error_message_name)));
     Log.d(TAG, "storeDataToDB: ====conf " + result);
     return;
     }

     if (!databaseHelper.checkUser(textInputEditTextUserName.getText().toString().trim())) {
     user.set_user(textInputEditTextUserName.getText().toString().trim());
     user.set_password(textInputEditTextPassword.getText().toString().trim());

     databaseHelper.addUser(user);
     Snackbar.make(scrollView, getString(R.string.success_message),
     Snackbar.LENGTH_LONG).show();
     clearInputEditText();

     } else {
     Snackbar.make(scrollView, getString(R.string.error_user_exists),
     Snackbar.LENGTH_LONG).show();
     clearInputEditText();
     }

     }
     ***/

}


