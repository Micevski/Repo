package com.marko.travelers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marko.travelers.Manifest;
import com.marko.travelers.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetUpActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private static final String READ_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int LOCATION_PERMISIONS_REQUEST_CODE = 1234;
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Button buttonImageAdd;
    Uri imgUri;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private EditText edit_name;
    private EditText edit_surname;
    private EditText edit_username;
    private EditText edit_phone;
    private Button set_up_btn;

    private String user_id;

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Boolean readWritePermissionsGranted = false;
    private Boolean isChange = false;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();

        edit_name = findViewById(R.id.set_up_edit_name);
        edit_surname = findViewById(R.id.set_up_edit_surname);
        edit_username = findViewById(R.id.set_up_edit_user_name);
        edit_phone = findViewById(R.id.set_up_edit_phone);
        set_up_btn = findViewById(R.id.set_up_btn);
        progressDialog = new ProgressDialog(this);



        headerCall();

        sideMenuCall();



        NavigationView navigationView = findViewById(R.id.side_nav_menu);
        navigationView.setNavigationItemSelectedListener(this);
        setImageOnImageView();
        addDataToDataBase();
        fillItemsWithData();

    }

    private void addDataToDataBase() {


        progressDialog.setMessage("Adding data...");
        set_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = edit_name.getText().toString();
                final String surname = edit_surname.getText().toString();
                final String username = edit_username.getText().toString();
                final String phone = edit_phone.getText().toString();

                if(isChange) {

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(username) &&
                            !TextUtils.isEmpty(phone) && imgUri != null) {

                        user_id = firebaseAuth.getCurrentUser().getUid();

                        progressDialog.show();

                        StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");

                        image_path.putFile(imgUri).addOnCompleteListener(new OnCompleteListener <UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task <UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {

                                    storeFirestore(task, name, surname, username, phone);

                                } else {
                                    Toast.makeText(SetUpActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                            }
                        });

                    }

                }
                else {
                    storeFirestore(null, name, surname, username, phone);
                }
            }
        });
    }


    private void setImageOnImageView() {
        imageView = findViewById(R.id.sing_up_image);
        buttonImageAdd = findViewById(R.id.sing_up_add_image);

        buttonImageAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SetUpActivity.this, READ_STORAGE)
                            == PackageManager.PERMISSION_GRANTED){
                        if(ContextCompat.checkSelfPermission(SetUpActivity.this, WRITE_STORAGE)
                                == PackageManager.PERMISSION_GRANTED){
                            readWritePermissionsGranted = true;
                        }else {
                            ActivityCompat.requestPermissions(SetUpActivity.this, permissions, LOCATION_PERMISIONS_REQUEST_CODE);
                        }
                    }else {
                        ActivityCompat.requestPermissions(SetUpActivity.this, permissions, LOCATION_PERMISIONS_REQUEST_CODE);
                    }
                }
                if(readWritePermissionsGranted == true){
                    openGallery();
                }


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        readWritePermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISIONS_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            readWritePermissionsGranted = false;
                            return;
                        }
                        readWritePermissionsGranted = true;

                        }

                }
            }
        }
    }
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            imgUri = data.getData();
            imageView.setImageURI(imgUri);
            isChange = true;
        }
        else {
            Toast.makeText(SetUpActivity.this,"Try Again", Toast.LENGTH_LONG).show();
        }
    }

    private void storeFirestore(Task<UploadTask.TaskSnapshot> task, String name , String surname, String username, String phone){

        Uri dowloadUri;

        if(task != null){
            dowloadUri = task.getResult().getUploadSessionUri();
        }
        else {
            dowloadUri = imgUri;
        }


        Map<String, String> userMap = new HashMap <>();
            userMap.put("name", name);
        userMap.put("surname", surname);
        userMap.put("username", username);
        userMap.put("phone", phone);
        userMap.put("image", dowloadUri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener <Void>() {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(SetUpActivity.this, MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(SetUpActivity.this, "ERROR: "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
            }
        });
    }

    private void fillItemsWithData() {

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if (task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String surname = task.getResult().getString("surname");
                        String username = task.getResult().getString("username");
                        String phone = task.getResult().getString("phone");
                        String image = task.getResult().getString("image");

                        edit_name.setText(name);
                        edit_surname.setText(surname);
                        edit_username.setText(username);
                        edit_phone.setText(phone);

                        imgUri = Uri.parse(image);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.ic_launcher_foreground);
                        Glide.with(SetUpActivity.this).setDefaultRequestOptions(requestOptions).load(image).into(imageView);

                    }
                    else {

                        Toast.makeText(SetUpActivity.this, "There is some ERROR... Please try again later",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(SetUpActivity.this, "ERROR: "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
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
            Intent intent = new Intent(SetUpActivity.this, SetUpActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.logout){
            mAuth.signOut();
            Intent intent = new Intent(SetUpActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.home){
            Intent intent = new Intent(SetUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.createTravel){
            Intent intent = new Intent(SetUpActivity.this, CreateTravelActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}
