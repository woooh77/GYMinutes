package com.lift.u.ulift.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lift.u.ulift.R;
import com.lift.u.ulift.models.SelectRoutines;

import java.util.ArrayList;

/**
 * Created by balavigneshr on 9/21/15.
 */
public class SelectRoutineAdapter extends ArrayAdapter<SelectRoutines> {
    private final ArrayList<SelectRoutines> values;
    private Context context;

    public SelectRoutineAdapter(Context context, ArrayList<SelectRoutines> values) {
        super(context, android.R.layout.simple_list_item_2, values);
        this.values = values;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectRoutines routine_list = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        if (routine_list != null) {
            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
            TextView text2 = (TextView) view.findViewById(android.R.id.text2);
            text1.setText(routine_list.getRoutine_name());
            String summary = routine_list.getSummary();
            if (summary != null || !summary.isEmpty())
                text2.setText(summary);
            String color = routine_list.getColor();
            if (color != null && !color.isEmpty()) {
                Resources resources = view.getResources();
                view.setBackgroundColor(resources.getColor(resources.getIdentifier(color, "color", view.getContext().getPackageName())));
                text1.setTextColor(resources.getColor(R.color.white));
                text2.setTextColor(resources.getColor(R.color.white));
            }
        }
        return view;
    }
}