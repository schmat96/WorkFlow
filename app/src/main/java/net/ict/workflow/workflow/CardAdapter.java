package net.ict.workflow.workflow;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Cards[] dataSet;

    private int currentIndex = 0;


    public CardAdapter(Cards[] bars) {
        this.dataSet = bars;


    }



    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardViewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardViewLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        //TODO bind params to cards

        Bar bar = holder.cardView.findViewById(R.id.bar);
        bar.setValue(dataSet[position].getMax(), dataSet[position].getZeit());

        Bar bar2 = holder.cardView.findViewById(R.id.bar2);
        bar2.setValue(dataSet[position].getMax(), dataSet[position].getZeit());

        Bar bar3 = holder.cardView.findViewById(R.id.bar3);
        bar3.setValue(dataSet[position].getMax(), dataSet[position].getZeit());


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

}
