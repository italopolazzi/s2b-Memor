package com.dev.mendes.android_mytasks.dataBase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tainaviriato on 21/06/17.
 */

public class Task implements Parcelable {

    private String taskName;
    private String id;
    private String taskPlace;
    private String taskCoordinates;
    private String taskDate;
    private String taskNote;
    private boolean isChecked;

    public String getTaskCoordinates() {
        return taskCoordinates;
    }

    public Task(String id, String taskName, String taskPlace, String taskCoordinates, String taskDate, String taskNote, boolean isChecked) {
        this.id = id;
        this.taskName = taskName;
        this.taskPlace = taskPlace;
        this.taskCoordinates = taskCoordinates;

        this.taskDate = taskDate;
        this.taskNote = taskNote;
        this.isChecked = isChecked;
    }

    public Task() {

    }

    public String getTaskName() {
        return taskName;
    }

    public String getId() {
        return id;
    }

    public String getTaskPlace() {
        return taskPlace;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTaskPlace(String taskPlace) {
        this.taskPlace = taskPlace;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    protected Task(Parcel in) {
        taskName = in.readString();
        id = in.readString();
        taskPlace = in.readString();
        taskDate = in.readString();
        taskNote = in.readString();
        isChecked = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskName);
        dest.writeString(id);
        dest.writeString(taskPlace);
        dest.writeString(taskDate);
        dest.writeString(taskNote);
        dest.writeByte((byte) (isChecked ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public void setCoordinates(String coordinates) {
        this.taskCoordinates = coordinates;
    }
}