package com.lift.u.ulift.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lift.u.ulift.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * Created by balavigneshr on 10/2/15.
 */
public class DayArrayAdapter extends AbstractWheelTextAdapter {
    // Count of days to be shown
    private final int daysCount = 365;

    // Calendar
    Calendar calendar;

    /**
     * Constructor
     */
    public DayArrayAdapter(Context context, Calendar calendar) {
        super(context, R.layout.time_picker_custom_day, NO_RESOURCE);
        this.calendar = calendar;

        setItemTextResource(R.id.time2_monthday);
    }
    public int getToday() {
        return daysCount / 2;
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        int day = -daysCount/2 + index;
        Calendar newCalendar = (Calendar) calendar.clone();
        newCalendar.roll(Calendar.DAY_OF_YEAR, day);

        View view = super.getItem(index, cachedView, parent);

        TextView day2 = (TextView) view.findViewById(R.id.time2_day);
        DateFormat format0 = new SimpleDateFormat("d");
        day2.setText(format0.format(newCalendar.getTime()));

        TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);
        DateFormat format = new SimpleDateFormat("EEE");
        weekday.setText(format.format(newCalendar.getTime()));

        TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
        DateFormat format1 = new SimpleDateFormat("MMM yy");
        monthday.setText(format1.format(newCalendar.getTime()));
        DateFormat dFormat = new SimpleDateFormat("MMM yy");
        view.setTag(dFormat.format(newCalendar.getTime()));
        return view;
    }

    @Override
    public int getItemsCount() {
        return daysCount + 1;
    }

    @Override
    public CharSequence getItemText(int index) {
        return "";
    }
}