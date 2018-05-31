package net.ict.workflow.workflow;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NfcActivity extends AppCompatActivity {

    TextView read_data;
    TextView show_data;
    Tag detected_tag;
    NfcAdapter nfcAdapter;
    IntentFilter[] intentFilters;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String MIME_IMAGE_ALL = "image/*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PackageManager pm = this.getPackageManager();
        show_data = (TextView) findViewById(R.id.hinweis);
        nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        read_data = (TextView) findViewById(R.id.hinweis2);
        read_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NfcActivity.this);
                        builder.setMessage("NFC feature is not available on this device!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "NFC feature is available on this device!", Toast.LENGTH_SHORT).show();
                    HandleIntent(getIntent());
                }
            }
        });
    }

    public void HandleIntent(Intent intent)
    {
        String action = intent.getAction();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            detected_tag = getIntent().getParcelableExtra(nfcAdapter.EXTRA_TAG);
            NDefReaderTask NDefReader = new NDefReaderTask();
            NDefReader.execute();
        }
    }

    public void onResume()
    {
        super.onResume();
        if(nfcAdapter != null)
            setupForeGroundDispatch(this, nfcAdapter);
    }

    public void onPause()
    {
        super.onPause();
        if(nfcAdapter != null)
            stopForeGroundDispatch(this, nfcAdapter);
    }

    public void onNewIntent(Intent intent)
    {
        HandleIntent(intent);
    }

    public void setupForeGroundDispatch (final Activity activity, NfcAdapter nfcAdapter)
    {
        Intent new_intent = new Intent(getApplicationContext(),NfcActivity.class);
        new_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,new_intent,0);
        intentFilters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        intentFilters[0] = new IntentFilter();
        intentFilters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        intentFilters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            intentFilters[0].addDataType(MIME_TEXT_PLAIN);
            intentFilters[0].addDataType(MIME_IMAGE_ALL);
        }
        catch(IntentFilter.MalformedMimeTypeException me)
        {
            me.printStackTrace();
        }

        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilters, techList);
    }

    public void stopForeGroundDispatch (final Activity activity, NfcAdapter nfcAdapter)
    {
        nfcAdapter.disableForegroundDispatch(activity);
    }

    public class NDefReaderTask extends AsyncTask<Tag, Void, String>
    {
        @Override
        protected String doInBackground(Tag... params)
        {
            try
            {
                detected_tag = params[0];
                Ndef ndef = Ndef.get(detected_tag);
                ndef.connect();
                if(ndef != null)
                {
                    NdefMessage ndefMessage = ndef.getCachedNdefMessage();
                    NdefRecord[] records = ndefMessage.getRecords();
                    for(NdefRecord ndefRecord : records)
                    {
                        if((ndefRecord.getTnf() == NdefRecord.TNF_ABSOLUTE_URI) || (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN))
                        {
                            byte[] payload = ndefRecord.getPayload();
                            String encoding1 = "UTF-8";
                            String encoding2 = "UTF-16";
                            String textEncoding = ((payload[0] & 128) == 0) ? encoding1 : encoding2;
                            int languageCodeLength = payload[0] & 0063;
                            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                        }
                    }
                }
                ndef.close();
            }
            catch (UnsupportedEncodingException UE)
            {
                UE.printStackTrace();
            }
            catch (IOException IE)
            {
                IE.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {

        }

        protected void onPostExecute(String result)
        {
            if(result != null)
            {
                show_data.setText(result);
            }
        }
    }
}
