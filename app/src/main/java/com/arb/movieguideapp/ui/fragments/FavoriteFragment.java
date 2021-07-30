package com.arb.movieguideapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.data.FavoriteDbHelper;
import com.arb.movieguideapp.listeners.MovieClickListener;
import com.arb.movieguideapp.ui.activity.MovieDetailActivity;
import com.arb.movieguideapp.adapters.FavoriteMovieAdapter;
import com.arb.movieguideapp.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements SearchView.OnQueryTextListener {

    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView rvFavorites;
    private FavoriteMovieAdapter favoriteMovieAdapter;
    private FavoriteDbHelper favoriteDbHelper;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        searchView = view.findViewById(R.id.search_fav_movie);

        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMovies();
    }

    private void getMovies() {
        rvFavorites = getActivity().findViewById(R.id.favorite_recycler);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.setItemAnimator(new DefaultItemAnimator());
        rvFavorites.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        getAllFavorite();

        String input_movie = searchView.getQuery().toString();
        searchFavoriteMovie(input_movie);

        rvFavorites.setAdapter(favoriteMovieAdapter);

        favoriteMovieAdapter.notifyDataSetChanged();
    }

    private void populateMovies(List<Movie> movies) {
        favoriteMovieAdapter = new FavoriteMovieAdapter(movies, new MovieClickListener() {
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

    private void getAllFavorite() {
        movieList.clear();

        favoriteDbHelper = new FavoriteDbHelper(getContext());
        movieList = favoriteDbHelper.getFavoriteMovies();

        populateMovies(movieList);
    }

    private void searchFavoriteMovie(String searchMovie) {
        movieList.clear();

        favoriteDbHelper = new FavoriteDbHelper(getContext());
        movieList = favoriteDbHelper.searchMovies(searchMovie);

        populateMovies(movieList);
    }

    private void vibratePhoneOnClick(Context context, short vibrateMilliSeconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateMilliSeconds);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        getMovies();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}