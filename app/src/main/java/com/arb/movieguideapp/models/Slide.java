package com.arb.movieguideapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Slide implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String description;
    @SerializedName("backdrop_path")
    private String thumbnail;
    @SerializedName("genre_ids")
    private List<Long> genre;
    private List<Genre> genres;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("poster_path")
    private String poster;

    public Slide(int id, String title, String description, String thumbnail,
                 List<Long> genre, String releaseDate, double voteAverage, String poster) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.poster = poster;
    }

    public Slide(String title, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public Slide(String title, List<Long> genre, String thumbnail) {
        this.title = title;
        this.genre = genre;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return "https://image.tmdb.org/t/p/w1280" + thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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
