package com.arb.movieguideapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cast implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("character")
    private String character;
    @SerializedName("profile_path")
    private String thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getThumbnail() {
        return "https://image.tmdb.org/t/p/w185" + thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
