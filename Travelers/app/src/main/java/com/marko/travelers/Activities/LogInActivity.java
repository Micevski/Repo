package com.marko.travelers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marko.travelers.R;

public class LogInActivity extends AppCompatActivity {

    private EditText loginMail;
    private EditText loginPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        loginMail = findViewById(R.id.mail_login);
        loginPassword = findViewById(R.id.password_login);

        signUpBtnClick();
        signInBtnClick();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            finish();
        }
    }

    private void signUpBtnClick() {

        Button signUpBtn = findViewById(R.id.log_in_btn_sing_up);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signInBtnClick() {

        Button signInBtn = findViewById(R.id.log_in_btn_log_in);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Signing in ...");
                progressDialog.show();

                String emailVal = loginMail.getText().toString();
                String passVal = loginPassword.getText().toString();

                if(!TextUtils.isEmpty(emailVal) && !TextUtils.isEmpty(passVal)){
                    mAuth.signInWithEmailAndPassword(emailVal,passVal).addOnCompleteListener(new OnCompleteListener <AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                String ex = task.getException().getMessage();
                                Toast.makeText(LogInActivity.this,"ERROR: "+ex,Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(LogInActivity.this,"ALL FIELDS ARE REQUIRED",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

}
