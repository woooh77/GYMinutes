package com.lift.u.ulift.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.karim.MaterialTabs;

/**
 * Created by balavigneshr on 9/3/15.
 */
public class AddExerciseActivity extends AppCompatActivity {
    private String major_muscle, workout_name, routine_name;
    LinkedHashMap<String, LinkedHashMap<String, List<String>>> dbValues = new LinkedHashMap<>();
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getSharedPreferences("Routine", Context.MODE_PRIVATE);
        workout_name = pref.getString("WORKOUT_NAME", "Workout");
        routine_name = pref.getString("ROUTINE_NAME", "Routine");
        major_muscle = getIntent().getStringExtra("MAJOR_MUSCLE");
        getSupportActionBar().setTitle(major_muscle);
        getEquipment();
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));

        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(pager);
    }

    private void getEquipment() {
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        String[] values = {major_muscle};
        Cursor c = db.rawQuery("SELECT " + Tables.ExercisesData.equipment + ", " + Tables.ExercisesData.Muscle
                + ", " + Tables.ExercisesData.ExerciseName + " from " + Tables.ExercisesData.table_name
                + " where " + Tables.ExercisesData.MajorMuscle + "=?", values);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String equip = c.getString(c.getColumnIndexOrThrow(Tables.ExercisesData.equipment));
                String muscle = c.getString(c.getColumnIndexOrThrow(Tables.ExercisesData.Muscle));
                String exercise = c.getString(c.getColumnIndexOrThrow(Tables.ExercisesData.ExerciseName));
                LinkedHashMap<String, List<String>> eMap = new LinkedHashMap<>();
                List<String> entry = new ArrayList<>();
                if (dbValues.containsKey(muscle)) {
                    eMap = dbValues.get(muscle);
                    if (eMap.containsKey(equip))
                        entry = eMap.get(equip);
                }
                entry.add(exercise);
                eMap.put(equip, entry);
                dbValues.put(muscle, eMap);
                c.moveToNext();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_only, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<String> mTitles = new ArrayList<>();
        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
            mTitles.addAll(dbValues.keySet());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<String> headers = new ArrayList<>();
            String tab_selected = mTitles.get(position);
            LinkedHashMap<String, List<String>> eMap = dbValues.get(tab_selected);
            headers.addAll(eMap.keySet());
            return SampleFragment.newInstance(headers, eMap, major_muscle, workout_name, routine_name, tab_selected);
        }
    }
}
