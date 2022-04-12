package my.edu.utar.libraryapp;

import com.google.firebase.database.Exclude;

public class Announcement {
    @Exclude
    String key;

    private String date;
    private String title;
    private String description;

    public Announcement(){

    }

    public Announcement(String date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
