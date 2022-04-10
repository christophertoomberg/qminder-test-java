package com.example.demo.model;

public class Venue {
    private final String id;
    private final String name;
    private final String location;
    private String hamburgerPictureUrl;

    public Venue(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String getHamburgerPictureUrl() {
        return hamburgerPictureUrl;
    }

    public void setHamburgerPictureUrl(String hamburgerPictureUrl) {
        this.hamburgerPictureUrl = hamburgerPictureUrl;
    }
}
