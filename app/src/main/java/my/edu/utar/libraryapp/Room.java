package my.edu.utar.libraryapp;

import java.util.Date;

import com.google.firebase.database.Exclude;

public class Room {

    @Exclude
    public String key;
    public int room_no;
    public String date;
    public int time_slot;
    public boolean status;

    public Room(){}

    public Room(int room_no, String date, int time_slot, boolean status) {
        this.room_no = room_no;
        this.date = date;
        this.time_slot = time_slot;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getRoom_no() {
        return room_no;
    }

    public void setRoom_no(int room_no) {
        this.room_no = room_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(int time_slot) {
        this.time_slot = time_slot;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
