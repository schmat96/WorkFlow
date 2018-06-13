package net.ict.workflow.workflow;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
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

import net.ict.workflow.workflow.model.App;
import net.ict.workflow.workflow.model.DatabaseHelper;
import net.ict.workflow.workflow.model.OwnSettings;

import java.time.DayOfWeek;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();


    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference pref = findPreference("WorkingMinutes");
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    DatabaseHelper dbh = new DatabaseHelper(App.getContext());
                    float newFloat = 0f;
                    try {
                        newFloat = Float.parseFloat(newValue.toString()) / ((float) 60);
                    } finally {
                        dbh.insertHoursPerDay(newFloat);
                    }

                    return true;
                }
            });

        }
    }
}
