package com.app.gpas2;

public class UserDetails {
    private int id;
    private String Name;
    private String Department;
    private String Designation;
    private String Email;
    private String password;

    public UserDetails() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getDepartment() {
        return Department;
    }

    public String getDesignation() {
        return Designation;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return password;
    }

    public UserDetails(int id, String name, String department, String designation, String email, String password) {
        this.id = id;
        Name = name;
        Department = department;
        Designation = designation;
        Email = email;
        this.password = password;
    }

}
