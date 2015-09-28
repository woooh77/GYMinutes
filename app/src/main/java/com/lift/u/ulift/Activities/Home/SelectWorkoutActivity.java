package com.lift.u.ulift.Activities.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lift.u.ulift.Activities.MainActivity;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by balavigneshr on 9/15/15.
 */
public class SelectWorkoutActivity extends AppCompatActivity {
    SwipeMenuListView listView;
    String routine_name, color;
    SharedPreferences pref;
    ArrayList<String> workouts = new ArrayList<>();
    ArrayAdapter<String> adapter;
    LinearLayout no_workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        pref = getSharedPreferences("Routine", Context.MODE_PRIVATE);
        routine_name = pref.getString("ROUTINE_NAME", "New Routine");
        Log.e("PARSE", "ROUTINE NAME - " + routine_name);
        color = pref.getString("COLOR", "Red");
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        getWorkoutsFromDB();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, workouts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                if (workouts.size() != 0) {
                    Resources resources = SelectWorkoutActivity.this.getResources();
                    if (!color.isEmpty()) {
                        textView.setBackgroundColor(resources.getColor(resources.getIdentifier(color, "color", getApplicationContext().getPackageName())));
                        textView.setTextColor(resources.getColor(R.color.white));
                    }
                    textView.setHeight(150);
                }
                return textView;
            }
        };
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SelectWorkoutActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setContentText("Do you want to start this workout?")
                        .setCustomImage(R.drawable.icon)
                        .setConfirmText("Yes,let's do it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(SelectWorkoutActivity.this, MainActivity.class);
                                intent.putExtra("ROUTINE", routine_name);
                                intent.putExtra("WORKOUT", workouts.get(position));
                                startActivity(intent);
                            }
                        });
                sweetAlertDialog.setCanceledOnTouchOutside(true);
                sweetAlertDialog.setTitleText(workouts.get(position));
                sweetAlertDialog.show();
            }
        });

        if (workouts.size() == 0) {
            TextView no_workout = (TextView) findViewById(R.id.no_workout);
            no_workout.setText("Please go to Workout Routine page to add workouts!");
        } else {
            no_workout = (LinearLayout) findViewById(R.id.no_routine_text);
            no_workout.setVisibility(View.GONE);
        }
        getSupportActionBar().setTitle(routine_name);
    }

    private void getWorkoutsFromDB() {
        SQLiteDatabase rdb = (new DatabaseHelper(this)).getReadableDatabase();
        String[] projection = {Tables.UserWorkout.WorkoutName};
        String sortOrder = Tables.UserWorkout.WorkoutName + " ASC";
        String[] values = {routine_name};
        Cursor c = rdb.query(
                Tables.UserWorkout.table_name,// The table to query
                projection,                          // The columns to return
                Tables.UserWorkout.RoutineName + "=?",                                // The columns for the WHERE clause
                values,                                // The values for the WHERE clause
                projection[0],                                // don't group the rows
                null,                                // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i ++) {
            workouts.add(c.getString(c.getColumnIndexOrThrow(projection[0])));
            c.moveToNext();
        }
    }
}