package com.example.firebasedemo;

public class UserHelperClass {
    String email, uname, password, phone;

    public UserHelperClass(String email, String uname, String password, String phone) {
        this.email = email;
        this.uname = uname;
        this.password = password;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUname() {
        return uname;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
