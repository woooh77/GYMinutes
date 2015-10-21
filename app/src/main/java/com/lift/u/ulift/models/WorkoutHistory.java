package com.lift.u.ulift.models;

/**
 * Created by balavigneshr on 10/5/15.
 */
public class WorkoutHistory {
    private int reps;
    private float weight;
    private int sets;

    public WorkoutHistory(int reps, float weight, int sets) {
        this.reps = reps;
        this.weight = weight;
        this.sets = sets;
    }

    @Override
    public String toString() {
        return "WorkoutHistory{" +
                "reps=" + reps +
                "weight=" + weight +
                ", sets=" + sets +
                '}';
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
}
