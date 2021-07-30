package com.arb.movieguideapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView txtSignUp, txtForgotPassword;
    private CardView cvLogin;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        txtSignUp = findViewById(R.id.txtSingUp);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        cvLogin = findViewById(R.id.cv_login);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser!= null) {
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String emailId = email.getText().toString();
                String pass = password.getText().toString();

                if (emailId.isEmpty()) {
                    email.setError("Please enter email!");
                    email.requestFocus();
                }
                else if (pass.isEmpty()) {
                    password.setError("Please enter password!");
                    password.requestFocus();
                }

                if (emailId.isEmpty() && pass.isEmpty()) {
                    Snackbar.make(v,"Fields are empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                if (!(emailId.isEmpty() && pass.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(emailId, pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful())
                                        Snackbar.make(v,"Username or password is incorrect!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    else
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your email address:");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error Occurred! Please try again", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
