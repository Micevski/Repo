package com.marko.travelers.Model;

import java.util.List;

public class Travel {

    private User Driver;
    private String Ftom;
    private String To;
    private String Date;
    private String Time;
    private String FreeSeats;
    private float Price;
    private String Valute;
    private List<User> Passengers;


    public Travel(User driver, String ftom, String to, String date, String time, String freeSeats, float price, String valute) {
        Driver = driver;
        Ftom = ftom;
        To = to;
        Date = date;
        Time = time;
        FreeSeats = freeSeats;
        Price = price;
        Valute = valute;

    }

    public User getDriver() {
        return Driver;
    }

    public void setDriver(User driver) {
        Driver = driver;
    }

    public String getFtom() {
        return Ftom;
    }

    public void setFtom(String ftom) {
        Ftom = ftom;
    }

    public String get_To() {
        return To;
    }

    public void set_To(String to) {
        To = to;
    }

    public String getFreeSeats() {
        return FreeSeats;
    }

    public void setFreeSeats(String freeSeats) {
        FreeSeats = freeSeats;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getValute() {
        return Valute;
    }

    public void setValute(String valute) {
        Valute = valute;
    }

    public List <User> getPassengers() {
        return Passengers;
    }

    public void setPassengers(List <User> passengers) {
        Passengers = passengers;
    }

    public String get_Date() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}

