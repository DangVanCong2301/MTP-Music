package com.example.spotifyapp.models;

public class Student {
    private int id;
    private String name;
    private String birthday;
    private String sex;
    private String email;
    private String address;
    private double average;

    public Student() {
    }

    public Student(int id, String name, String birthday, String sex, String email, String address, double average) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.email = email;
        this.address = address;
        this.average = average;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
