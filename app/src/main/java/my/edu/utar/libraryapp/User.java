package my.edu.utar.libraryapp;

import com.google.firebase.database.Exclude;

public class User {
    @Exclude
    public String key;

    public String studentID;
    public String name;
    public String email;
    public String img;
    public String identityNo;
    public boolean student;

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public String getFirebaseUID() {
        return firebaseUID;
    }

    public void setFirebaseUID(String firebaseUID) {
        this.firebaseUID = firebaseUID;
    }

    public String firebaseUID;

    public User(){

    }

    public User(String studentID, String name, String email, String img, String identityNo, boolean student, String firebaseUID) {
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.img = img;
        this.identityNo = identityNo;
        this.student = student;
        this.firebaseUID = firebaseUID;
    }

//    public User(String studentID, String name, String email, String img, boolean student, String firebaseUID) {
//        this.studentID = studentID;
//        this.name = name;
//        this.email = email;
//        this.img = img;
//        this.student = student;
//        this.firebaseUID = firebaseUID;
//    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
