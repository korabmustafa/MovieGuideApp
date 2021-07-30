package com.arb.movieguideapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String description;
    @SerializedName("genre_ids")
    private List<Long> genre;
    private List<Genre> genres;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("poster_path")
    private String thumbnail;
    @SerializedName("backdrop_path")
    private String coverImg;

    public Movie() { }

    public Movie(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Movie(int id, String title, String description, List<Long> genre,
                 String releaseDate, double voteAverage, String thumbnail, String coverImg) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.thumbnail = thumbnail;
        this.coverImg = coverImg;
        this.genres = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getGenre() {
        return genre;
    }

    public void setGenre(List<Long> genre) {
        this.genre = genre;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void mapGenres(List<Genre> genreList) {
        if (this.genres == null)
            this.genres = new ArrayList<>();

        for (Long l : genre) {
            for (Genre c : genreList) {
                if (l.equals(c.getId()))
                    this.genres.add(c);
            }
        }
    }
}
