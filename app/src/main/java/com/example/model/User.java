package com.example.model;

public class User {
    private String uid; // ID duy nhất cho mỗi người dùng
    private String email;
    private String name;

    // Constructor không tham số cần thiết cho Firestore
    public User() {}

    public User(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    // Getter và Setter cho UID
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Getter và Setter cho Email và Name
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
