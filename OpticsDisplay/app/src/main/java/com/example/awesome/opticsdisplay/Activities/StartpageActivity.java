package com.example.awesome.opticsdisplay.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.awesome.opticsdisplay.R;

public class StartpageActivity extends AppCompatActivity {

    private Button adminBtn;
    private Button loginPageBtn;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        getSupportActionBar().hide();

        adminBtn = (Button) findViewById(R.id.adminBtn);
        loginPageBtn = (Button) findViewById(R.id.loginPageBtn);

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StartpageActivity.this, PinActivity.class);
                startActivity(intent);
            }
        });

        loginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StartpageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
