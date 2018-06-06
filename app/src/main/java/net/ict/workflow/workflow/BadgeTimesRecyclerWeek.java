package net.ict.workflow.workflow;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.ict.workflow.workflow.model.BadgeTimes;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BadgeTimesRecyclerWeek extends RecyclerView.Adapter<BadgeTimesRecyclerWeek.ViewHolder> {

    private ArrayList<LocalDateTime> dataSet;
    private BadgeTimesActivityWeek mainActivity;

    public BadgeTimesRecyclerWeek(BadgeTimesActivityWeek ma, LocalDateTime ldt) {
        BadgeTimes bt = new BadgeTimes();
        bt.init();
        dataSet = bt.getTimeStampsInDate(ldt.toLocalDate());
    }

    @Override
    public BadgeTimesRecyclerWeek.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BadgeTimesRecyclerWeek.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.badge_time_week);
        }
    }
}
