package net.ict.workflow.workflow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class Bar extends LinearLayout {
    public Bar(Context context) {
        super(context);
    }

    public Bar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Bar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Bar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setValue(int v) {
        LinearLayout linearLayoutLeft = (LinearLayout) findViewById(R.id.app_bar_left);
        LinearLayout linearLayoutRight = (LinearLayout) findViewById(R.id.app_bar_right);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                10f
        );

        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                5f
        );

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                5f
        );

        this.setLayoutParams(param);
        linearLayoutLeft.setLayoutParams(param1);
        linearLayoutRight.setLayoutParams(param2);

    }
}
