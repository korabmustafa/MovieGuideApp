package com.arb.movieguideapp.wrappers;

import com.arb.movieguideapp.models.Genre;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GenreWrapper {
    @SerializedName("genres")
    private ArrayList<Genre> genre;

    public ArrayList<Genre> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Genre> genre) {
        this.genre = genre;
    }
}
