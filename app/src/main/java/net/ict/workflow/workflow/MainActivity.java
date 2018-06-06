package net.ict.workflow.workflow;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.ict.workflow.workflow.model.CardType;
import net.ict.workflow.workflow.model.User;
import net.ict.workflow.workflow.record.NdefMessageParser;
import net.ict.workflow.workflow.record.ParsedNdefRecord;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;


public class  MainActivity extends AppCompatActivity {

    public static final String INTENT_CHOOSEN_DATE = "CHOOSEN_DATE";
    private static String TAG = "MainActivity";

    User user;
    Boolean loggedIn = false;
    Menu headerMenu;
    Toolbar toolbar;


    private NfcAdapter nfcAdapter;
    private Switch nfcswitch;
    private TextView textView;
    private RecyclerView recyclerView;
    private SnapHelper snapHelper;
    private CardAdapter cardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        textView = (TextView) findViewById(R.id.hinweis);
        nfcswitch = findViewById(R.id.nfcswitch);
        setSupportActionBar(toolbar);

        this.user = new User();
        if (this.user.getID()!=0) {
            loggedIn = true;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.card_view_container);
        recyclerView.setLayoutManager(layoutManager);

        setRecycleView();

        setNFCAdapter();


        nfcswitch.setOnCheckedChangeListener(new switchListener());

        //if(nfcAdapter != null){
            //handleNFCIntent(getIntent());
        //}
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

    }

    @Override
    protected void onPause() {
        if (nfcAdapter!=null) {stopNFCSensor(this, nfcAdapter);}
        super.onPause();
    }

    public void setNFCAdapter(){
        Log.d("NFCDemo", "Checking for NFC activated");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter != null){
            handleNFCIntent(getIntent());
        } else {
            textView.setText("This device doesn't support NFC.");
            textView.setVisibility(View.VISIBLE);
            nfcswitch.setVisibility(View.GONE);
        }
    }

    private void handleNFCIntent(Intent intent) {
        String action = intent.getAction();
        Log.e("NFCDemo", action);

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msg;

            if(rawMsg != null){
                msg = new NdefMessage[rawMsg.length];
                for(int i = 0; i < rawMsg.length; i++) {
                    msg[i] = (NdefMessage) rawMsg[i];
                }
            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag =  intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = NfcReader.dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                msg = new NdefMessage[] {message};
                Log.e("Inside", msg.toString());
            }
            displayMessages(msg);
        }
    }



    private void displayMessages(NdefMessage[] msg){
        if(msg == null || msg.length == 0){
            return;
        }

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msg[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++){
            ParsedNdefRecord record = records.get(i);
            String string = record.str();
            builder.append(string).append("\n");
        }
        //TextView text = findViewById(R.id.hinweis);
        //text.setText(builder.toString());
        //text.invalidate();
        Intent intent = new Intent(getApplicationContext(), NfcDetails.class);
        intent.putExtra("info", builder.toString());
        startActivity(intent);
        Log.e("finalMessage", builder.toString());
        //Log.v("textView", text.getText().toString());
    }

    private void startNFCSensor(Activity activity, NfcAdapter adapter) {
        Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Log.e("NFCDemo", "intent set");
        PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);


        TextView textview = (TextView) findViewById(R.id.hinweis);

        if(nfcAdapter != null){

            if(!nfcAdapter.isEnabled()){
                textview.setVisibility(View.GONE);
                nfcswitch.setVisibility(View.VISIBLE);
                nfcswitch.setChecked(false);
                //Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG);
            } else {
                nfcswitch.setChecked(true);
                //Toast.makeText(this, "NFC is activated.", Toast.LENGTH_LONG);
            }
        }

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
        if (loggedIn) {
            headerMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.logged));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            loggedIn = true;
            if (loggedIn) {
                headerMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.logged));
            }
            return true;
        } else if(id == R.id.action_settings){

            //TODO Change to Settings
            LinearLayout ll = (LinearLayout) findViewById(R.id.scanHinweis);
            TransitionDrawable transition = (TransitionDrawable) ll.getBackground();
            transition.startTransition(1200);

            DialogFragment newFragmentDate = new DatePickerFragment();
            newFragmentDate.show(this.getFragmentManager(), "datePicker");

            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(this.getFragmentManager(), "timePicker");


            //newFragment.show(this.getSupportFragmentManager(), "timePicker");
            //user.addBadgeTime(LocalDateTime.now());
            // TODO get current position of recyclerView
            //user.reloadCard(0, LocalDateTime.now(), this);

        }
        return super.onOptionsItemSelected(item);
    }


    private void setRecycleView(){
        recyclerView = findViewById(R.id.card_view_container);
        int anzahl = 3;

        //TODO hier "this" mitzugeben ist nicht schön aber ich habe keinen anderen Weg gefunden um auf die R.string.* zuzugreiffen.
        LocalDateTime ldt = LocalDateTime.now();
        cardAdapter = new CardAdapter(this.user.getCards(), this);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //SwipeController swipeController = new SwipeController(cardAdapter);
        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    public void buttonUpClicked() {
        user.plusDate(findScrollPosition());
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
        return this.user.getCards()[pos].getCardType();
    }

    public void changeViewBadgesTimes() {
        Intent intent = new Intent(getApplicationContext(), BadgeTimesActivity.class);
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

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }

}
