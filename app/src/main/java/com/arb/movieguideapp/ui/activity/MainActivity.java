package com.arb.movieguideapp.ui.activity;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.ui.fragments.FavoriteFragment;
import com.arb.movieguideapp.ui.fragments.HomeFragment;
import com.arb.movieguideapp.ui.fragments.MoreFragment;
import com.arb.movieguideapp.ui.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            setupBottomNavigationBar();
        }
    }

    private void setupBottomNavigationBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_homeFragment, R.id.nav_searchFragment, R.id.nav_favoriteFragment, R.id.nav_settingsFragment)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }
}
