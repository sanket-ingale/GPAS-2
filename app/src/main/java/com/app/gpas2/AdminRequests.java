package com.app.gpas2;

public class AdminRequests {
    String email, password, status;

    public AdminRequests(String email,String password,String status){
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
