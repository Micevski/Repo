package com.marko.travelers.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.marko.travelers.R;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        headerCall();

        sideMenuCall();

        NavigationView navigationView = findViewById(R.id.side_nav_menu);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void headerCall() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    public void sideMenuCall(){
        drawerLayout = findViewById(R.id.id_account);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.account){
            Intent intent = new Intent(AccountActivity.this, AccountActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.logout){
            Intent intent = new Intent(AccountActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.home){
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.createTravel){
            Intent intent = new Intent(AccountActivity.this, CreateTravelActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}
