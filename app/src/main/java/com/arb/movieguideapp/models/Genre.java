package com.arb.movieguideapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Genre implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String genres;

    public Genre(String categories) {
        this.genres = categories;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String categories) {
        this.genres = categories;
    }
}
