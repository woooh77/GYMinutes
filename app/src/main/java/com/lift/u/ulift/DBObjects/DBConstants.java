package com.lift.u.ulift.DBObjects;

/**
 * Created by balavigneshr on 8/24/15.
 */
public class DBConstants {
    public final static String create_excercises = "CREATE TABLE " + Tables.ExercisesData.table_name + "(" + Tables.ExercisesData.objectId + " TEXT, "
            + Tables.ExercisesData.equipment + " TEXT, " + Tables.ExercisesData.ExerciseName + " TEXT, " + Tables.ExercisesData.ExpLevel + " TEXT, "
            + Tables.ExercisesData.MajorMuscle + " TEXT, " + Tables.ExercisesData.Mechanics + " TEXT, " + Tables.ExercisesData.Muscle + " TEXT, "
            + Tables.ExercisesData.Type + " TEXT);";
    public final static String insert_exercise = "INSERT INTO " + Tables.ExercisesData.table_name + "(" +  Tables.ExercisesData.objectId + ", "
            + Tables.ExercisesData.equipment + ", " + Tables.ExercisesData.ExerciseName + ", " + Tables.ExercisesData.ExpLevel + ", "
            + Tables.ExercisesData.MajorMuscle + ", " + Tables.ExercisesData.Mechanics + ", " + Tables.ExercisesData.Muscle + ", "
            + Tables.ExercisesData.Type + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    public final static String create_routines = "CREATE TABLE " + Tables.RoutineInformation.table_name + "("
            + Tables.RoutineInformation.routineName + " TEXT, " + Tables.RoutineInformation.summary + " TEXT, "
            + Tables.RoutineInformation.desc + " TEXT, " + Tables.RoutineInformation.goal + " TEXT, " + Tables.RoutineInformation.duration + " TEXT, "
            + Tables.RoutineInformation.color + " TEXT, " + Tables.RoutineInformation.type + " TEXT, " + Tables.RoutineInformation.level + " TEXT);";
    public final static String insert_routine = "INSERT INTO " + Tables.RoutineInformation.table_name + "("
            + Tables.RoutineInformation.routineName + ", " + Tables.RoutineInformation.summary + ", "
            + Tables.RoutineInformation.desc + ", " + Tables.RoutineInformation.goal + ", " + Tables.RoutineInformation.duration + ", "
            + Tables.RoutineInformation.color + ", " + Tables.RoutineInformation.type + ", " + Tables.RoutineInformation.level + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    public final static String create_userWorkout = "CREATE TABLE " + Tables.UserWorkout.table_name + "("
            + Tables.UserWorkout.RoutineName + " TEXT, " + Tables.UserWorkout.ExerciseName + " TEXT, "
            + Tables.UserWorkout.ExerciseNumber + " TEXT, " + Tables.UserWorkout.MajorMuscle + " TEXT, " + Tables.UserWorkout.Muscle + " TEXT, "
            + Tables.UserWorkout.Reps + " TEXT, " + Tables.UserWorkout.RestTimer + " TEXT, " + Tables.UserWorkout.Sets + " TEXT, "
            + Tables.UserWorkout.WorkoutName + " TEXT, "+ Tables.UserWorkout.progress + " TEXT, " + Tables.UserWorkout.UserId + " TEXT);";
    public final static String insert_userWorkout = "INSERT INTO " + Tables.UserWorkout.table_name + "("
            + Tables.UserWorkout.RoutineName + ", " + Tables.UserWorkout.ExerciseName + ", "
            + Tables.UserWorkout.ExerciseNumber + ", " + Tables.UserWorkout.MajorMuscle + ", " + Tables.UserWorkout.Muscle + ", "
            + Tables.UserWorkout.Reps + ", " + Tables.UserWorkout.RestTimer + ", " + Tables.UserWorkout.Sets + ", "
            + Tables.UserWorkout.WorkoutName + ", " + Tables.UserWorkout.progress + ", " + Tables.UserWorkout.UserId + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public final static String create_userSet = "CREATE TABLE " + Tables.UserSet.table_name + "("
            + Tables.UserSet.Date + " TEXT, " + Tables.UserSet.ExerciseName + " TEXT, " + Tables.UserSet.Sets + " TEXT, "
            + Tables.UserSet.ExerciseNumber + " TEXT, " + Tables.UserSet.MajorMuscle + " TEXT, " + Tables.UserSet.Muscle + " TEXT, "
            + Tables.UserSet.Reps + " TEXT, " + Tables.UserSet.RoutineName + " TEXT, " + Tables.UserSet.TimeStamp + " TEXT, " + Tables.UserSet.Weight + " TEXT, "
            + Tables.UserSet.WorkoutName + " TEXT, " + Tables.UserSet.UserId + " TEXT);";
    public final static String insert_userSet = "INSERT INTO " + Tables.UserSet.table_name + "("
            + Tables.UserSet.Date + ", " + Tables.UserSet.ExerciseName + ", " + Tables.UserSet.Sets + ", "
            + Tables.UserSet.ExerciseNumber + ", " + Tables.UserSet.MajorMuscle + ", " + Tables.UserSet.Muscle + ", "
            + Tables.UserSet.Reps + ", " + Tables.UserSet.RoutineName + ", " + Tables.UserSet.TimeStamp + ", " + Tables.UserSet.Weight + ", "
            + Tables.UserSet.WorkoutName + ", " + Tables.UserSet.UserId + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    public final static String get_count(String column, String table, String value) {
        return "SELECT * FROM " + table + " WHERE " + column + "=\'" + value + "\';";
    }
}
