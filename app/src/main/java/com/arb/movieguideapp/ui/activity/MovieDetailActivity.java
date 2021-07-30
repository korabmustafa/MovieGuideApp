package com.arb.movieguideapp.ui.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.clients.GetCreditsDataService;
import com.arb.movieguideapp.clients.GetGenreDataService;
import com.arb.movieguideapp.clients.GetMovieDataService;
import com.arb.movieguideapp.clients.GetMovieTrailerService;
import com.arb.movieguideapp.data.FavoriteDbHelper;
import com.arb.movieguideapp.listeners.MovieClickListener;
import com.arb.movieguideapp.listeners.TrailerClickListener;
import com.arb.movieguideapp.models.Cast;
import com.arb.movieguideapp.models.Crew;
import com.arb.movieguideapp.models.Genre;
import com.arb.movieguideapp.models.Movie;
import com.arb.movieguideapp.models.MovieTrailer;
import com.arb.movieguideapp.wrappers.CreditsWrapper;
import com.arb.movieguideapp.wrappers.GenreWrapper;
import com.arb.movieguideapp.wrappers.MovieWrapper;
import com.arb.movieguideapp.adapters.CastAdapter;
import com.arb.movieguideapp.adapters.CrewAdapter;
import com.arb.movieguideapp.adapters.MovieAdapter;
import com.arb.movieguideapp.adapters.TrailerAdapter;
import com.arb.movieguideapp.utils.RetrofitClientInstance;
import com.arb.movieguideapp.wrappers.MovieTrailerWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView txtTitle, txtRating, txtDate, txtDescription, txtTrailer, txtGenre, txtCrew, txtCast, txtSimilarMovies;
    private ImageView imgPoster, imgCover;
    private FloatingActionButton favoriteButton;

    private String movieTitle, moviePosterPath, movieDescription, movieBackdropPath, movieReleaseDate;
    private int movieId;
    private Double movieVoteAverage;

    private TrailerAdapter mTrailerAdapter;
    private CastAdapter mCastAdapter;
    private CrewAdapter mCrewAdapter;
    private MovieAdapter mMovieAdapter;

    private List<MovieTrailer> mMovieTrailers;
    private List<Cast> mCast;
    private List<Crew> mCrew;
    private List<Genre> genreList = new ArrayList<>();

    private RecyclerView mTrailerRecyclerView, mCastRecycleView, mSimilarMovieRecycleView, mCrewRecycleView;

    private Movie movie, favorite;
    private FavoriteDbHelper favoriteDbHelper;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getMovieGenres();

        mCastRecycleView = findViewById(R.id.rv_cast);
        mCastRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mCrewRecycleView = findViewById(R.id.rv_crew);
        mCrewRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mSimilarMovieRecycleView = findViewById(R.id.rv_similar_movies);
        mSimilarMovieRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mTrailerRecyclerView = findViewById(R.id.rv_trailer);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mTrailerRecyclerView.setNestedScrollingEnabled(false);

        txtTitle = findViewById(R.id.detail_movie_title);
        txtRating = findViewById(R.id.detail_movie_ratings);
        txtDate = findViewById(R.id.detail_movie_date);
        txtDescription = findViewById(R.id.detail_movie_description);
        imgPoster = findViewById(R.id.detail_movie_img);
        imgCover = findViewById(R.id.detail_movie_cover);
        txtGenre = findViewById(R.id.detail_genre);
        txtTrailer = findViewById(R.id.detail_trailer);
        txtCast = findViewById(R.id.detail_cast);
        txtCrew = findViewById(R.id.detail_crew);
        txtSimilarMovies = findViewById(R.id.detail_similar_movies);
        favoriteButton = findViewById(R.id.addFavorites);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                movie = (Movie) bundle.getSerializable("movie");
                if (movie != null) {
                    favoriteDbHelper = new FavoriteDbHelper(MovieDetailActivity.this);
                    if (favoriteDbHelper.checkIfMovieExists(movie.getId())) {
                        favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_24));
                    } else {
                        favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                    }
                    populateActivity(movie);
                    if(isNetworkAvailable()) {
                        getCasts(movie.getId());
                        getCrew(movie.getId());
                        getTrailer(movie.getId());
                        getSimilarMovies(movie.getId());
                    }
                }
            }
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                vibratePhoneOnClick((short) 50);
                ObjectAnimator.ofFloat(favoriteButton, "rotation", 0f, 360f).setDuration(800).start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (savedInstanceState == null) {
                            Intent intent = getIntent();
                            Bundle bundle = intent.getExtras();
                            if (bundle != null) {
                                favoriteDbHelper = new FavoriteDbHelper(MovieDetailActivity.this);
                                movie = (Movie) bundle.getSerializable("movie");
                                if (movie != null) {
                                    if (!favoriteDbHelper.checkIfMovieExists(movie.getId())) {
                                        Log.v("TAG", movie.getId()+"");
                                        favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_24));
                                        saveFavorite();
                                        Snackbar.make(view, movie.getTitle() + " added to favorites!", Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));

                                        favoriteDbHelper = new FavoriteDbHelper(MovieDetailActivity.this);
                                        favoriteDbHelper.deleteFavorite(movie.getId());

                                        Snackbar.make(view, movie.getTitle() + " removed from favorites!", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                }, 400);
            }
        });
    }

    private void populateActivity(Movie mMovie) {
        movieId = mMovie.getId();
        movieTitle = mMovie.getTitle();
        movieVoteAverage = mMovie.getVoteAverage();
        moviePosterPath = mMovie.getThumbnail();
        movieDescription = mMovie.getDescription();
        movieBackdropPath = mMovie.getCoverImg();
        movieReleaseDate = mMovie.getReleaseDate();

        String poster = "https://image.tmdb.org/t/p/w342" + moviePosterPath;
        String cover = "https://image.tmdb.org/t/p/w1280" + movieBackdropPath;

        Picasso.get().load(poster).into(imgPoster);
        Picasso.get().load(cover).into(imgCover);

        txtTitle.setText(movieTitle);
        txtDescription.setText(movieDescription);
        txtDate.setText(movieReleaseDate);

        List<String> genreString = new ArrayList<>();
        if (mMovie.getGenre() != null)  {
            for (Genre genre : movie.getGenres()) {
                genreString.add(genre.getGenres());
            }
        }
        String genreRe = genreString.toString().replace("[", "");
        genreRe = genreRe.replace("]", "");
        genreRe = genreRe.replace(",", " ‧ ");
        txtGenre.setText(genreRe);

        String userRatingText = movieVoteAverage + "/10";
        txtRating.setText(userRatingText);
    }

    private void saveFavorite() {
        favoriteDbHelper = new FavoriteDbHelper(MovieDetailActivity.this);
        favorite = new Movie();

        String posterPath = moviePosterPath.replace("https://image.tmdb.org/t/p/w342", "");
        String backdropPath = movieBackdropPath.replace("https://image.tmdb.org/t/p/w1280", "");

        favorite.setId(movieId);
        favorite.setTitle(movieTitle);
        favorite.setVoteAverage(movieVoteAverage);
        favorite.setThumbnail(posterPath);
        favorite.setDescription(movieDescription);
        favorite.setCoverImg(backdropPath);
        favorite.setReleaseDate(movieReleaseDate);

        favoriteDbHelper.addFavorite(favorite);
    }

    private void populateCasts(List<Cast> mCast){
        if (mCast.size() > 0) {
            txtCast.setVisibility(View.VISIBLE);
            mCastRecycleView.setVisibility(View.VISIBLE);
            mCastAdapter = new CastAdapter(mCast);
            mCastRecycleView.setAdapter(mCastAdapter);
        }
    }

    private void getCasts(int movieId) {
        GetCreditsDataService castService = RetrofitClientInstance.getRetrofitInstance().create(GetCreditsDataService.class);
        Call<CreditsWrapper> call = castService.getCast(movieId);

        call.enqueue(new Callback<CreditsWrapper>() {
            @Override
            public void onResponse(@NonNull Call<CreditsWrapper> call, Response<CreditsWrapper> response) {
                if (response.body() != null) {
                    mCast = response.body().getCast();
                    populateCasts(mCast);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreditsWrapper> call, Throwable t) {
                showError();
            }
        });
    }

    private void populateCrew(List<Crew> mCrew){
        if (mCrew.size() > 0) {
            txtCrew.setVisibility(View.VISIBLE);
            mCrewRecycleView.setVisibility(View.VISIBLE);
            mCrewAdapter = new CrewAdapter(mCrew);
            mCrewRecycleView.setAdapter(mCrewAdapter);
        }
    }

    private void getCrew(int movieId) {
        GetCreditsDataService crewService = RetrofitClientInstance.getRetrofitInstance().create(GetCreditsDataService.class);
        Call<CreditsWrapper> call = crewService.getCrew(movieId);

        call.enqueue(new Callback<CreditsWrapper>() {
            @Override
            public void onResponse(@NonNull Call<CreditsWrapper> call, Response<CreditsWrapper> response) {
                if (response.body() != null) {
                    mCrew = response.body().getCrew();
                    populateCrew(mCrew);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreditsWrapper> call, Throwable t) {
                showError();
            }
        });
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
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<GenreWrapper> call, Throwable t) {
                    Log.d("Error ", t.getMessage());
                    showError();
                }
            });
        }
        catch (Exception e) {
            Log.d("Error ", e.getMessage());
            showError();
        }
    }

    private void populateSimilarMovies(List<Movie> mMovie){
        if (mMovie.size() > 0) {
            txtSimilarMovies.setVisibility(View.VISIBLE);
            mSimilarMovieRecycleView.setVisibility(View.VISIBLE);
            mMovieAdapter = new MovieAdapter(mMovie);
            onClickMovie(mMovie);
            mSimilarMovieRecycleView.setAdapter(mMovieAdapter);
        }
    }

    private void getSimilarMovies(int movieId) {
        try {
            GetMovieDataService movieService = RetrofitClientInstance.getRetrofitInstance().create(GetMovieDataService.class);
            Call<MovieWrapper> call = movieService.getSimilarMovie(movieId);

            call.enqueue(new Callback<MovieWrapper>() {
                @Override
                public void onResponse(Call<MovieWrapper> call, Response<MovieWrapper> response) {
                    if (response.body() != null) {
                        List<Movie> movieList = response.body().getMovies();

                        for (Movie m : movieList)
                            m.mapGenres(genreList);

                        populateSimilarMovies(movieList);

                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        showError();
                    }
                }

                @Override
                public void onFailure(Call<MovieWrapper> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("Error ", t.getMessage());
                    showError();
                }
            });
        }
        catch (Exception e) {
            progressDialog.dismiss();
            Log.d("Error ", e.getMessage());
            showError();
        }
    }

    private void onClickMovie(List<Movie> movieList) {
        mMovieAdapter = new MovieAdapter(movieList, new MovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                vibratePhoneOnClick((short) 50);
                Intent intent = new Intent(MovieDetailActivity.this, MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie", movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void populateTrailers(List<MovieTrailer> mMovieTrailers){
        if (mMovieTrailers.size() > 0) {
            txtTrailer.setVisibility(View.VISIBLE);
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
            mTrailerAdapter = new TrailerAdapter(mMovieTrailers, new TrailerClickListener() {
                @Override
                public void onMovieTrailerClick(MovieTrailer mMovieTrailer) {
                    vibratePhoneOnClick((short) 100);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mMovieTrailer.getKey())));
                }
            });
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        }
    }

    private void getTrailer(int movieId) {
        GetMovieTrailerService movieTrailerService = RetrofitClientInstance.getRetrofitInstance().create(GetMovieTrailerService.class);
        Call<MovieTrailerWrapper> call = movieTrailerService.getTrailers(movieId);

        call.enqueue(new Callback<MovieTrailerWrapper>() {
            @Override
            public void onResponse(Call<MovieTrailerWrapper> call, Response<MovieTrailerWrapper> response) {
                if (response.body() != null) {
                    mMovieTrailers = response.body().getTrailerResult();
                    populateTrailers(mMovieTrailers);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MovieTrailerWrapper> call, Throwable t) {
                showError();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movie", movie);
    }

    private void vibratePhoneOnClick(short vibrateMilliSeconds) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateMilliSeconds);
    }

    private void showError() {
        Toast.makeText(MovieDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }
}
