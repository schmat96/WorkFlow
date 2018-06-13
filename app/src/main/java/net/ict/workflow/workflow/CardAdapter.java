package net.ict.workflow.workflow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Cards[] dataSet;
    private MainActivity mainActivity;
    private int currentIndex = 0;

    public CardAdapter(Cards[] bars, MainActivity ma) {
        this.dataSet = bars;
        this.mainActivity = ma;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardViewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardViewLayout);
        cardViewLayout.setOnTouchListener(viewListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {

        Bar bar = holder.cardView.findViewById(R.id.bar);
        bar.setValue(dataSet[position].getMax(), dataSet[position].getZeit());



        TextView tv = holder.cardView.findViewById(R.id.cardViewTitle);
        tv.setText(dataSet[position].title());

        TextView tv2 = holder.cardView.findViewById(R.id.date);
        tv2.setText(dataSet[position].getDate());

        ImageButton butUp = holder.cardView.findViewById(R.id.upButton);
        butUp.setOnClickListener(buttonUpOnClickListener);

        ImageButton butDown = holder.cardView.findViewById(R.id.downButton);
        butDown.setOnClickListener(buttonDownOnClickListener);


    }

    public void swipeLeft() {
        swipe(currentIndex+1);
    }

    public void swipeRight() {
        swipe(currentIndex-1);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    private void swipe(int newIndex) {

    }

    private View.OnTouchListener viewListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                mainActivity.changeViewBadgesTimes();
            }
            return true;
        }
    };

    private View.OnClickListener buttonUpOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivity.buttonUpClicked();
        }
    };



    private View.OnClickListener buttonDownOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivity.buttonDownClicked();
        }
    };

}
