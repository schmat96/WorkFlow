/*package net.ict.workflow.workflow.model;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import net.ict.workflow.workflow.R;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class NFCReaderTask extends AsyncTask<Tag, Void, String> {

    Activity activity;

    public NFCReaderTask(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];
        Ndef ndef = Ndef.get(tag);
        if(ndef == null) {
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for(NdefRecord ndefRecord : records){
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e("NFCDemo", "Unsupported Encoding", e);
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    protected void onPostExecute(String result) {
        if(result != null){
            TextView hinweis = activity.findViewById(R.id.hinweis);
            hinweis.setText("Read content: " + result);
            hinweis.invalidate();
            Toast.makeText(activity, "Read content" + result, Toast.LENGTH_LONG);
        }
    }
}*/
