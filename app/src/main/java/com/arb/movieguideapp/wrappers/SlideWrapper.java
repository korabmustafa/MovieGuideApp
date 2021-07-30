package com.arb.movieguideapp.wrappers;

import com.arb.movieguideapp.models.Slide;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SlideWrapper {

    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<Slide> slides;

    public List<Slide> getMovies() {
        return slides;
    }

    public void setMovies(List<Slide> slides) {
        this.slides = slides;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
