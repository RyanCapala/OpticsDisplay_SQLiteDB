package com.example.awesome.opticsdisplay.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by awesome on 1/21/18.
 */

public class InputValidation {
    public static final String TAG = "InputValidation";
    private Context context;

    public InputValidation() {
    }

    public InputValidation(Context context) {
        this.context = context;
    }

//    public boolean isInpuEditTextFilled(EditText textInputEditText,
//                                        String textInputLayout, String message){
//        String value = textInputEditText.getText().toString().trim();
//        //Log.d(TAG, "isInpuEditTextFilled: >>>" + message + " " + value);
//        if (value.isEmpty()){
//            textInputLayout.setError(message);
//            hideKeyboard(textInputEditText);
//            //return false;
//            return true;
//        } else {
//            textInputLayout.setErrorEnabled(false);
//        }
//        //return true;
//        return false;
//    }

    public boolean isInputEditTextFilled(String username, String password, String conf_pass) {

        if (username.isEmpty() || password.isEmpty() || conf_pass.isEmpty()){
            return false;
        }

        return true;
    }

    public boolean isInputEditTextFilled(String username, String password) {


        if (username.isEmpty() || password.isEmpty()){
            return false;
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
