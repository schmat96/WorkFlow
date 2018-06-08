package net.ict.workflow.workflow;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import net.ict.workflow.workflow.model.CardType;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;

public class BadgeTimesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;
    public static final String INTENT_CHOOSEN_DATE = "CHOOSEN_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.badge_times);

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
        BadgeTimesRecycler btr = new BadgeTimesRecycler(user, this);
        recyclerView.setAdapter(btr);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SwipeController swipeController = new SwipeController(btr, this);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        updateCard();

        ImageButton but = (ImageButton) findViewById(R.id.addButton);
        but.setOnClickListener(buttonUpOnClickListener);

    }

    public void changeViewBadgesTimes(LocalDateTime ldt) {
        Intent intent = new Intent(getApplicationContext(), EditTimeActivity.class);
        intent.putExtra(INTENT_CHOOSEN_DATE, ldt);
        startActivity(intent);
    }

    private View.OnClickListener buttonUpOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeViewBadgesTimes(user.getChoosenDate());
        }
    };

    public void updateCard() {
        float valueBadged = User.getBadgedTimeDay(user.getChoosenDate());
        Bar bar = (Bar) findViewById(R.id.barDay);
        bar.setValue(8.24f, valueBadged);
        TextView tv = (TextView) findViewById(R.id.cardViewTitle);
        Resources res = getResources();
        float rest = (User.getMaxTime(CardType.DAY, user.getChoosenDate()) - valueBadged);
        if (rest > 0) {
            String text = String.format("%.2f", rest);
            tv.setText(res.getString(R.string.WorkDay, text, text));
        } else {
            tv.setText(res.getString(R.string.EnoughWorked));
        }

    }
}
