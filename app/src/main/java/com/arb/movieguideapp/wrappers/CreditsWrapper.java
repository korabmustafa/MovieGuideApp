package com.arb.movieguideapp.wrappers;

import com.arb.movieguideapp.models.Cast;
import com.arb.movieguideapp.models.Crew;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreditsWrapper {
    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private ArrayList<Cast> cast;
    @SerializedName("crew")
    private ArrayList<Crew> crew;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Crew> crew) {
        this.crew = crew;
    }
}
