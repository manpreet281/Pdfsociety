package com.navjot.deepak.manpreet.pdfsociety.Models;

public class User {

    private String username;
    private String email;
    private String password;
    private int day_of_birth;
    private int month_of_birth;
    private int year_of_birth;
    private String gender;
    private String city;
    private String reg_token;

    public User() {}

    public User(String username, String email, String password, int day_of_birth, int month_of_birth, int year_of_birth, String gender, String city, String reg_token) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.day_of_birth = day_of_birth;
        this.month_of_birth = month_of_birth;
        this.year_of_birth = year_of_birth;
        this.gender = gender;
        this.city = city;
        this.reg_token = reg_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDay_of_birth() {
        return day_of_birth;
    }

    public void setDay_of_birth(int day_of_birth) {
        this.day_of_birth = day_of_birth;
    }

    public int getMonth_of_birth() {
        return month_of_birth;
    }

    public void setMonth_of_birth(int month_of_birth) {
        this.month_of_birth = month_of_birth;
    }

    public int getYear_of_birth() {
        return year_of_birth;
    }

    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReg_token() {
        return reg_token;
    }

    public void setReg_token(String reg_token) {
        this.reg_token = reg_token;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", day_of_birth=" + day_of_birth +
                ", month_of_birth=" + month_of_birth +
                ", year_of_birth=" + year_of_birth +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", reg_token='" + reg_token + '\'' +
                '}';
    }
}
