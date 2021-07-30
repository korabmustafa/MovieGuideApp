package com.arb.movieguideapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Crew implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    @SerializedName("profile_path")
    private String thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getThumbnail() {
        return "https://image.tmdb.org/t/p/w185" + thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
