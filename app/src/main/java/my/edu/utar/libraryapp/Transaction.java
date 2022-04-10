package my.edu.utar.libraryapp;

import com.google.firebase.database.Exclude;

public class Transaction {
    @Exclude
    String key;

    private String ISBN;
    private String student_ID;
    private String borrow_date, due_date;
    private String status;
    private String userUID;

    public Transaction() { }

    public Transaction(String ISBN, String student_ID, String borrow_date, String due_date, String status, String userUID) {
        this.ISBN = ISBN;
        this.student_ID = student_ID;
        this.borrow_date = borrow_date;
        this.due_date = due_date;
        this.status = status;
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getStudent_ID() {
        return student_ID;
    }

    public void setStudent_ID(String student_ID) {
        this.student_ID = student_ID;
    }

    public String getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(String borrow_date) {
        this.borrow_date = borrow_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
