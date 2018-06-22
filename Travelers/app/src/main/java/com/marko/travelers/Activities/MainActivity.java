package com.marko.travelers.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marko.travelers.Adapters.SectionsPageAdapter;
import com.marko.travelers.Fragments.FragmentAll;
import com.marko.travelers.Fragments.FragmentDrive;
import com.marko.travelers.Fragments.FragmentMy;
import com.marko.travelers.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static  final String TAG = "MainActivity";

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        mViewPager = findViewById(R.id.container);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        setupViewPager(mViewPager);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        headerCall();

        sideMenuCall();

        NavigationView navigationView = findViewById(R.id.side_nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
        }
        else {
            currentUserId = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){
                        if(!task.getResult().exists()){
                            startActivity(new Intent(MainActivity.this, SetUpActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "ERROR: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentAll(), "All");
        adapter.AddFragment(new FragmentMy(), "My Travels");
        adapter.AddFragment(new FragmentDrive(), "My Drives");
        viewPager.setAdapter(adapter);
    }


    private void headerCall() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    public void sideMenuCall(){
        drawerLayout = findViewById(R.id.main_content);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.account){
            Intent intent = new Intent(MainActivity.this, SetUpActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.logout){
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.home){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.createTravel){
            Intent intent = new Intent(MainActivity.this, CreateTravelActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
