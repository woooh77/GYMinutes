package com.lift.u.ulift.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by balavigneshr on 8/14/15.
 */
public class HealthCards implements Parcelable {

    public HealthCards(String exercise_name, String workout, int sets, String max, float weight_moved, String color, int progress, int sets_today) { //, List<WorkoutHistory> setsComplete) {
        this.exercise_name = exercise_name;
        this.workout = workout;
        this.sets = sets;
        this.max = max;
        this.weight_moved = weight_moved;
        this.color = color;
        this.progress = progress;
        this.sets_today = sets_today;
//        this.setsComplete = setsComplete;
    }
    public String exercise_name;
    public String workout;
    public int sets;
    public String max;
    public float weight_moved;
    public String color;
    public int progress;
    public int sets_today;
//    public List<WorkoutHistory> setsComplete;

    public int getSets_today() {
        return sets_today;
    }

    public void setSets_today(int sets_today) {
        this.sets_today = sets_today;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

//    public List<WorkoutHistory> getSetsComplete() {
//        return setsComplete;
//    }
//
//    public void setSetsComplete(List<WorkoutHistory> setsComplete) {
//        this.setsComplete = setsComplete;
//    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

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

    public float getWeight_moved() {
        return weight_moved;
    }

    public void setWeight_moved(float weight_moved) {
        this.weight_moved = weight_moved;
    }

    @Override
    public String toString() {
        return "HealthCards{" +
                "workout='" + workout + '\'' +
                ", sets=" + sets +
                ", max='" + max + '\'' +
                ", weight_moved=" + weight_moved +
                ", color='" + color + '\'' +
                ", progress=" + progress +
                ", sets_today =" + sets_today +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(exercise_name);
        parcel.writeString(workout);
        parcel.writeInt(sets);
        parcel.writeString(max);
        parcel.writeFloat(weight_moved);
        parcel.writeString(color);
        parcel.writeInt(progress);
        parcel.writeInt(sets_today);
//        parcel.writeList(setsComplete);
    }

    public HealthCards(Parcel in) {
        exercise_name = in.readString();
        workout = in.readString();
        sets = in.readInt();
        max = in.readString();
        weight_moved = in.readFloat();
        color = in.readString();
        progress = in.readInt();
        sets_today = in.readInt();
//        setsComplete = new ArrayList<>();
//        in.readList(setsComplete, null);
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
