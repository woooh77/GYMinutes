package com.lift.u.ulift.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lift.u.ulift.Activities.Home.SelectRoutineActivity;
import com.lift.u.ulift.Adapters.HealthCardAdapter;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.FillLocalFromParse;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.lift.u.ulift.models.HealthCards;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST = 0;
    Calendar calendar = Calendar.getInstance();
    DateFormat sdf = new SimpleDateFormat("EEE dd, MMM yyyy");
    TextView dateText;
    SlidingPaneLayout workoutSummary;
    DatabaseHelper dbHelper;
    String workout;
    String routine;
    ArrayList<HealthCards> list = new ArrayList<>();
    HealthCardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.workout);
        workout = getIntent().getStringExtra("WORKOUT");
        routine = getIntent().getStringExtra("ROUTINE");

        adapter = new HealthCardAdapter(this, list);
        ListView cards = (ListView) findViewById(R.id.cards);
        cards.setAdapter(adapter);

        if (workout != null && !workout.isEmpty() && routine != null && !routine.isEmpty())
            getNumbersFromDb();

        if (ParseUser.getCurrentUser() != null)
            buildDrawer();
        checkUser();
        workoutSummary = (SlidingPaneLayout) findViewById(R.id.workout_summary);
        workoutSummary.setVisibility(View.GONE);
        dateText = (TextView) findViewById(R.id.date);
        getCurrentDate();

        Button previous = (Button) findViewById(R.id.left);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateToParse = dateText.getText().toString();
                dateText = (TextView) findViewById(R.id.date);
                try {
                    Date myDate = sdf.parse(dateToParse);
                    calendar.setTime(myDate);
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    dateText.setText(sdf.format(calendar.getTime()));
                } catch (Exception e) {
                    Log.e("Previous Date Error", e.getMessage());
                }
            }
        });

        Button next = (Button) findViewById(R.id.right);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateToParse = dateText.getText().toString();
                dateText = (TextView) findViewById(R.id.date);
                try {
                    Date myDate = sdf.parse(dateToParse);
                    calendar.setTime(myDate);
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    dateText.setText(sdf.format(calendar.getTime()));
                } catch (Exception e) {
                    Log.e("Next Date Error", e.getMessage());
                }
            }
        });
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
            Log.d("PARSE", "User not logged in");
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), LOGIN_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("PARSE", "Returned activity with result code " + resultCode);
        LinearLayout homescreen = (LinearLayout) findViewById(R.id.homescreen);
        homescreen.setVisibility(View.GONE);
        Log.d("PARSE", "1");
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                ProgressActivity progressActivity = (ProgressActivity) findViewById(R.id.progressActivity);
                progressActivity.showLoading();
                dbHelper = new DatabaseHelper(this);
                Log.d("PARSE","2");
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
            case R.id.workout_summary:
                workoutSummary.setVisibility((workoutSummary.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNumbersFromDb() {
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String[] values = {routine, workout};
        Cursor c = db.rawQuery("SELECT " + Tables.UserWorkout.ExerciseName + ", " + Tables.UserWorkout.Sets + ", " + Tables.UserWorkout.MajorMuscle
                + ", " + Tables.UserWorkout.Reps + ", " + Tables.UserWorkout.RestTimer + " from " + Tables.UserWorkout.table_name
                + " where " + Tables.UserWorkout.RoutineName + "=? and " + Tables.UserWorkout.WorkoutName + "=?", values);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String exercise_name = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.ExerciseName));
                String sets = c.getString(c.getColumnIndexOrThrow(Tables.UserWorkout.Sets));
                list.add(new HealthCards(exercise_name, Integer.parseInt(sets), "100", 100));
                c.moveToNext();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void buildDrawer() {
        final ResideMenu resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.orange);
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
                    }
                    if (intent != null) {
                        resideMenu.closeMenu();
                        startActivity(intent);
                    }
                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT);
        }
    }
}