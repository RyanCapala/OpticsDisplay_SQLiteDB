package com.example.awesome.opticsdisplay.Helper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by awesome on 1/21/18.
 */

public class InputValidation {
    public static final String TAG = "InputValidation";
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isInpuEditTextFilled(TextInputEditText textInputEditText,
                                        TextInputLayout textInputLayout, String message){
        String value = textInputEditText.getText().toString().trim();
        Log.d(TAG, "isInpuEditTextFilled: >>>" + message + " " + value);
        if (value.isEmpty()){
            textInputLayout.setError(message);
            hideKeyboard(textInputEditText);
            //return false;
            return true;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        //return true;
        return false;
    }

    public boolean isInputEditTextFilled(String username, String password, String conf_pass) {
        Log.d(TAG, "isInputEditTextFilled: >>>username: " + username);
        Log.d(TAG, "isInputEditTextFilled: >>>password: " + password);
        Log.d(TAG, "isInputEditTextFilled: >>>confPass: " + conf_pass);

        if (username.isEmpty() || password.isEmpty() || conf_pass.isEmpty()){
            return false;
        } else {
            Log.d(TAG, "isInputEditTextFilled: <<< fields are filled >>>");
        }
        return true;
    }

    public boolean isInputEditTextFilled(String username, String password) {
        Log.d(TAG, "isInputEditTextFilled: >>>username: " + username);
        Log.d(TAG, "isInputEditTextFilled: >>>password: " + password);

        if (username.isEmpty() || password.isEmpty()){
            return false;
        } else {
            Log.d(TAG, "isInputEditTextFilled: <<< fields are filled >>>");
        }
        return true;
    }


    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}
