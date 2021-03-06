package net.ict.workflow.workflow;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.TransitionDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.ict.workflow.workflow.model.App;
import net.ict.workflow.workflow.model.BadgeTimes;
import net.ict.workflow.workflow.model.CardType;
import net.ict.workflow.workflow.model.OwnSettings;
import net.ict.workflow.workflow.model.User;
import net.ict.workflow.workflow.record.NdefMessageParser;
import net.ict.workflow.workflow.record.ParsedNdefRecord;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;


public class  MainActivity extends AppCompatActivity {

    protected static final String INTENT_CHOOSEN_DATE = "CHOOSEN_DATE";

    User user;
    Boolean loggedIn = false;
    Menu headerMenu;
    Toolbar toolbar;

    /*
    The NFC Adapter for nfc scans.
     */
    private NfcAdapter nfcAdapter;
    private TextView textView;

    /*

     */
    private RecyclerView recyclerView;

    /*
    Used for "snapping" the cards. So that there is only 1 card visible at a time.
     */
    private SnapHelper snapHelper;
    private CardAdapter cardAdapter;

    /*
    sad
     */
    private Cards[] cards;
    private LocalDateTime wrongTime = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.setContext(this);

        OwnSettings.getDaysCode();

        textView = (TextView) findViewById(R.id.hinweis);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        this.user = new User(this);
        if (this.user.getID()!=0) {
            loggedIn = true;
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.card_view_container);
        recyclerView.setLayoutManager(layoutManager);

        setRecycleView();

        setNFCAdapter();

        LocalDateTime now = LocalDateTime.now();
        TextView textView = findViewById(R.id.hinweis);
        LocalDateTime lastTime = user.getLastBadgedTime(now);
        if(lastTime != null) {
            textView.setText(Converter.convertLocalDateTime(now));
        }

        CardView cv = (CardView) findViewById(R.id.wrongTimesCard);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wrongTime!=null) {
                    user.setChoosenDate(wrongTime);
                    Intent intent;
                    intent = new Intent(getApplicationContext(), BadgeTimesActivity.class);
                    intent.putExtra(INTENT_CHOOSEN_DATE, user.getChoosenDate());
                    startActivity(intent);
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNFCIntent(intent);

    }

    @Override
    protected  void onResume() {
        super.onResume();
        if (nfcAdapter!=null) {
            startNFCSensor(this, nfcAdapter);
        }
        reloadCard(null);
        checkForWrongTimes();
    }

    private void checkForWrongTimes() {
        this.wrongTime = User.badgedTimesEven();
        CardView cv = (CardView) findViewById(R.id.wrongTimesCard);
        if (wrongTime==null) {
            cv.setVisibility(View.GONE);
        } else {
            cv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        if (nfcAdapter!=null) {stopNFCSensor(this, nfcAdapter);}
        super.onPause();
    }

    public void setNFCAdapter(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter != null){
            handleNFCIntent(getIntent());
        } else {
            textView.setText(R.string.noNFC);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void handleNFCIntent(Intent intent) {
        String action = intent.getAction();

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            // on successful scan, save current time
            LocalDateTime now = LocalDateTime.now();
            user.addBadgeTime(now, OwnSettings.getDaysCode());
            TextView textView = findViewById(R.id.hinweis);
            textView.setText(Converter.convertLocalDateTime(now));
            animateBackground();
        }
    }

    public void animateBackground() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.hinweis);
        tv.clearAnimation();
        tv.startAnimation(a);


        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mainActivityLayout);

        final int from = ContextCompat.getColor(this, R.color.colorPrimaryBackground);
        final int to   = ContextCompat.getColor(this, R.color.colorPrimaryBackgroundAnimatedTo);
        final int back   = ContextCompat.getColor(this, R.color.colorPrimaryBackground);

        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(from, to, back);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                rl.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });

        anim.setDuration(4000);
        anim.start();

    }

    private void startNFCSensor(Activity activity, NfcAdapter adapter) {
        Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    private void stopNFCSensor(Activity activity, NfcAdapter adapter){
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        headerMenu = menu;

        headerMenu.getItem(0).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            if (this.wrongTime!=null) {
                this.user.setChoosenDate(this.wrongTime);
                Intent intent;
                intent = new Intent(getApplicationContext(), BadgeTimesActivity.class);
                intent.putExtra(INTENT_CHOOSEN_DATE, user.getChoosenDate());
                startActivity(intent);
            }



        } else if(id == R.id.action_settings){

            Intent intent;
            intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    public Cards[] getCards() {
        long timecurrent = System.currentTimeMillis();
        Log.e("User", "starting badgetimes");
        if (cards == null) {
            cards = new Cards[3];
            cards[0] = new Cards(user, CardType.DAY);
            cards[1] = new Cards(user, CardType.WEEK);
            cards[2] = new Cards(user, CardType.MONTH);
        }
        Log.e("User", "finished loading"+(System.currentTimeMillis()-timecurrent));
        return cards;
    }

    public void reloadCard(CardType pos) {
        Log.e("User", "starting badgetimes");
        long timecurrent = System.currentTimeMillis();
        if (pos != null) {
            switch (pos) {
                case DAY:
                    cards[0] = new Cards(user, CardType.DAY);
                    break;
                case WEEK:
                    cards[1] = new Cards(user, CardType.WEEK);
                    break;
                case MONTH:
                    cards[2] = new Cards(user, CardType.MONTH);
                    break;
            }
        } else {
            cards[0] = new Cards(user, CardType.DAY);
            cards[1] = new Cards(user, CardType.WEEK);
            cards[2] = new Cards(user, CardType.MONTH);
        }
        Log.e("User", "finished loading "+(System.currentTimeMillis()-timecurrent));
        //cardAdapter.notifyDataSetChanged();
    }


    private void setRecycleView(){
        recyclerView = findViewById(R.id.card_view_container);
        int anzahl = 3;

        LocalDateTime ldt = LocalDateTime.now();
        cardAdapter = new CardAdapter(this.getCards(), this);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    public void buttonUpClicked() {
        user.plusDate(findScrollPosition());
        reloadCard(null);
        cardAdapter.notifyDataSetChanged();
    }

    public void buttonDownClicked() {
        user.minusDate(findScrollPosition());
        cardAdapter.notifyDataSetChanged();
    }

    private CardType findScrollPosition() {
        RecyclerView.LayoutManager mLayoutManager = this.recyclerView.getLayoutManager();
        View centerView = snapHelper.findSnapView(mLayoutManager);
        int pos = mLayoutManager.getPosition(centerView);
        return this.getCards()[pos].getCardType();
    }

    public void changeViewBadgesTimes() {
        Intent intent;
        CardType pos = findScrollPosition();
        switch(pos){
            case DAY:
                intent = new Intent(getApplicationContext(), BadgeTimesActivity.class);
                break;
            case WEEK:
                intent = new Intent(getApplicationContext(), BadgeTimesActivityWeek.class);
                break;
            case MONTH:
                intent = new Intent(getApplicationContext(), BadgeTimesActivityMonth.class);
                break;
            default:
                intent = new Intent(getApplicationContext(), BadgeTimesActivity.class);
                break;
        }
        intent.putExtra(INTENT_CHOOSEN_DATE, user.getChoosenDate());
        startActivity(intent);
    }

    private class switchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        }
    }
}
