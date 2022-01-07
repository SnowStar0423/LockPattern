package com.henleylee.lockpattern.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.henleylee.lockpattern.Cell;
import com.henleylee.lockpattern.CellStatus;
import com.henleylee.lockpattern.OnPatternChangedListener;
import com.henleylee.lockpattern.PatternIndicatorView;
import com.henleylee.lockpattern.PatternLockerView;
import com.henleylee.lockpattern.style.ArrowLockerLinkedLineStyle;
import com.henleylee.lockpattern.style.DefaultLockerCellStyle;
import com.henleylee.lockpattern.style.DefaultLockerLinkedLineStyle;
import com.henleylee.lockpattern.style.RippleLockerCellStyle;

import java.util.List;

public class PatternSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private boolean isFailure;
    private PatternIndicatorView indicatorView;
    private PatternLockerView lockerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_setting);
        Utility.initActionBar(getSupportActionBar());

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
                if (isFailure) {
                    view.setPatternStatus(CellStatus.ERROR);
                }
                indicatorView.setSelectedCells(cells);
            }

            @Override
            public void onPatternClear() {

            }
        });

        ((CheckBox) findViewById(R.id.check_box_fail)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.check_box_feedback)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.check_box_skip_mode)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.check_box_stealth_mode)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.check_box_auto_clean)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.check_box_ripple)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.check_box_arrow)).setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_box_fail:
                isFailure = isChecked;
                break;
            case R.id.check_box_feedback:
                lockerView.setEnableHapticFeedback(isChecked);
                break;
            case R.id.check_box_skip_mode:
                lockerView.setEnableSkipMode(isChecked);
                break;
            case R.id.check_box_stealth_mode:
                lockerView.setEnableInStealthMode(isChecked);
                break;
            case R.id.check_box_auto_clean:
                lockerView.setEnableAutoClean(isChecked);
                break;
            case R.id.check_box_ripple:
                if (isChecked) {
                    lockerView.setCellStyle(new RippleLockerCellStyle(lockerView.getDecoratorStyle()));
                } else {
                    lockerView.setCellStyle(new DefaultLockerCellStyle(lockerView.getDecoratorStyle()));
                }
                break;
            case R.id.check_box_arrow:
                if (isChecked) {
                    lockerView.setLinkedLineStyle(new ArrowLockerLinkedLineStyle(lockerView.getDecoratorStyle()));
                } else {
                    lockerView.setLinkedLineStyle(new DefaultLockerLinkedLineStyle(lockerView.getDecoratorStyle()));
                }
                break;
            default:
                break;
        }
    }

}
