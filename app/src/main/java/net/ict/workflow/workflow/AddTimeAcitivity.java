package net.ict.workflow.workflow;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import net.ict.workflow.workflow.model.App;
import net.ict.workflow.workflow.model.OwnSettings;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;
import java.util.Date;

import static net.ict.workflow.workflow.MainActivity.INTENT_CHOOSEN_DATE;

public class AddTimeAcitivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;
    protected LocalDateTime ldt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_time);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ldt = (LocalDateTime)intent.getSerializableExtra(MainActivity.INTENT_CHOOSEN_DATE);
        user = new User(ldt, this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.badge_times);
        recyclerView.setLayoutManager(layoutManager);
        BadgeTimesRecycler btr = new BadgeTimesRecycler(user);
        recyclerView.setAdapter(btr);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        addActionListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    protected void addActionListeners() {
        Button but = (Button) findViewById(R.id.confirmButton);
        but.setOnClickListener(buttonConfirmListener);
    }


    private View.OnClickListener buttonConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePicker tp = (TimePicker) findViewById(R.id.timepicker);
            LocalDateTime newTime = ldt.toLocalDate().atTime(tp.getHour(), tp.getMinute());
            int daysCode = OwnSettings.getDaysCode();
            User.addBadgeTime(newTime, daysCode);
            Intent intent = new Intent(getApplicationContext(), BadgeTimesActivity.class);
            intent.putExtra(INTENT_CHOOSEN_DATE, ldt);
            finish();
            startActivity(intent);
        }
    };

}
