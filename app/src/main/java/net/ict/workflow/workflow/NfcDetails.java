package net.ict.workflow.workflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NfcDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_details);

        TextView view = findViewById(R.id.details);
        view.setText(getIntent().getStringExtra("info"));
    }
}
