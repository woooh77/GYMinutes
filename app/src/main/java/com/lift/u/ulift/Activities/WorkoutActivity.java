package com.lift.u.ulift.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by balavigneshr on 9/2/15.
 */
public class WorkoutActivity extends AppCompatActivity {
    SwipeMenuListView listView;
    String routine_name, color;
    SharedPreferences pref;
    ArrayList<String> workouts = new ArrayList<>();
    String workout = "";
    ArrayAdapter<String> adapter;
    LinearLayout no_workout;
    ScaleInAnimationAdapter animationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        pref = getSharedPreferences("Routine", Context.MODE_PRIVATE);
        routine_name = pref.getString("ROUTINE_NAME", "New Routine");
        color = pref.getString("COLOR", "Red");
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        getWorkoutsFromDB();

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
                        new SweetAlertDialog(WorkoutActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                                .setContentText("Your workout has been deleted!")
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

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, workouts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                if (workouts.size() != 0) {
                    Resources resources = WorkoutActivity.this.getResources();
                    if (!color.isEmpty()) {
                        textView.setBackgroundColor(resources.getColor(resources.getIdentifier(color, "color", getApplicationContext().getPackageName())));
                        textView.setTextColor(resources.getColor(R.color.white));
                    }
                    textView.setHeight(150);
                }
                return textView;
            }
        };
        animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                SharedPreferences pref = getSharedPreferences("Routine", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("WORKOUT_NAME", workouts.get(position));
                edit.putString("COLOR", color);
                edit.commit();
                Intent intent = new Intent(getBaseContext(), ExerciseListActivity.class);
                startActivity(intent);
            }
        });

        if (workouts.size() == 0)
            showInputDialog();
        else {
            no_workout = (LinearLayout) findViewById(R.id.no_routine_text);
            no_workout.setVisibility(View.GONE);
        }
        getSupportActionBar().setTitle(routine_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_only, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                showInputDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void showInputDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext= new EditText(getApplicationContext());
        edittext.setTextColor(getResources().getColor(R.color.violet));
        edittext.setHint("Enter Workout Name");
        edittext.setHintTextColor(getResources().getColor(R.color.orange));
        alert.setView(edittext, 40, 70, 40, 0);
        alert.setCancelable(true);
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                workout = edittext.getText().toString().trim();
                dialog.dismiss();
                String error_message = "";
                if (workout.isEmpty())
                    error_message = "No Workout specified.";
                else if (workouts.contains(workout))
                    error_message = "Workout already exists.";
                else {
                    Intent intent = new Intent(WorkoutActivity.this, ExerciseListActivity.class);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("WORKOUT_NAME", workout);
                    edit.commit();
                    startActivity(intent);
                }
                if (!error_message.isEmpty()) {
                    new SweetAlertDialog(WorkoutActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(error_message)
                            .show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void deleteItem(int position) {
        SQLiteDatabase wdb = new DatabaseHelper(this).getWritableDatabase();
        String selection = Tables.UserWorkout.WorkoutName + "= ?";
        String[] selectionArgs = {workouts.get(position)};
        wdb.delete(Tables.UserWorkout.table_name, selection, selectionArgs);
        workouts.remove(position);
        adapter.notifyDataSetChanged();
        animationAdapter.notifyDataSetChanged();
        if (workouts.size() == 0)
            no_workout.setVisibility(View.VISIBLE);
    }
}
