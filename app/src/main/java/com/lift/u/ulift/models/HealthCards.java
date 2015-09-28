package com.lift.u.ulift.models;

/**
 * Created by balavigneshr on 8/14/15.
 */
public class HealthCards {

    public HealthCards(String workout, int sets, String max, int weight_moved) {
        this.workout = workout;
        this.sets = sets;
        this.max = max;
        this.weight_moved = weight_moved;
    }

    public String workout;
    public int sets;
    public String max;
    public int weight_moved;

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public int getWeight_moved() {
        return weight_moved;
    }

    public void setWeight_moved(int weight_moved) {
        this.weight_moved = weight_moved;
    }

    @Override
    public String toString() {
        return "HealthCards{" +
                "workout='" + workout + '\'' +
                ", sets=" + sets +
                ", max=" + max +
                ", weight_moved=" + weight_moved +
                '}';
    }
}
