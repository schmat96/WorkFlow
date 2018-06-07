package net.ict.workflow.workflow;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;

public class AddTimeAcitivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_time);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        LocalDateTime ldt = (LocalDateTime)intent.getSerializableExtra(MainActivity.INTENT_CHOOSEN_DATE);
        user = new User(ldt);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.badge_times);
        recyclerView.setLayoutManager(layoutManager);
        BadgeTimesRecycler btr = new BadgeTimesRecycler(user);
        recyclerView.setAdapter(btr);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
