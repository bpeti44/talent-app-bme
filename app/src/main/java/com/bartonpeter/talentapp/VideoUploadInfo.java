package com.bartonpeter.talentapp;

/**
 * Created by petib on 2018. 03. 09..
 */

public class VideoUploadInfo {

    public String username;
    public String videoURL;

    public VideoUploadInfo(){

    }

    public VideoUploadInfo(String name, String url) {

        this.username = name;
        this.videoURL= url;
    }

    public String getUsername() {
        return username;
    }

    public String getVideoURL() {
        return videoURL;
    }
}
