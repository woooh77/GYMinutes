package com.lift.u.ulift.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lift.u.ulift.R;
import com.lift.u.ulift.models.WorkoutHistory;

import java.util.ArrayList;

/**
 * Created by balavigneshr on 10/5/15.
 */
public class WorkoutHistoryAdapter extends ArrayAdapter<WorkoutHistory> {
    private final ArrayList<WorkoutHistory> values;
    private Context context;

    public WorkoutHistoryAdapter(Context context, ArrayList<WorkoutHistory> values) {
        super(context, R.layout.list_item_workout, values);
        this.values = values;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WorkoutHistory exercises = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_workout, parent, false);
        TextView id = (TextView) rowView.findViewById(R.id.id);
        TextView reps = (TextView) rowView.findViewById(R.id.reps);
        TextView weight = (TextView) rowView.findViewById(R.id.weight);
        id.setText(String.valueOf(position + 1));
        reps.setText(Integer.toString(exercises.getSets()) + " Reps");
        weight.setText(Float.toString(exercises.getWeight()) + " Lbs");
        return rowView;
    }

}