package net.ict.workflow.workflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.time.LocalDateTime;

public class BadgeTimesActivityWeek extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.badge_times_week);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        LocalDateTime ldt = (LocalDateTime)intent.getSerializableExtra(MainActivity.INTENT_CHOOSEN_DATE);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.badge_times_week);
        recyclerView.setLayoutManager(layoutManager);
        BadgeTimesRecyclerWeek btr = new BadgeTimesRecyclerWeek(this, ldt);
        recyclerView.setAdapter(btr);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
