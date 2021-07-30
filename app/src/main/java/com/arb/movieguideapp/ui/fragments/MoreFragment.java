package com.arb.movieguideapp.ui.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.arb.movieguideapp.login.LoginActivity;
import com.arb.movieguideapp.R;
import com.arb.movieguideapp.ui.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MoreFragment extends Fragment {

    private TextView txtSignOut, txtVerify, txtChangePassword, txtDeactivateAccount;
    private Switch notificationSwitch;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationSwitch = view.findViewById(R.id.switch_notification);
        txtSignOut = view.findViewById(R.id.txt_sign_out);
        txtVerify = view.findViewById(R.id.txtVerify);
        txtChangePassword = view.findViewById(R.id.txt_update_password);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching data!");
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (user != null){
            if (!user.isEmailVerified()) {
                txtVerify.setVisibility(View.VISIBLE);
                txtVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "A verification email has been sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } else {
                txtVerify.setVisibility(View.GONE);
            }
            progressDialog.dismiss();
        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                vibratePhoneOnClick(getContext(), (short) 50);
                if (b) {
                    Log.v("TAG", "Checked");
                    String title = "Movie Guide Notification";
                    String message = "Go search movies...";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),
                            "personal notifications")
                            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                            .setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                            .setLights(Color.BLUE, 3000, 3000)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    Intent intent = new Intent(getContext(), MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());
                }
            }
        });

        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibratePhoneOnClick(getContext(), (short) 50);
                getFragment(new ChangePasswordFragment());
            }
        });
//
//        txtDeactivateAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                vibratePhoneOnClick(getContext(), (short) 100);
//                user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null) {
//                    final AlertDialog.Builder deactivateDialog = new AlertDialog.Builder(v.getContext());
//                    deactivateDialog.setTitle("Deactivate Account");
//                    deactivateDialog.setMessage("Are you sure you want to deactivate your account?");
//
//                    deactivateDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            progressDialog.setMessage("Deactivating, please wait...");
//                            progressDialog.show();
//
//                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (!task.isSuccessful()) {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(getActivity(), "Account deactivated", Toast.LENGTH_SHORT).show();
//
//                                        finishActivity();
//                                        startActivity(new Intent(getActivity(), LoginActivity.class));
//                                    }
//                                    else {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(getActivity(), "Account could not be deactivated", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    });
//                    deactivateDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finishActivity();
//                        }
//                    });
//
//                    deactivateDialog.create().show();
//                }
//            }
//        });

        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibratePhoneOnClick(getContext(), (short) 100);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void getFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.more_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void finishActivity() {
        if(getActivity() != null) {
            getActivity().finish();
        }
    }

    private void vibratePhoneOnClick(Context context, short vibrateMilliSeconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateMilliSeconds);
    }
}
