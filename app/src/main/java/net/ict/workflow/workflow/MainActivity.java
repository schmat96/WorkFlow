package net.ict.workflow.workflow;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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

import net.ict.workflow.workflow.model.User;
import net.ict.workflow.workflow.record.NdefMessageParser;
import net.ict.workflow.workflow.record.ParsedNdefRecord;

import java.util.List;


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
        if(nfcAdapter != null){
            handleNFCIntent(getIntent());
        }
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
        if (nfcAdapter!=null) {stopNFCSensor(this, nfcAdapter);}
        super.onPause();
    }

    public void setNFCAdapter(){
        Log.d("NFCDemo", "Checking for NFC activated");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        TextView textView = (TextView) findViewById(R.id.hinweis);
        if(nfcAdapter != null){
            handleNFCIntent(getIntent());
        } else {
            textView.setText("This device doesn't support NFC.");
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG);
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
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                msg = new NdefMessage[] {message};
                Log.e("Inside", msg.toString());
            }
            displayMessages(msg);
        }

        /*
        Toast.makeText(this, "Scan started", Toast.LENGTH_SHORT);
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
        }*/
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }



    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
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

        if(nfcAdapter != null){
            TextView textView = findViewById(R.id.hinweis);
            if(!nfcAdapter.isEnabled()){
                textView.setText("NFC is disabled.");
                textView.invalidate();
                Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG);
            } else {
                textView.setText("NFC is activated.");
                textView.invalidate();
                Toast.makeText(this, "NFC is activated.", Toast.LENGTH_LONG);
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
            Intent nfcIntent = new Intent(NfcAdapter.ACTION_TAG_DISCOVERED);
            nfcIntent.setType("text/plain");
            nfcIntent.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, "Custom Messages");
            startActivity(nfcIntent);
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
