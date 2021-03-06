package net.ict.workflow.workflow;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ict.workflow.workflow.model.BadgeTimes;
import net.ict.workflow.workflow.model.CardType;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static net.ict.workflow.workflow.MainActivity.INTENT_CHOOSEN_DATE;

public class BadgeTimesRecyclerWeek extends RecyclerView.Adapter<BadgeTimesRecyclerWeek.ViewHolder> {

    private ArrayList<LocalDateTime> dataSet;
    //private BadgeTimes badgeTimes;
    private BadgeTimesActivityWeek mainActivity;
    private LocalDateTime ldt;

    public BadgeTimesRecyclerWeek(BadgeTimesActivityWeek ma, LocalDateTime ldt) {
        dataSet = initDataset(ldt);
        mainActivity = ma;
        this.ldt = ldt;
    }

    @Override
    public BadgeTimesRecyclerWeek.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardViewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_time_week, parent, false);
        BadgeTimesRecyclerWeek.ViewHolder viewHolder = new BadgeTimesRecyclerWeek.ViewHolder(cardViewLayout);
        // on touch: go to single day view
        cardViewLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(mainActivity.getApplicationContext(), BadgeTimesActivity.class);
                    intent.putExtra(INTENT_CHOOSEN_DATE, dataSet.get(viewHolder.getAdapterPosition()));
                    mainActivity.startActivity(intent);
                }
                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BadgeTimesRecyclerWeek.ViewHolder holder, int position) {
        TextView textView = holder.cardView.findViewById(R.id.day_card);
        textView.setText(dataSet.get(position).getDayOfWeek().toString());

        Bar bar = holder.cardView.findViewById(R.id.bar);

        bar.setValue(User.getMaxTime(CardType.DAY, dataSet.get(position)), User.getBadgeTime(CardType.DAY, dataSet.get(position)));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    private ArrayList<LocalDateTime> initDataset(LocalDateTime ldt){
        ArrayList<LocalDateTime> week = new ArrayList<>();
        int currentDay = ldt.getDayOfWeek().getValue();
        for(int i = 1; i < currentDay; i++){
            week.add(ldt.minusDays(currentDay - i));
        }
        for(int j = currentDay; j <= 7; j++){
            week.add(ldt.plusDays(j - currentDay));
        }
        return week;
    }


    /**
     * Inner Class ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.badge_time_week);
        }
    }

}
