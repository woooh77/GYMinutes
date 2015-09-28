package com.lift.u.ulift.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lift.u.ulift.R;

import java.util.List;

/**
 * Created by balavigneshr on 9/10/15.
 */
public class MuscleListAdapter extends ArrayAdapter<String> {

    private int itemHeight;
    private static class ViewHolder {
        TextView text1;
    }

    public MuscleListAdapter(Context context, List<String> muscleList, int listViewHeight) {
        super(context, R.layout.muscle_list_item, muscleList);
        Log.e("PARSE", "listview height - " + listViewHeight + " muscle size - " + muscleList.size());
        this.itemHeight = listViewHeight/muscleList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String muscle = getItem(position).toString();
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.muscle_list_item, parent, false);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text1.setBackgroundResource(convertView.getResources().getIdentifier(muscle, "color", "com.lift.u.ulift"));
        viewHolder.text1.setHeight(itemHeight);
        viewHolder.text1.setText(muscle);
        return convertView;
    }
}
