package net.ict.workflow.workflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.DayOfWeek;

public class SettingsActivity extends AppCompatActivity {

    private Boolean[] weekDays;
    private Toolbar toolbar;
    private String[] themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupPensum();

        setupPensumTime();

        setupThemes();


    }

    private void setupPensumTime() {
        CardView cv = (CardView) findViewById(R.id.language);
        TextView tv = (TextView) cv.findViewById(R.id.cardViewTitle);
        tv.setText(R.string.language);
        LinearLayout ll = (LinearLayout) cv.findViewById(R.id.settingpoint1);

        TimePicker tp = new TimePicker(this);
        tp.setLayoutMode(1);
        tp.setIs24HourView(true);
        ll.addView(tp);
    }


    private void setupPensum() {
        CardView cv = (CardView) findViewById(R.id.pensum);
        TextView tv = (TextView) cv.findViewById(R.id.cardViewTitle);

        LinearLayout ll = (LinearLayout) cv.findViewById(R.id.settingpoint1);

        int i = 0;
        setWeekDays();
        for (DayOfWeek dow : DayOfWeek.values()) {
            SwitchCompat switchCompat = new SwitchCompat(this);
            switchCompat.setId(i);
            switchCompat.setText(dow.toString());
            switchCompat.setSwitchTextAppearance(getApplicationContext(), R.style.AppTheme);
            switchCompat.setChecked(weekDays[i]);
            switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> weekDays[buttonView.getId()] = isChecked
            );
            ll.addView(switchCompat);
            i++;
        }
        Button confirm = (Button) cv.findViewById(R.id.confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                int zahl = 0;
                for (Boolean bu : weekDays) {
                    if (bu) {
                        zahl = zahl + ((int) Math.pow(2,i));

                    }
                    i++;
                }
                Log.d("TAG", "--->" + zahl);
            }
        });
    }

    private void setupThemes() {
        CardView cv = (CardView) findViewById(R.id.theme);
        TextView tv = (TextView) cv.findViewById(R.id.cardViewTitle);
        tv.setText(R.string.theme);
        LinearLayout ll = (LinearLayout) cv.findViewById(R.id.settingpoint1);
        setThemes();
        final RadioButton[] rb = new RadioButton[themes.length];
        RadioGroup rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        for(int i = 0; i<themes.length;i++){
            rb[i]  = new RadioButton(this);
            rb[i].setText(themes[i]);
            rb[i].setId(i + 100);
            rg.addView(rb[i]);
        }
        ll.addView(rg);
    }

    private void setThemes() {
        themes = new String[3];
        themes[0] = "Dark";
        themes[1] = "White";
        themes[2] = "Orange";
    }

    private void setWeekDays() {
        weekDays = new Boolean[7];
        int setting = 113;
        int i = 0;
        while (i<=6) {
            if (setting % 2 == 0) {
                weekDays[i] = false;
            } else {
                weekDays[i] = true;
                setting--;
            }
            setting = setting / 2;
            i++;
        }
    }


}
