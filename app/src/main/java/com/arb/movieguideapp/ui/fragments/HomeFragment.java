package com.arb.movieguideapp.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.arb.movieguideapp.clients.GetGenreDataService;
import com.arb.movieguideapp.models.Genre;
import com.arb.movieguideapp.wrappers.GenreWrapper;
import com.arb.movieguideapp.ui.activity.MovieDetailActivity;
import com.arb.movieguideapp.R;
import com.arb.movieguideapp.adapters.MovieAdapter;
import com.arb.movieguideapp.adapters.SlideAdapter;
import com.arb.movieguideapp.clients.GetMovieDataService;
import com.arb.movieguideapp.listeners.MovieClickListener;
import com.arb.movieguideapp.models.Movie;
import com.arb.movieguideapp.wrappers.MovieWrapper;
import com.arb.movieguideapp.models.Slide;
import com.arb.movieguideapp.utils.RetrofitClientInstance;
import com.arb.movieguideapp.wrappers.SlideWrapper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Genre> genreList = new ArrayList<>();
//    private List<Slide> lstSlides;

    private RecyclerView rvPopular, rvNowPlaying, rvTopRated, rvUpcoming;
    private ViewPager slidePager;
    private TabLayout indicator;

    private MovieAdapter movieAdapter;
    private SlideAdapter slideAdapter;

    private AlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GetMovieDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetMovieDataService.class);

        getMovieGenres();

        initViews(view, slidePager, indicator, service.getTrendingSlides());

        initViews(view, rvPopular, R.id.rv_popular, service.getPopular());
        initViews(view, rvNowPlaying, R.id.rv_now_playing, service.getNowPlaying());
        initViews(view, rvTopRated, R.id.rv_top_rated, service.getTopRated());
        initViews(view, rvUpcoming, R.id.rv_upcoming, service.getUpcoming());
    }

    private void initViews(@NonNull View view, RecyclerView recyclerView, int recycle, Call<MovieWrapper> call){
        recyclerView = view.findViewById(recycle);

        initRecycleView(recyclerView);

        getMovie(recyclerView, call);
    }

    private void initViews(@NonNull View view, ViewPager viewPager, TabLayout tabIndicator, Call<SlideWrapper> call){
        viewPager = view.findViewById(R.id.slider_pager);
        tabIndicator = view.findViewById(R.id.indicator);

        getMovie(viewPager, tabIndicator, call);
    }

    private void initRecycleView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void getMovieGenres() {
        try {
            GetGenreDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetGenreDataService.class);
            Call<GenreWrapper> call = service.getGenre();
            call.enqueue(new Callback<GenreWrapper>() {
                @Override
                public void onResponse(Call<GenreWrapper> call, Response<GenreWrapper> response) {
                    if (response.body() != null) {
                        genreList = response.body().getGenre();
                    } else {
                        showError();
                    }
                }

                @Override
                public void onFailure(Call<GenreWrapper> call, Throwable t) {
                    showError();
                }
            });
        }
        catch (Exception e) {
            showError();
        }
    }

    private void getMovie(final RecyclerView recyclerView, Call<MovieWrapper> call) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        try {
            call.enqueue(new Callback<MovieWrapper>() {
                @Override
                public void onResponse(Call<MovieWrapper> call, Response<MovieWrapper> response) {
                    if (response.body() != null) {
                        List<Movie> movieList = response.body().getMovies();

                        for (Movie m : movieList)
                            m.mapGenres(genreList);

                        onClickMovie(movieList);

                        recyclerView.setAdapter(movieAdapter);
                        recyclerView.smoothScrollToPosition(0);
                    } else {
                        showError();
                    }
                    movieAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieWrapper> call, Throwable t) {
                    progressDialog.dismiss();
                    showError();
                }
            });
        }
        catch (Exception e) {
            progressDialog.dismiss();
            showError();
        }
        progressDialog.dismiss();
    }

    private void onClickMovie(List<Movie> movieList) {
        movieAdapter = new MovieAdapter(movieList, new MovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                vibratePhoneOnClick(getContext(), (short) 50);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie", movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getMovie(final ViewPager viewPager, final TabLayout tabIndicator, Call<SlideWrapper> call) {
        try {
            call.enqueue(new Callback<SlideWrapper>() {
                @Override
                public void onResponse(Call<SlideWrapper> call, Response<SlideWrapper> response) {
                    if (response.body() != null) {
                        List<Slide> slideList = response.body().getMovies();
                        slideAdapter = new SlideAdapter(slideList);

                        for (Slide m : slideList)
                            m.mapGenres(genreList);

                        viewPager.setAdapter(slideAdapter);
                    } else {
                        showError();
                    }
                    tabIndicator.setupWithViewPager(viewPager,true);
                }

                @Override
                public void onFailure(Call<SlideWrapper> call, Throwable t) {
                    showError();
                }
            });
        }
        catch (Exception e) {
            showError();
        }
    }

    private void vibratePhoneOnClick(Context context, short vibrateMilliSeconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateMilliSeconds);
    }

    private void showError() {
        Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
    }
}
