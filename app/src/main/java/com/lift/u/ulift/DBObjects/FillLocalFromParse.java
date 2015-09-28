package com.lift.u.ulift.DBObjects;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by balavigneshr on 8/22/15.
 */
public class FillLocalFromParse {

    public static void fromParseToLocal(DatabaseHelper dbHelper) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("PARSE","Filling up db from Parse.");
        // common exercises
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Exercises");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> exerciseList, ParseException e) {
                if (e == null) {
                    for (ParseObject p : exerciseList) {
                        String[] args = new String[]{p.getObjectId(), p.getString(Tables.ExercisesData.equipment), p.getString(Tables.ExercisesData.ExerciseName), p.getString(Tables.ExercisesData.ExpLevel),
                                p.getString(Tables.ExercisesData.MajorMuscle), p.getString(Tables.ExercisesData.Mechanics), p.getString(Tables.ExercisesData.Muscle), p.getString(Tables.ExercisesData.Type)};
                        db.execSQL(DBConstants.insert_exercise, args);
                    }
                    Log.d("PARSE", "Filled up local db with " + exerciseList.size());
                }
            }
        });

        // user-specific exercises
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("UserExercises");
        query1.whereEqualTo("UserId", ParseUser.getCurrentUser().getObjectId());
        Log.d("PARSE", "User Object Id is " + ParseUser.getCurrentUser().getObjectId());
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> exerciseList, ParseException e) {
                Log.d("PARSE", "Size of User specific exercises " + exerciseList.size());
                if (e == null) {
                    for (ParseObject p : exerciseList) {
                        String[] args = new String[]{p.getObjectId(), p.getString(Tables.ExercisesData.equipment), p.getString(Tables.ExercisesData.ExerciseName), p.getString(Tables.ExercisesData.ExpLevel),
                                p.getString(Tables.ExercisesData.MajorMuscle), p.getString(Tables.ExercisesData.Mechanics), p.getString(Tables.ExercisesData.Muscle), p.getString(Tables.ExercisesData.Type)};
                        db.execSQL(DBConstants.insert_exercise, args);
                    }
                }
            }
        });

        // Create local Routine table
        // Fill routines from Parse to local db;

    }
}
