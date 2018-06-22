package com.marko.travelers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.DragAndDropPermissions;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marko.travelers.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateTravelActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private EditText editFrom;
    private EditText editTo;
    private EditText editFree;
    private EditText editPrice;
    private EditText editValute;
    private DatePicker Date;
    private TimePicker Time;
    private Button postTravel;
    private String currentUserId;

    private FirebaseFirestore firebaseFirestore;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel);

        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        editFrom = findViewById(R.id.createFrom);
        editTo = findViewById(R.id.createTo);
        editFree = findViewById(R.id.createFree);
        editPrice = findViewById(R.id.createPrice);
        editValute = findViewById(R.id.createValute);
        Date = findViewById(R.id.createDate);
        Time = findViewById(R.id.createTime);
        postTravel = findViewById(R.id.createBtn);
        currentUserId = mAuth.getCurrentUser().getUid();

        progressDialog = new ProgressDialog(this);

        headerCall();

        sideMenuCall();

        NavigationView navigationView = findViewById(R.id.side_nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

        onButtonClickfillDataBase();

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
        drawerLayout = findViewById(R.id.create_travel_id);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.account){
            Intent intent = new Intent(CreateTravelActivity.this, SetUpActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.logout){
            mAuth.signOut();
            Intent intent = new Intent(CreateTravelActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.home){
            Intent intent = new Intent(CreateTravelActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.createTravel){
            Intent intent = new Intent(CreateTravelActivity.this, CreateTravelActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


//__________________________________________________________________________________________________________________________________________


    private void onButtonClickfillDataBase() {

        progressDialog.setMessage("Adding data...");
        postTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                final String from = editFrom.getText().toString();
                final String to = editTo.getText().toString();
                final String free = editFree.getText().toString();
                final String price = editPrice.getText().toString();
                final String value = editValute.getText().toString();
                final String date = Date.getDayOfMonth() + "." + Date.getMonth() + " " + Date.getYear();
                final String time = Time.getHour() + ":" + Time.getMinute();




                if(!TextUtils.isEmpty(from)&& !TextUtils.isEmpty(to)&& !TextUtils.isEmpty(free)&& !TextUtils.isEmpty(price)
                        && !TextUtils.isEmpty(value)&& !TextUtils.isEmpty(date)&& !TextUtils.isEmpty(time)){

                    Map<String, String> travelMap = new HashMap<>();
                    travelMap.put("from", from);
                    travelMap.put("to", to);
                    travelMap.put("free", free);
                    travelMap.put("price", price);
                    travelMap.put("value", value);
                    travelMap.put("date", date);
                    travelMap.put("time", time);
                    travelMap.put("user_id", currentUserId);

                    firebaseFirestore.collection("Travels").add(travelMap).addOnCompleteListener(new OnCompleteListener <DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task <DocumentReference> task) {

                            if(task.isSuccessful()){
                                startActivity(new Intent(CreateTravelActivity.this, MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(CreateTravelActivity.this, "ERROR: "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            }
                            progressDialog.dismiss();

                        }
                    });
                }

            }
        });


    }

}
