package com.lift.u.ulift.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lift.u.ulift.Activities.Home.CurrentActivity;
import com.lift.u.ulift.Activities.Home.SelectRoutineActivity;
import com.lift.u.ulift.Adapters.DayArrayAdapter;
import com.lift.u.ulift.Adapters.HealthCardAdapter;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.FillLocalFromParse;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.lift.u.ulift.models.HealthCards;
import com.lift.u.ulift.models.WorkoutHistory;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import antistatic.spinnerwheel.AbstractWheel;

public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST = 0;
    Calendar calendar = Calendar.getInstance();
    DateFormat sdf = new SimpleDateFormat("EEE dd, MMM yyyy");
    TextView dateText;
    DatabaseHelper dbHelper;
    String workout;
    String routine;
    ArrayList<HealthCards> list = new ArrayList<>();
    HealthCardAdapter adapter;
    SharedPreferences activity;
    AbstractWheel day;
    ScaleInAnimationAdapter animationAdapter;
    float weights_moved_today = 0.0f;
    int sets_today = 0;
    String weight_max_today = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.workout);
        activity = getSharedPreferences("ROUTINE", Context.MODE_PRIVATE);
        if (activity != null) {
            routine = activity.getString("ROUTINE_NAME", null);
            workout = activity.getString("WORKOUT_NAME", null);
        }
        day = (AbstractWheel) findViewById(R.id.hour_horizontal);
        DayArrayAdapter dayAdapter = new DayArrayAdapter(this, calendar);
        day.setViewAdapter(dayAdapter);
        day.setCurrentItem(dayAdapter.getToday());
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("PARSE", "Date" + day.getCurrentItem());
            }
        });
        adapter = new HealthCardAdapter(this, list);
        final ListView cards = (ListView) findViewById(R.id.cards);
        animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(cards);
        cards.setAdapter(animationAdapter);
        cards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CurrentActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("CARDS", list);
                intent.putExtras(b);
                intent.putExtra("I", i);
                intent.putExtra("ROUTINE", routine);
                intent.putExtra("WORKOUT", workout);
                startActivity(intent);
            }
        });
        if (workout != null && !workout.isEmpty() && routine != null && !routine.isEmpty())
            getNumbersFromDb();

        if (ParseUser.getCurrentUser() != null)
            buildDrawer();
        checkUser();
//        dateText = (TextView) findViewById(R.id.date);
//        getCurrentDate();
//
//        Button bottom_up = (Button) findViewById(R.id.left);
//        bottom_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String dateToParse = dateText.getText().toString();
//                dateText = (TextView) findViewById(R.id.date);
//                try {
//                    Date myDate = sdf.parse(dateToParse);
//                    calendar.setTime(myDate);
//                    calendar.add(Calendar.DAY_OF_YEAR, -1);
//                    dateText.setText(sdf.format(calendar.getTime()));
//                } catch (Exception e) {
//                    Log.e("Previous Date Error", e.getMessage());
//                }
//            }
//        });
//
//        Button bottom_down = (Button) findViewById(R.id.right);
//        bottom_down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String dateToParse = dateText.getText().toString();
//                dateText = (TextView) findViewById(R.id.date);
//                try {
//                    Date myDate = sdf.parse(dateToParse);
//                    calendar.setTime(myDate);
//                    calendar.add(Calendar.DAY_OF_YEAR, 1);
//                    dateText.setText(sdf.format(calendar.getTime()));
//                } catch (Exception e) {
//                    Log.e("Next Date Error", e.getMessage());
//                }
//            }
//        });
    }

    private void getCurrentDate() {
        long date = System.currentTimeMillis();
        String dateString = sdf.format(date);
        dateText.setText(dateString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void checkUser() {
        if (ParseUser.getCurrentUser() == null) {
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), LOGIN_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LinearLayout homescreen = (LinearLayout) findViewById(R.id.homescreen);
        homescreen.setVisibility(View.GONE);
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                ProgressActivity progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
                progressActivity.showLoading();
                dbHelper = new DatabaseHelper(this);
                FillLocalFromParse.fromParseToLocal(dbHelper);
                progressActivity.showContent();
                homescreen.setVisibility(View.VISIBLE);
                buildDrawer();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_workout:
                Intent intent = new Intent(this, SelectRoutineActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_share_item:
                // Share
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNumbersFromDb() {
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String[] values = {routine, workout};
        Cursor c = db.rawQuery("SELECT " + Tables.UserWorkout.ExerciseName + ", " + Tables.UserWorkout.Sets + ", " + Tables.UserWorkout.MajorMuscle
                + ", " + Tables.UserWorkout.Reps + ", " + Tables.UserWorkout.RestTimer + ", " + Tables.UserWorkout.progress + " from " + Tables.UserWorkout.table_name
                + " where " + Tables.UserWorkout.RoutineName + "=? and " + Tables.UserWorkout.WorkoutName + "=?", values);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String exercise_name = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.ExerciseName));
                String sets = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.Sets));
                String color = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.MajorMuscle));
                int progress = c.getInt(c.getColumnIndexOrThrow(Tables.UserWorkout.progress));
                List<WorkoutHistory> setsComplete = new ArrayList<>();
                getSetsFromDB(exercise_name);
                list.add(new HealthCards(exercise_name, workout, Integer.parseInt(sets), "100", weights_moved_today, color, progress, sets_today));
                c.moveToNext();
            }
        }
        adapter.notifyDataSetChanged();
        animationAdapter.notifyDataSetChanged();
    }

    private void getSetsFromDB(String exercise_name) {
        weights_moved_today = 0.0f;
        sets_today = 0;
        weight_max_today = "";
        List<WorkoutHistory> setsComplete = new ArrayList<>();
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String[] values = {routine, exercise_name};
        Cursor c = db.rawQuery("SELECT " + Tables.UserSet.Weight + " from " + Tables.UserSet.table_name
                + " where " + Tables.UserSet.RoutineName + "=? and "+ Tables.UserSet.ExerciseName + "=?", values);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
//                String reps = c.getString(c.getColumnIndexOrThrow(Tables.UserSet.Reps));
//                String sets = c.getString(c.getColumnIndexOrThrow(Tables.UserSet.Sets));
                String weight = c.getString(c.getColumnIndexOrThrow(Tables.UserSet.Weight));
                weights_moved_today = weights_moved_today + Float.parseFloat(weight);
                sets_today = sets_today + 1;
                //setsComplete.add(new WorkoutHistory(Integer.parseInt(reps), Float.parseFloat(weight), Integer.parseInt(sets)));
                c.moveToNext();
            }
        }
    }

    public void buildDrawer() {
        final ResideMenu resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.orange);
        resideMenu.setShadowVisible(true);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.attachToActivity(this);
        String titles[] = { getResources().getString(R.string.home), getResources().getString(R.string.profile), getResources().getString(R.string.analyze),
                getResources().getString(R.string.routines), getResources().getString(R.string.settings) };
        int icon[] = { R.drawable.home, R.drawable.profile, R.drawable.analyze, R.drawable.routine, R.drawable.settings };
        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            final int j = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    switch(j) {
                        case 1:
                            intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            break;
                        case 3:
                            intent = new Intent(getApplicationContext(), RoutineActivity.class);
                            break;
                        case 4:
                            intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            break;
                    }
                    if (intent != null) {
                        resideMenu.closeMenu();
                        startActivity(intent);
                    }
                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT);
        }
        if (day != null)
            resideMenu.addIgnoredView(day);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

}