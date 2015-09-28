package com.lift.u.ulift.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lift.u.ulift.Adapters.MuscleListAdapter;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by balavigneshr on 9/2/15.
 */
public class MuscleActivity extends AppCompatActivity {
    ListView listView;
    List<String> muscle_list = new ArrayList<>();
    int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle);
        height = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> listAdapter = getMuscle();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AddExerciseActivity.class);
                intent.putExtra("MAJOR_MUSCLE", muscle_list.get(i));
                startActivity(intent);
            }
        });
    }

    private ArrayAdapter<String> getMuscle() {
        SQLiteDatabase db = (new DatabaseHelper(this)).getReadableDatabase();
        String[] projection = {Tables.ExercisesData.MajorMuscle};
        String sortOrder = Tables.ExercisesData.MajorMuscle + " ASC";
        Cursor c = db.query(
                Tables.ExercisesData.table_name,          // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                projection[0],         // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            muscle_list.add(c.getString(c.getColumnIndexOrThrow(Tables.ExercisesData.MajorMuscle)));
            c.moveToNext();
        }
        MuscleListAdapter itemsAdapter = new MuscleListAdapter(MuscleActivity.this, muscle_list, height);
        return itemsAdapter;
    }
}
