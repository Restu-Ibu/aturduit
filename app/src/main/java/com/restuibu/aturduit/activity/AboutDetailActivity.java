package com.restuibu.aturduit.activity;

import android.app.Activity;
import android.os.Bundle;

import com.restuibu.aturduit.R;

public class AboutDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_detail);

        getActionBar().setTitle("Informasi Aplikasi");
    }
}
