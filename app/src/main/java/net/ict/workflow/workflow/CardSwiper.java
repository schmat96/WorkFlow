package net.ict.workflow.workflow;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CardSwiper implements View.OnTouchListener {


    private final GestureDetector gestureDetector;
    private Context context;

    public CardSwiper(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e){
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean executed = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        executed = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    executed = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return executed;

        }

        public void onSwipeRight() {
            Toast.makeText(context, "right", Toast.LENGTH_SHORT);
        }

        public void onSwipeLeft() {
            Toast.makeText(context, "left", Toast.LENGTH_SHORT);
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }


    }
}
