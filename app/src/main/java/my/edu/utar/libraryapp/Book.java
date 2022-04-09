package my.edu.utar.libraryapp;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Book implements Serializable {

    private String title, author, ISBN;
    private int year, pages;
    private boolean status;
    private String image;
    @Exclude
    private String key;

    public Book() {}

    public Book(String title, String author, int year, String ISBN, String image,int pages, Boolean status){
        this.title = title;
        this.author = author;
        this.year = year;
        this.ISBN = ISBN;
        this.image = image;
        this.pages = pages;
        this.status = status;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
