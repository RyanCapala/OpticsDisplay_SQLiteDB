package com.example.awesome.opticsdisplay.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.awesome.opticsdisplay.Data.DBHandlerAdmin;
import com.example.awesome.opticsdisplay.Data.DatabaseHelperUser;
import com.example.awesome.opticsdisplay.R;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserActivity extends AppCompatActivity {

    //============
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    //============

    ListView userListView;
    Button doneBtn;
    List<String> userArray;
    DatabaseHelperUser databaseHelperUser;
    DBHandlerAdmin dbHandlerAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        getSupportActionBar().hide();

        initDeclaredObjects();


        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userArray);
        userListView.setAdapter(listAdapter);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteUserActivity.this, StartpageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = (String) userListView.getItemAtPosition(position);
                showDeleteDialog(user, position);
            }
        });
    }

    private void initDeclaredObjects() {
        databaseHelperUser = new DatabaseHelperUser(this);
        dbHandlerAdmin = new DBHandlerAdmin(this);
        userArray = new ArrayList<>();
        userArray = databaseHelperUser.getAllUserNames();
        userListView = (ListView) findViewById(R.id.userListView);
        doneBtn = (Button) findViewById(R.id.doneBtn_del_act);
    }

    private void showDeleteDialog(final String user, final int pos) {
        //Create alert dialog
        alertDialogBuilder = new AlertDialog.Builder(this);
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.confirmation_dialog, null);

        Button noButton = (Button) view.findViewById(R.id.noButton);
        Button yesButton = (Button) view.findViewById(R.id.yesButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(DeleteUserActivity.this, user, Toast.LENGTH_LONG).show();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete user
                Toast.makeText(DeleteUserActivity.this, getString(R.string.user_deleted) +": " + user, Toast
                        .LENGTH_SHORT)
                        .show();
                databaseHelperUser.deleteUser(user);

                //check if deleted user is an Admin
                //then delete it from admin DB if it is
                if (dbHandlerAdmin.checkAdmin(user)) {
                    dbHandlerAdmin.deleteAdmin(user);
                }
                userArray.remove(pos);
                dialog.dismiss();

                Intent intent = new Intent(DeleteUserActivity.this, DeleteUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
