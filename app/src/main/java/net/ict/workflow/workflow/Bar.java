package net.ict.workflow.workflow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

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



    /**
     *
     * @param sum max value for the bar. F.E. 8.24 hours.
     * @param valueLeft Left Bar filled. Can be higher then sum value.
     */
    public void setValue(float sum, float valueLeft) {

        TextView textLeft = (TextView) findViewById(R.id.app_bar_textleft);
        TextView textMid = (TextView) findViewById(R.id.app_bar_textmid);
        TextView textRight = (TextView) findViewById(R.id.app_bar_textright);

        textMid.setText("Max: "+sum);
        textRight.setText("Current: "+valueLeft);

        LinearLayout linearLayoutMain = (LinearLayout) findViewById(R.id.app_bar_bar);
        LinearLayout linearLayoutLeft = (LinearLayout) findViewById(R.id.app_bar_left);
        LinearLayout linearLayoutRight = (LinearLayout) findViewById(R.id.app_bar_right);
        manipulateColor(linearLayoutRight);
        linearLayoutMain.setWeightSum(sum);

        Boolean left = true;

        while (valueLeft>sum) {
            valueLeft = valueLeft - sum;

            if (left) {
                switchColors (linearLayoutLeft,linearLayoutRight);
                manipulateColor(linearLayoutLeft);
            } else {
                switchColors (linearLayoutRight, linearLayoutLeft);
                manipulateColor(linearLayoutRight);
            }
            left = !left;
        }

        if (left) {
            setValuesLinearLayout(linearLayoutLeft,valueLeft);
            setValuesLinearLayout(linearLayoutRight,sum-valueLeft);
        } else {
            setValuesLinearLayout(linearLayoutLeft,sum-valueLeft);
            setValuesLinearLayout(linearLayoutRight,valueLeft);
        }




    }

    private void setValuesLinearLayout(LinearLayout ll,float val) {
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                val
        );
        ll.setLayoutParams(param1);
    }

    private  void manipulateColor(LinearLayout layout) {
        int color = Color.TRANSPARENT;
        float factor = 0.85f;
        Drawable background = layout.getBackground();
        if (background instanceof ColorDrawable) {
            color = ((ColorDrawable) background).getColor();
        }

        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        layout.setBackgroundColor(Color.argb(a, Math.min(r,255), Math.min(g,255), Math.min(b,255)));
    }

    private void switchColors(LinearLayout ll, LinearLayout lr) {
        int color2 = Color.TRANSPARENT;
        Drawable background2 = lr.getBackground();
        if (background2 instanceof ColorDrawable) {
            color2 = ((ColorDrawable) background2).getColor();
        }

        ll.setBackgroundColor(color2);
    }

}
