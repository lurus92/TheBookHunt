package com.sampi.luigirusso.thebookhunt.entities;

import java.net.URL;

/**
 * Created by luigirusso on 16/03/16.
 */
public class Book {
    private int id;
    private long ISBN;
    private String title;
    private String author;
    private String description;
    private int image;           //As we're working locally, the image is coded with an int (R.drawable.<image>)
    private int year;
    private double longitude;     //TODO: coordinates must become an array of coordinates
    private double latitude;
    private URL thumbURL;

    public Book(int id, long ISBN, String title, String author, String description, int year, int image, Double latitude, Double longitude){
        this.id = id;
        this.ISBN = ISBN;
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
        this.year = year;
        //this.longitude = 0;
        if ((latitude==null)||(longitude==null)){
            this.setLatitude(Float.parseFloat(""+ Math.random()*10+""));          //RANDOM initialization for LAT/LON
            this.setLongitude(Float.parseFloat(""+ Math.random()*10+""));
        }else{
            this.setLongitude(longitude);
            this.setLatitude(latitude);
        }
        this.thumbURL = null;
    }

    //TODO: Please do a better job here
    public Book(int id, long ISBN, String title, String author, String description, int year, URL image, Double latitude, Double longitude){
        this.id = id;
        this.ISBN = ISBN;
        this.image = -1;
        this.title = title;
        this.author = author;
        this.description = description;
        this.year = year;
        //this.longitude = 0;
        if ((latitude==null)||(longitude==null)){
            this.setLatitude(Float.parseFloat(""+ Math.random()*10+""));          //RANDOM initialization for LAT/LON
            this.setLongitude(Float.parseFloat(""+ Math.random()*10+""));
        }else{
            this.setLongitude(longitude);
            this.setLatitude(latitude);
        }
        this.thumbURL = image;
    }

    //Constructor used for remote images


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPosition(){ return ""+ getLatitude() +","+ getLongitude();}

    public int getYear() {
        return year;
    }
    /* TODO: Check if other attributes are needed */

    public int getImage(){ return  image; }

    @Override
    public String toString() {
        return getTitle()+","+getAuthor()+","+getDescription()+","+getPosition(); //TODO: work with JSON/XML, add all attributes
    }

    public long getISBN() {
        return ISBN;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(double lat, double lon) {
        this.setLatitude(lat);
        this.setLongitude(lon);
    }

    public URL getThumb() {
        return thumbURL;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
