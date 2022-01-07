package com.henleylee.lockpattern.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.henleylee.lockpattern.Cell;
import com.henleylee.lockpattern.CellStatus;
import com.henleylee.lockpattern.OnPatternChangedListener;
import com.henleylee.lockpattern.PatternHelper;
import com.henleylee.lockpattern.PatternIndicatorView;
import com.henleylee.lockpattern.PatternLockerView;

import java.util.List;

/**
 * 创建密码
 */
public class PatternCheckActivity extends AppCompatActivity {

    private static final int MAX_RETRY_COUNT = 4;

    private int retryCount = MAX_RETRY_COUNT;
    private String password;
    private TextView tvMessage;
    private PatternIndicatorView indicatorView;
    private PatternLockerView lockerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_locker);
        Utility.initActionBar(getSupportActionBar());

        password = Utility.getPatternPassword(this);

        tvMessage = findViewById(R.id.pattern_message);
        indicatorView = findViewById(R.id.pattern_indicator_view);
        lockerView = findViewById(R.id.pattern_locker_view);
        lockerView.setOnPatternChangedListener(new OnPatternChangedListener() {
            @Override
            public void onPatternStart() {
                indicatorView.setSelectedCells(null);
            }

            @Override
            public void onPatternChange(PatternLockerView view, List<Cell> cells) {

            }

            @Override
            public void onPatternComplete(PatternLockerView view, List<Cell> cells) {
                handlePatternPassword(view.getSide(), cells);
            }

            @Override
            public void onPatternClear() {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handlePatternPassword(int side, List<Cell> cells) {
        if (!TextUtils.equals(password, PatternHelper.patternToString(side, cells))) {
            retryCount--;
            lockerView.setPatternStatus(CellStatus.ERROR);
            indicatorView.setSelectedCells(cells);
            tvMessage.setTextColor(Color.RED);
            if (retryCount > 0) {
                tvMessage.setText("Wrong password，You can also enter " + retryCount + " Second-rate");
            } else {
                retryCount = MAX_RETRY_COUNT;
                tvMessage.setText("Wrong password，please enter again");
            }
            return;
        }
        indicatorView.setSelectedCells(cells);
            Utility.showToast(this, "Password is correct");
        finish();
    }

}
