package net.ict.workflow.workflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import net.ict.workflow.workflow.AddTimeAcitivity;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;

import static net.ict.workflow.workflow.MainActivity.INTENT_CHOOSEN_DATE;

public class EditTimeActivity extends AddTimeAcitivity {



    protected void addActionListeners() {
        Button but = (Button) findViewById(R.id.confirmButton);
        but.setOnClickListener(buttonConfirmListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public View.OnClickListener buttonConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePicker tp = (TimePicker) findViewById(R.id.timepicker);
            LocalDateTime newTime = ldt.toLocalDate().atTime(tp.getHour(), tp.getMinute());
            User.updateBadgeTime(ldt, newTime);
            Intent intent = new Intent(getApplicationContext(), BadgeTimesActivity.class);
            intent.putExtra(INTENT_CHOOSEN_DATE, ldt);
            finish();
            startActivity(intent);
        }
    };
}
