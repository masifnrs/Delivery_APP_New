package com.test.delivery_app_new.model;

public class Delivery {
    private String title, thumbnailUrl, address;
    private String latDetails;
    String longDetails;
    String urduDetail;

 
    public Delivery() {
    }
 
    public Delivery(String name, String thumbnailUrl,String latDetails) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.latDetails=latDetails;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public String getAddress() {
        return address;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }


    public void setLongDetails(String longDetails)
    {
        this.longDetails = longDetails;
    }
    public String getLongDetails()
    {
        return longDetails;
    }

    public void setLatDetails(String latDetails) {
        this.latDetails = latDetails;
    }
    public String getLatDetails()
    {
        return latDetails;
    }


 
}