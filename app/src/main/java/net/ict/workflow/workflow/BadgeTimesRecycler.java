package net.ict.workflow.workflow;

import android.content.Intent;
import android.graphics.Color;
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

import com.google.common.io.Resources;

import net.ict.workflow.workflow.model.BadgeTimes;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BadgeTimesRecycler extends RecyclerView.Adapter<BadgeTimesRecycler.ViewHolder> {

    private ArrayList<LocalDateTime> dataSet;
    private LocalDateTime localDateTime;
    private BadgeTimesActivity badgeTimesActivity;

    private boolean inOrOut;
    private boolean timeError;

    public BadgeTimesRecycler(@NonNull User user, BadgeTimesActivity bta) {
        dataSet = User.getTimeStampsInDate(user.getChoosenDate().toLocalDate());
        if(getItemCount() % 2 != 0) {
            timeError = true;
        }
        badgeTimesActivity = bta;
    }

    public BadgeTimesRecycler(@NonNull User user) {
        dataSet = User.getTimeStampsInDate(user.getChoosenDate().toLocalDate());
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
        ImageView iv = holder.cardView.findViewById(R.id.symbol);
        if(timeError && position == getItemCount() - 1) {
            holder.cardView.setBackgroundResource(R.color.wrongTime);
            tv.setText(badgeTimesActivity.getResources().getString(R.string.wrongTimeRead));
            iv.clearColorFilter();
        } else {
            tv.setText(Converter.convertLocalDateTime(dataSet.get(position)));
            if (inOrOut) {
                iv.setImageResource(R.drawable.arrow_left);
            } else {
                iv.setImageResource(R.drawable.arrow_right);
            }
            inOrOut = !inOrOut;
        }
    }


    @Override
    public int getItemCount() {
        if(timeError) {
            return dataSet.size() + 1;
        } else {
            return dataSet.size();
        }
    }

    public void removeAtPosition(int pos) {
        User.removeWithValue(dataSet.get(pos));
        dataSet.remove(pos);
        this.notifyItemRemoved(pos);
        this.notifyItemRangeChanged(pos, this.getItemCount());
        if (badgeTimesActivity!=null) {
            badgeTimesActivity.updateCard();
        }


    }

    public LocalDateTime getDateAtPosition(int pos) {
        return dataSet.get(pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.badge_time);
        }
    }
}

