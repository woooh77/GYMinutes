package com.lift.u.ulift.Activities.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lift.u.ulift.Activities.MainActivity;
import com.lift.u.ulift.Adapters.SelectRoutineAdapter;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.lift.u.ulift.models.SelectRoutines;

import java.util.ArrayList;

/**
 * Created by balavigneshr on 9/15/15.
 */
public class SelectRoutineActivity extends AppCompatActivity {

    ArrayList<SelectRoutines> routinesList = new ArrayList<>();
    LinearLayout no_routine;
    SwipeMenuListView listView;
    SelectRoutineAdapter itemsAdapter;
    SQLiteDatabase rdb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_activity);
        rdb = (new DatabaseHelper(this)).getReadableDatabase();
        SharedPreferences settings = this.getSharedPreferences("Routine", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        getRoutines();

        itemsAdapter = new SelectRoutineAdapter(this, routinesList);

        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                    long id) {
                                                SelectRoutines routine_selected = routinesList.get(position);
                                                Intent intent = new Intent(getBaseContext(), SelectWorkoutActivity.class);
                                                intent.putExtra("ROUTINE_NAME", routine_selected.getRoutine_name());
                                                intent.putExtra("COLOR", routine_selected.getColor());
                                                startActivity(intent);
                                            }
                                        }
        );
        if (routinesList.size() == 0) {
            TextView no_workout = (TextView) findViewById(R.id.no_routine);
            no_workout.setText("Please go to Workout Routine page to add routines!");
        } else {
            no_routine = (LinearLayout) findViewById(R.id.no_routine_text);
            no_routine.setVisibility(View.GONE);
        }
    }

    private void getRoutines() {
        String[] projection = {Tables.RoutineInformation.routineName, Tables.RoutineInformation.summary, Tables.RoutineInformation.color};
        Cursor c = rdb.rawQuery("SELECT " + projection[0] + ", " + projection[1] +  ", " + projection[2] + " from " + Tables.RoutineInformation.table_name + ";", null);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i ++) {
                routinesList.add(new SelectRoutines(c.getString(c.getColumnIndexOrThrow(projection[0])), c.getString(c.getColumnIndexOrThrow(projection[1])), c.getString(c.getColumnIndexOrThrow(projection[2]))));
                c.moveToNext();
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
