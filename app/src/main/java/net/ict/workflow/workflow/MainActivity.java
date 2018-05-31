package net.ict.workflow.workflow;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.ict.workflow.workflow.model.NFCReaderTask;
import net.ict.workflow.workflow.model.User;


public class  MainActivity extends AppCompatActivity {

    User user;
    Boolean loggedIn = false;
    Menu headerMenu;
    Toolbar toolbar;
    private static String TAG = "MainActivity";

    private NfcAdapter nfcAdapter;
    private Switch nfcswitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView myList = (RecyclerView) findViewById(R.id.card_view_container);
        myList.setLayoutManager(layoutManager);
        CardView cardView = findViewById(R.id.card_view);



        //setCardSwiper(cardView);
        setRecycleView();

        setNFCAdapter();
        if (nfcAdapter!=null) {
            handleNFCIntent(getIntent());
        }

        nfcswitch = (Switch) findViewById(R.id.nfcswitch);
        nfcswitch.setOnCheckedChangeListener(new switchListener());



    }

    @Override
    protected void onStart() {
        super.onStart();
        this.user = new User();
        if (this.user.getID()!=0) {
            loggedIn = true;
        }
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
        if (nfcAdapter!=null) {
            stopNFCSensor(this, nfcAdapter);
        }
        super.onPause();
    }

    public void setNFCAdapter(){
        Log.d("NFCDemo", "Checking for NFC activated");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        TextView textView = (TextView) findViewById(R.id.hinweis);
        if(nfcAdapter != null){
            if(!nfcAdapter.isEnabled()){
                textView.setText("NFC is disabled.");
                Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG);
            } else {
                textView.setText("NFC is activated.");
                Toast.makeText(this, "NFC is activated.", Toast.LENGTH_LONG);
            }
            handleNFCIntent(getIntent());
        } else {
            textView.setText("This device doesn't support NFC.");
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG);
        }
    }

    private void handleNFCIntent(Intent intent) {
        /*
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];

                    TextView hinweis = (TextView) findViewById(R.id.hinweis);
                    hinweis.setText(messages[i].toString());
                    Toast.makeText(this, messages[i].toString(), Toast.LENGTH_LONG);
                }

            }
        }*/
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if ("text/plain".equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NFCReaderTask(this).execute(tag);

            } else {
                Log.d("NFCDemo", "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NFCReaderTask(this).execute(tag);
                    break;
                }
            }
        }
    }

    private void startNFCSensor(Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

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
        } else if (id == R.id.action_settings) {
            final Intent intent = new Intent(NfcAdapter.ACTION_NDEF_DISCOVERED);
            intent.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, "Custom Messages");
            startActivity(intent);

        }

        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setRecycleView(){
        RecyclerView recyclerView = findViewById(R.id.card_view_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        int anzahl = 10;
        Cards[] cards = new Cards[anzahl];
        for (int i=0;i<anzahl;i++) {
            cards[i] = new Cards((i+4)/3f, (i+1)/3);
        }

        CardAdapter cardAdapter = new CardAdapter(cards);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SwipeController swipeController = new SwipeController(cardAdapter);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

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
