package com.hejin.aidldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void general(View view) {
        startActivity(new Intent(this,GeneralActivity.class));
    }

    public void custom(View view) {
        startActivity(new Intent(this,CustomActivity.class));
    }
}
