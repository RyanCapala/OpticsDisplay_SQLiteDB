package com.example.awesome.opticsdisplay.Helper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.example.awesome.opticsdisplay.Data.DBHandlerAdmin;
import com.example.awesome.opticsdisplay.Data.DatabaseHelperUser;
import com.example.awesome.opticsdisplay.Model.Admin;
import com.example.awesome.opticsdisplay.Model.User;
import com.example.awesome.opticsdisplay.Util.Constants;

/**
 * Created by awesomeness on 3/26/18.
 */

public class StoreToDBHelper {

    private DBHandlerAdmin dbHandlerAdmin;
    private InputValidation inputValidation;
    private DatabaseHelperUser databaseHelperUser;
    private Admin admin;
    private User user;

    private AppCompatActivity activity;

    public StoreToDBHelper() {
    }

    public StoreToDBHelper(Admin admin, User user, AppCompatActivity activity) {
        this.admin = admin;
        this.user = user;
        this.activity = activity;
    }

    public boolean isStoredToDB(ScrollView _scrollview, String _uname, String _pin, String _confPin) {

        initClass();

        if (inputValidation.isInputEditTextFilled(_uname, _pin, _confPin)) {
            if (!dbHandlerAdmin.checkAdmin(_uname)) {
                if (_pin.equals(_confPin)) {
                    admin.set_user_(_uname);
                    admin.set_password_(_pin);
                    dbHandlerAdmin.addAdmin(admin);
                    storeAdminAsUserToDB(_uname, _pin);
                    Snackbar.make(_scrollview, Constants.SUCCESS_MSG, Snackbar.LENGTH_LONG).show();
                    return true;
                } else {
                    Snackbar.make(_scrollview, Constants.PIN_MATCH_ERR, Snackbar.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Snackbar.make(_scrollview, Constants.USER_EXIST_ERR, Snackbar.LENGTH_LONG).show();
                return false;
            }
        } else {
            Snackbar.make(_scrollview, Constants.EMPTY_FIELDS_ERR, Snackbar.LENGTH_LONG).show();
            return false;
        }


    }

    private void storeAdminAsUserToDB(String _uname, String _pin) {
        if (!databaseHelperUser.checkUser(_uname)) {
            user.set_user(_uname);
            user.set_password(_pin);
            databaseHelperUser.addUser(user);
        }
    }

    private void initClass() {
        dbHandlerAdmin = new DBHandlerAdmin(activity);
        inputValidation = new InputValidation();
        databaseHelperUser = new DatabaseHelperUser(activity);

    }

}//end of Class
