package com.lift.u.ulift.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lift.u.ulift.Adapters.ExerciseListAdapter;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.lift.u.ulift.models.Exercises;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by balavigneshr on 9/2/15.
 */
public class ExerciseListActivity extends AppCompatActivity {
    String routine_name;
    String workout_name;
    SharedPreferences pref;
    SwipeMenuListView listView;
    ArrayList<Exercises> exerciseList = new ArrayList<>();
    ExerciseListAdapter adapter;
    LinearLayout no_exercise_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_activity);
        pref = getSharedPreferences("Routine", Context.MODE_PRIVATE);
        workout_name = pref.getString("WORKOUT_NAME", "Workout");
        routine_name = pref.getString("ROUTINE_NAME", "New Routine");
        Log.e("PARSE","ROUTINE FROM WORKOUT - " + routine_name);
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        getSupportActionBar().setTitle(workout_name);
        getExerciseFromDB();
        no_exercise_layout = (LinearLayout) findViewById(R.id.no_exercise_text);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(R.color.error_red);
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("DELETE");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                swipeMenu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        new SweetAlertDialog(ExerciseListActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Won't be able to recover this!")
                                .setConfirmText("Yes,delete it.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        deleteItem(position);
                                        sweetAlertDialog
                                                .setTitleText("Deleted!")
                                                .showCancelButton(false)
                                                .setContentText("Your exercise has been deleted!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                })
                                .setCancelText("No,keep it.")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        if (exerciseList.size() != 0) {
            no_exercise_layout.setVisibility(View.GONE);
            adapter = new ExerciseListAdapter(this, exerciseList);
            listView.setAdapter(adapter);
        } else {
            no_exercise_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, MuscleActivity.class);
                startActivity(intent);
                return true;
            case R.id.save:
                Intent intent1 = new Intent(this, WorkoutActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getExerciseFromDB() {
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String[] values = {routine_name, workout_name};
        Cursor c = db.rawQuery("SELECT " + Tables.UserWorkout.ExerciseName + ", " + Tables.UserWorkout.Sets + ", " + Tables.UserWorkout.MajorMuscle
                + ", " + Tables.UserWorkout.Reps + ", " + Tables.UserWorkout.RestTimer + " from " + Tables.UserWorkout.table_name
                + " where " + Tables.UserWorkout.RoutineName + "=? and " + Tables.UserWorkout.WorkoutName + "=?", values);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String exercise_name = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.ExerciseName));
                String sets = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.Sets));
                String reps = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.Reps));
                String restTimer = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.RestTimer));
                String color = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.MajorMuscle));
                exerciseList.add(new Exercises(exercise_name, sets, reps, restTimer, color));
                c.moveToNext();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void deleteItem(int position) {
        SQLiteDatabase wdb = new DatabaseHelper(this).getWritableDatabase();
        String selection = Tables.UserWorkout.ExerciseName + "= ?";
        String[] selectionArgs = {exerciseList.get(position).getExerciseName()};
        wdb.delete(Tables.UserWorkout.table_name, selection, selectionArgs);
        exerciseList.remove(position);
        adapter.notifyDataSetChanged();
        if (exerciseList.size() == 0)
            no_exercise_layout.setVisibility(View.VISIBLE);
    }
}
