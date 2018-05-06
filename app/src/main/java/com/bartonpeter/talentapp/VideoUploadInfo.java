package com.bartonpeter.talentapp;

/**
 * Created by petib on 2018. 03. 09..
 */

public class VideoUploadInfo {

    public String username;
    public String videoURL;
    public double rating;

    public String getSeason() {
        return season;
    }

    public String season;

    public VideoUploadInfo(){

    }

//    public VideoUploadInfo(String name, String url) {
//
//        this.username = name;
//        this.videoURL= url;
//    }


    public VideoUploadInfo(String name, String url, String season) {

        this.username = name;
        this.videoURL= url;
        this.season = season;
    }

    public String getUsername() {
        return username;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
