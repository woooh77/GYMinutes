package com.lift.u.ulift.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lift.u.ulift.R;
import com.lift.u.ulift.models.Exercises;

import java.util.ArrayList;

/**
 * Created by balavigneshr on 9/10/15.
 */
public class ExerciseListAdapter extends ArrayAdapter<Exercises> {
    private final ArrayList<Exercises> values;
    private Context context;

        public ExerciseListAdapter(Context context, ArrayList<Exercises> values) {
        super(context, R.layout.exercise_list_item, values);
        this.values = values;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Exercises exercises = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.exercise_list_item, parent, false);
        LinearLayout listItem = (LinearLayout) rowView.findViewById(R.id.listItem);
        listItem.setBackgroundColor(rowView.getResources().getColor(rowView.getResources().getIdentifier(exercises.getColor(), "color", getContext().getPackageName())));
        TextView exerciseName = (TextView) rowView.findViewById(R.id.exercise_name);
        TextView sets = (TextView) rowView.findViewById(R.id.reps);
        TextView reps = (TextView) rowView.findViewById(R.id.reps);
        TextView restTimer = (TextView) rowView.findViewById(R.id.restTimer);
        exerciseName.setText(exercises.getExerciseName());
        sets.setText(Integer.toString(exercises.getSets()));
        reps.setText(Integer.toString(exercises.getReps()));
        restTimer.setText(Integer.toString(exercises.getRestTimer()));
        return rowView;
    }
}
