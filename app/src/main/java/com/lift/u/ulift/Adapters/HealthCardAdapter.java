package com.lift.u.ulift.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.lift.u.ulift.R;
import com.lift.u.ulift.models.HealthCards;

import java.util.ArrayList;

/**
 * Created by balavigneshr on 8/14/15.
 */
public class HealthCardAdapter extends ArrayAdapter<HealthCards> {
    private final Context context;
    private final ArrayList<HealthCards> values;

    public HealthCardAdapter(Context context, ArrayList<HealthCards> values) {
        super(context, R.layout.health_cards, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.health_cards, parent, false);
        TextView exercise_name = (TextView) rowView.findViewById(R.id.exercise_name);
        TextView sets = (TextView) rowView.findViewById(R.id.tsets);
        TextView weights_moved = (TextView) rowView.findViewById(R.id.tweights);
        LinearLayout l1 = (LinearLayout) rowView.findViewById(R.id.l1);
        TableLayout t1 = (TableLayout) rowView.findViewById(R.id.t1);
        ImageView progress_view = (ImageView) rowView.findViewById(R.id.progress);
        HealthCards health_card = values.get(position);
        if (health_card != null) {
            exercise_name.setText(health_card.getExercise_name());
            sets.setText(String.valueOf(health_card.getSets_today()));
            weights_moved.setText(String.valueOf(health_card.getWeight_moved()));
            l1.setBackgroundResource(rowView.getResources().getIdentifier(health_card.getColor(), "color", "com.lift.u.ulift"));
            t1.setBackgroundResource(rowView.getResources().getIdentifier(health_card.getColor(), "color", "com.lift.u.ulift"));
            int progress = health_card.getProgress();
            // set progress icon here;
        }
        return rowView;
    }
}
