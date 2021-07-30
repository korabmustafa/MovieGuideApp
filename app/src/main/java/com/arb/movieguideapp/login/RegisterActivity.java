package com.arb.movieguideapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arb.movieguideapp.ui.activity.MainActivity;
import com.arb.movieguideapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, confirmPassword, name;
    private TextView txtLogin;
    private CardView cvRegister;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseStore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStore = FirebaseFirestore.getInstance();
        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtReEmail);
        password = findViewById(R.id.txtRePassword);
        confirmPassword = findViewById(R.id.txtChangePass);
        cvRegister = findViewById(R.id.cv_register);
        txtLogin = findViewById(R.id.txtLoginHere);

        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        cvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String fullName = name.getText().toString().trim();
                String emailId = email.getText().toString().trim();
                String pass = password.getText().toString();
                String confPass = confirmPassword.getText().toString();

                if (emailId.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                }
                else if (pass.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                }

                if (pass.length() < 6) {
                    password.setError("Password must have minimum 5 characters");
                    password.requestFocus();
                }

                if (emailId.isEmpty() && pass.isEmpty()) {
                    Snackbar.make(v,"Fields are empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                if (!pass.equals(confPass)){
                    confirmPassword.setError("Password does not match");
                    confirmPassword.requestFocus();
                } else {
                    if (!(emailId.isEmpty() && pass.isEmpty())) {
                        mFirebaseAuth.createUserWithEmailAndPassword(emailId, pass)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful())
                                            Snackbar.make(v,"SignUp is unsuccessful, please try again!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        else{
                                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                            firebaseUser.sendEmailVerification()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(RegisterActivity.this,
                                                            "A verification email has been sent", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegisterActivity.this,
                                                            "An error occurred", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                                            userID = mFirebaseAuth.getCurrentUser().getUid();

                                            DocumentReference documentReference = mFirebaseStore.collection("users").document(userID);
                                            Map<String,Object> user = new HashMap<>();
                                            user.put("fName",fullName);
                                            user.put("email",email);

                                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.v("TAG", "onSuccess: user Profile is created for "+ userID);
                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.v("TAG", "onFailure: " + e.toString());
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Error Occurred!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}