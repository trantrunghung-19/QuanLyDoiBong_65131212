package vn.edu.tinhoc123.quanlydoibong.models;

import com.google.firebase.Timestamp;

public class User {
    private String uid;
    private String hoTen;
    private String email;
    private String role;
    private Timestamp createdAt;

    public User() {}

    public User(String uid, String hoTen, String email, String role) {
        this.uid = uid;
        this.hoTen = hoTen;
        this.email = email;
        this.role = role;
        this.createdAt = Timestamp.now();
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}