package com.lift.u.ulift.Activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.lift.u.ulift.Adapters.ExpandableListAdapter;
import com.lift.u.ulift.DBObjects.DatabaseHelper;
import com.lift.u.ulift.DBObjects.Tables;
import com.lift.u.ulift.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by balavigneshr on 9/9/15.
 */
public class SampleFragment extends Fragment implements ExpandableListView.OnChildClickListener{

    @InjectView(R.id.explist)
    ExpandableListView expandableListView;
    ExpandableListAdapter listAdapter;
    String major_muscle;
    String workout_name;
    String routine_name;
    String muscle;


    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public void setRoutine_name(String routine_name) {
        this.routine_name = routine_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }

    public void setMajor_muscle(String major_muscle) {
        this.major_muscle = major_muscle;
    }

    public void setListDataHeader(ArrayList<String> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    public void setListDataChild(HashMap<String, List<String>> listDataChild) {
        this.listDataChild = listDataChild;
    }

    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public static SampleFragment newInstance(ArrayList<String> equipments, HashMap<String, List<String>> equipment_map, String major_muscle,
                                             String workout_name, String routine_name, String muscle) {
        SampleFragment f = new SampleFragment();
        f.setListDataHeader(equipments);
        f.setListDataChild(equipment_map);
        f.setMajor_muscle(major_muscle);
        f.setWorkout_name(workout_name);
        f.setRoutine_name(routine_name);
        f.setMuscle(muscle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        listAdapter = new ExpandableListAdapter(rootView.getContext(), listDataHeader, listDataChild, major_muscle);
        expandableListView.setAdapter(listAdapter);
        expandableListView.setOnChildClickListener(this);
        return rootView;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String equipment = listDataHeader.get(groupPosition);
        String exerciseName = listDataChild.get(equipment).get(childPosition);
        Toast.makeText(v.getContext(), "Exercise Added\n" + exerciseName, Toast.LENGTH_SHORT).show();
        storeRoutine(exerciseName);
        return true;
    }

    private void storeRoutine(String exercise_name) {
        SQLiteDatabase db = (new DatabaseHelper(getContext())).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tables.UserWorkout.RoutineName, routine_name);
        values.put(Tables.UserWorkout.ExerciseName, exercise_name);
        values.put(Tables.UserWorkout.MajorMuscle, major_muscle);
        values.put(Tables.UserWorkout.Muscle, muscle);
        values.put(Tables.UserWorkout.Reps, "10");
        values.put(Tables.UserWorkout.RestTimer, "45");
        values.put(Tables.UserWorkout.Sets, "3");
        values.put(Tables.UserWorkout.WorkoutName, workout_name);
        values.put(Tables.UserWorkout.UserId, ParseUser.getCurrentUser().getObjectId());

        db.insert(
                Tables.UserWorkout.table_name,
                null,
                values);
    }
}