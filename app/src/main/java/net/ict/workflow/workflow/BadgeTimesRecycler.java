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
import android.widget.ImageView;
import android.widget.TextView;

import net.ict.workflow.workflow.model.BadgeTimes;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BadgeTimesRecycler extends RecyclerView.Adapter<BadgeTimesRecycler.ViewHolder> {

    private ArrayList<LocalDateTime> dataSet;

    private boolean inOrOut;

    public BadgeTimesRecycler(@NonNull User user) {
        dataSet = user.getBadgeTimes().getTimeStampsInDate(user.getChoosenDate().toLocalDate());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardViewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_time, parent, false);
        BadgeTimesRecycler.ViewHolder viewHolder = new BadgeTimesRecycler.ViewHolder(cardViewLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BadgeTimesRecycler.ViewHolder holder, int position) {
        TextView tv = holder.cardView.findViewById(R.id.badgeTimeTextView);
        tv.setText(dataSet.get(position).toString());
        ImageView iv = holder.cardView.findViewById(R.id.symbol);
        if (inOrOut) {
            iv.setImageResource(R.drawable.arrow_left);
        } else {
            iv.setImageResource(R.drawable.arrow_right);
        }
        inOrOut = !inOrOut;
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.badge_time);
        }
    }
}
