package net.ict.workflow.workflow;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ict.workflow.workflow.model.BadgeTimes;
import net.ict.workflow.workflow.model.CardType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BadgeTimesRecyclerMonth extends RecyclerView.Adapter<BadgeTimesRecyclerMonth.ViewHolder> {

    private BadgeTimes badgeTimes;
    private ArrayList<LocalDateTime> dataSet;
    private WeekFields weekFields;

    public BadgeTimesRecyclerMonth(BadgeTimesActivityMonth ma, LocalDateTime ldt) {
        badgeTimes = new BadgeTimes();
        badgeTimes.init();
        dataSet = initDataset(ldt);

        weekFields = WeekFields.of(Locale.getDefault());
    }

    @Override
    public BadgeTimesRecyclerMonth.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardViewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_time_month, parent, false);
        BadgeTimesRecyclerMonth.ViewHolder viewHolder = new BadgeTimesRecyclerMonth.ViewHolder(cardViewLayout);
        //TODO HERE set onclick listener for badgetime single week view
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BadgeTimesRecyclerMonth.ViewHolder holder, int position) {
        TextView textView = holder.cardView.findViewById(R.id.week_card);
        textView.setText("Week " + dataSet.get(position).get(weekFields.weekOfWeekBasedYear()));

        Bar bar = holder.cardView.findViewById(R.id.bar);
        bar.setValue(badgeTimes.getMax(CardType.WEEK, dataSet.get(position)), badgeTimes.getBadgedTimeWeek(dataSet.get(position)));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private ArrayList<LocalDateTime> initDataset(LocalDateTime ldt){
        ArrayList<LocalDateTime> weeks = new ArrayList<>();

        LocalDate firstDay = ldt.toLocalDate().withDayOfMonth(1);
        LocalDate lastDay = ldt.toLocalDate().withDayOfMonth((ldt.toLocalDate().lengthOfMonth()));

        LocalDateTime dateTime;
        while(firstDay.compareTo(lastDay) <= 0){
            dateTime = firstDay.atStartOfDay();
            weeks.add(dateTime);
            firstDay = firstDay.plusDays(7);
        }
        return weeks;
    }


    /**
     * Inner Class ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.badge_time_month);
        }
    }


}
