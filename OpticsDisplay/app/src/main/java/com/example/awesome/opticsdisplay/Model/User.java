package com.example.awesome.opticsdisplay.Model;

/**
 * Created by awesome on 1/21/18.
 */

public class User {

    private int _id;
    private String _user;
    private String _password;

    public User() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_user() {
        return _user;
    }

    public void set_user(String _user) {
        this._user = _user;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }
}
