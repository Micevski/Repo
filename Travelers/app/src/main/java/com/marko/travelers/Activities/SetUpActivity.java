package com.marko.travelers.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.marko.travelers.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class SetUpActivity extends AppCompatActivity {


    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private CircleImageView imageView;
    private EditText edit_name;
    private EditText edit_surname;
    private EditText edit_username;
    private EditText edit_phone;
    private Button buttonImageAdd;
    private Button setupBtn;
    private ProgressDialog progressDialog;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        imageView = findViewById(R.id.sing_up_image);
        buttonImageAdd = findViewById(R.id.sing_up_add_image);
        edit_name = findViewById(R.id.set_up_edit_name);
        edit_surname = findViewById(R.id.set_up_edit_surname);
        edit_username = findViewById(R.id.set_up_edit_user_name);
        edit_phone = findViewById(R.id.set_up_edit_phone);
        setupBtn = findViewById(R.id.set_up_btn);
        progressDialog = new ProgressDialog(this);



        setupBtn.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String surname = task.getResult().getString("surname");
                        String username = task.getResult().getString("username");
                        String phone = task.getResult().getString("phone");
                        String image = task.getResult().getString("image");

                        mainImageURI = Uri.parse(image);

                        edit_name.setText(name);
                        edit_surname.setText(surname);
                        edit_username.setText(username);
                        edit_phone.setText(phone);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.ic_launcher_foreground);

                        Glide.with(SetUpActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(imageView);


                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(SetUpActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }


                setupBtn.setEnabled(true);

            }
        });


        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = edit_name.getText().toString();
                final String surname = edit_surname.getText().toString();
                final String username = edit_username.getText().toString();
                final String phone = edit_phone.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(username)
                        && !TextUtils.isEmpty(phone) && mainImageURI != null) {



                    if (isChanged) {

                        user_id = firebaseAuth.getCurrentUser().getUid();

                        File newImageFile = new File(mainImageURI.getPath());
                        try {

                            compressedImageFile = new Compressor(SetUpActivity.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();

                        UploadTask image_path = storageReference.child("profile_images").child(user_id + ".jpg").putBytes(thumbData);

                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    storeFirestore(task, name, surname, username, phone);

                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(SetUpActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();



                                }
                            }
                        });

                    } else {

                        storeFirestore(null, name, surname, username, phone);

                    }

                }

            }

        });

//        buttonImageAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//                    if(ContextCompat.checkSelfPermission(SetUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
//                        Toast.makeText(SetUpActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
//                        ActivityCompat.requestPermissions(SetUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//                    } else {
//
//                        BringImagePicker();
//
//                    }
//
//                } else {
//
//                    BringImagePicker();
//
//                }
//
//            }
//
//        });


    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String name , String surname, String username, String phone) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult().getUploadSessionUri();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("surname", surname);
        userMap.put("username", username);
        userMap.put("phone", phone);
        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(SetUpActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(SetUpActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(SetUpActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }



            }
        });


    }

//    private void BringImagePicker() {
//
//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(1, 1)
//                .start(SetUpActivity.this);
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//                mainImageURI = result.getUri();
//                imageView.setImageURI(mainImageURI);
//
//                isChanged = true;
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//
//                Exception error = result.getError();
//
//            }
//        }
//
//    }
}
