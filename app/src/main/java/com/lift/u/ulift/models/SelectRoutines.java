package com.lift.u.ulift.models;

/**
 * Created by balavigneshr on 9/21/15.
 */
public class SelectRoutines {
    private String routine_name;
    private String summary;
    private String color;

    public SelectRoutines(String routine_name, String summary, String color) {
        this.routine_name = routine_name;
        this.summary = summary;
        this.color = color;
    }

    public String getRoutine_name() {
        return routine_name;
    }

    public void setRoutine_name(String routine_name) {
        this.routine_name = routine_name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
