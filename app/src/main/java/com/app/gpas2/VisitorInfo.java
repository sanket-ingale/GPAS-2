package com.app.gpas2;

public class VisitorInfo {
    private int id;
    private String Name;
    private String Address;
    private String Email;
    private String Contact;
    private String Vehicle;
    private String Org;
    private String intimDate;
    private String VDate;
    private String VTime;
    private String ConcernPerson;
    private String Purpose;
    private String Status;
    private String approvingAuth;
    private String startMeet;
    private String closeMeet;

    public VisitorInfo() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getEmail() {
        return Email;
    }

    public String getContact() {
        return Contact;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public String getOrg() {
        return Org;
    }

    public String getIntimDate() {
        return intimDate;
    }

    public String getVDate() {
        return VDate;
    }

    public String getVTime() {
        return VTime;
    }

    public String getConcernPerson() {
        return ConcernPerson;
    }

    public String getPurpose() {
        return Purpose;
    }

    public String getStatus() {
        return Status;
    }

    public String getApprovingAuth() {
        return approvingAuth;
    }

    public String getStartMeet() {
        return startMeet;
    }

    public String getCloseMeet() {
        return closeMeet;
    }

    public VisitorInfo(int id, String name, String address, String email, String contact, String vehicle, String org, String intimDate, String VDate, String VTime, String concernPerson, String purpose, String status, String approvingAuth, String startMeet, String closeMeet) {
        this.id = id;
        Name = name;
        Address = address;
        Email = email;
        Contact = contact;
        Vehicle = vehicle;
        Org = org;
        this.intimDate = intimDate;
        this.VDate = VDate;
        this.VTime = VTime;
        ConcernPerson = concernPerson;
        Purpose = purpose;
        Status = status;
        this.approvingAuth = approvingAuth;
        this.startMeet = startMeet;
        this.closeMeet = closeMeet;
    }
}
