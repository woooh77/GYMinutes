package com.lift.u.ulift.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by balavigneshr on 8/14/15.
 */
public class HealthCards implements Parcelable {

    public HealthCards(String workout, int sets, String max, int weight_moved, String color) {
        this.workout = workout;
        this.sets = sets;
        this.max = max;
        this.weight_moved = weight_moved;
        this.color = color;
    }

    public String workout;
    public int sets;
    public String max;
    public int weight_moved;
    public String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(workout);
        parcel.writeInt(sets);
        parcel.writeString(max);
        parcel.writeInt(weight_moved);
        parcel.writeString(color);
    }

    public HealthCards(Parcel in) {
        workout = in.readString();
        sets = in.readInt();
        max = in.readString();
        weight_moved = in.readInt();
        color = in.readString();
    }

    public static final Parcelable.Creator<HealthCards> CREATOR = new Parcelable.Creator<HealthCards>() {
        public HealthCards createFromParcel(Parcel in) {
            return new HealthCards(in);
        }

        public HealthCards[] newArray(int size) {
            return new HealthCards[size];
        }
    };
}
