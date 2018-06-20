package com.marko.travelers.Model;

public class User {

    private String Name;
    private String Surname;
    private String userName;
    private String Password;
    private int Photo;
    private String eMail;
    private String Phone;


    public User(String name, String surname, String userName, String password, int photo, String eMail, String phone) {
        Name = name;
        Surname = surname;
        this.userName = userName;
        Password = password;
        Photo = photo;
        this.eMail = eMail;
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getPhoto() {
        return Photo;
    }

    public void setPhoto(int photo) {
        Photo = photo;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
