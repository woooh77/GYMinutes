package com.lift.u.ulift.models;

/**
 * Created by balavigneshr on 10/5/15.
 */
public class WorkoutHistory {
    private float weight;
    private int sets;
    private int seat;
    private int hand;
    private int timer;

    public WorkoutHistory(float weight, int sets, int seat, int hand, int timer) {
        this.weight = weight;
        this.sets = sets;
        this.seat = seat;
        this.hand = hand;
        this.timer = timer;
    }

    @Override
    public String toString() {
        return "WorkoutHistory{" +
                "weight=" + weight +
                ", sets=" + sets +
                ", seat=" + seat +
                ", hand=" + hand +
                ", timer=" + timer +
                '}';
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

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getHand() {
        return hand;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
