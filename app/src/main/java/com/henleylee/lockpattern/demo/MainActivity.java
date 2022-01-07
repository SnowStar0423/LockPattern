package com.henleylee.lockpattern.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.pattern_create).setOnClickListener(this);
        findViewById(R.id.pattern_check).setOnClickListener(this);
        findViewById(R.id.pattern_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pattern_create:
                startActivity(new Intent(this, PatternCreateActivity.class));
                break;
            case R.id.pattern_check:
                startActivity(new Intent(this, PatternCheckActivity.class));
                break;
            case R.id.pattern_setting:
                startActivity(new Intent(this, PatternSettingActivity.class));
                break;
            default:
                break;
        }
    }
}
