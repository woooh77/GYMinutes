package com.lift.u.ulift.models;

/**
 * Created by balavigneshr on 9/10/15.
 */
public class Exercises {

    public String exerciseName;
    public int sets;
    public int reps;
    public int restTimer;
    public String color;

    public Exercises(String exerciseName, String sets, String reps, String restTimer, String color) {
        this.exerciseName = exerciseName;
        this.sets = Integer.parseInt(sets);
        this.reps = Integer.parseInt(reps);
        this.restTimer = Integer.parseInt(restTimer);
        this.color = color;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getRestTimer() {
        return restTimer;
    }

    public String getColor() {
        return color;
    }
}
