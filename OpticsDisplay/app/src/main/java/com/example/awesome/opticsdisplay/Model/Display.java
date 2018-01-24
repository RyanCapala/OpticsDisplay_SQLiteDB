package com.example.awesome.opticsdisplay.Model;

/**
 * Created by awesome on 1/17/18.
 */

public class Display {

    private String name;
    private String description;
    private String model;
    private String dateItemAdded;
    private String location;
    private int id;

    public Display() {
    }

    public Display(String name, String description, String model, String dateItemAdded, String location) {
        this.name = name;
        this.description = description;
        this.model = model;
        this.dateItemAdded = dateItemAdded;
        this.location = location;
    }

    public Display(String name, String description, String model, String dateItemAdded, String location, int id) {
        this.name = name;
        this.description = description;
        this.model = model;
        this.dateItemAdded = dateItemAdded;
        this.location = location;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
