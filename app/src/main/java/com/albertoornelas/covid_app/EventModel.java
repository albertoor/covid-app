package com.albertoornelas.covid_app;

public class EventModel {

    // variables
    String id, name, place;
    String time, date;

    // Contrusctors
    public EventModel() {}
    public EventModel(String id, String name, String place,String time, String date) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.time = time;
        this.date = date;
    }

    // Getters And Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
