package com.lift.u.ulift.DBObjects;

import android.provider.BaseColumns;

/**
 * Created by balavigneshr on 8/22/15.
 */
public class Tables {
    public static final String database_name = "uLift";

    public Tables() {}

    public static abstract class ExercisesData implements BaseColumns {
        public static final String objectId = "objectId";
        public static final String equipment= "Equipment";
        public static final String ExerciseName = "ExerciseName";
        public static final String ExpLevel = "ExpLevel";
        public static final String MajorMuscle = "MajorMuscle";
        public static final String Mechanics = "Mechanics";
        public static final String Muscle = "Muscle";
        public static final String Type = "Type";
        public static final String table_name = "exercises";
    }

    public static abstract class RoutineInformation implements BaseColumns {
        public static final String routineName = "RoutineName";
        public static final String summary = "summary";
        public static final String desc = "desc";
        public static final String goal = "goal";
        public static final String duration = "duration";
        public static final String color = "color";
        public static final String type = "type";
        public static final String level = "level";
        public static final String table_name = "routines";
    }

    public static abstract class UserWorkout implements BaseColumns {
        public static final String RoutineName = "RoutineName";
        public static final String ExerciseName = "ExerciseName";
        public static final String ExerciseNumber = "ExerciseNumber";
        public static final String MajorMuscle = "MajorMuscle";
        public static final String Muscle = "Muscle";
        public static final String Reps = "Reps";
        public static final String RestTimer = "RestTimer";
        public static final String Sets = "Sets";
        public static final String WorkoutName = "WorkoutName";
        public static final String UserId = "UserId";
        public static final String table_name = "UserWorkout";
    }
}
