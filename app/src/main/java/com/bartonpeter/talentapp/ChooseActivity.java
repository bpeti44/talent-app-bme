package com.bartonpeter.talentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }


    public void summerActivity(View v){
        Intent summerIntent = new Intent(this, SummerMainActivity.class);
        startActivity(summerIntent);
    }

    public void springActivity(View v){
        Intent springIntent = new Intent(this, SpringMainActivity.class);
        startActivity(springIntent);
    }



}
