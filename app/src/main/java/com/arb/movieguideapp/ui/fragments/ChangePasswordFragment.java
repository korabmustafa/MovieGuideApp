package com.arb.movieguideapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.arb.movieguideapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private EditText newPassword, confirmPassword;
    private CardView cvChangePassword;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();

        newPassword = view.findViewById(R.id.txtPassword);
        confirmPassword = view.findViewById(R.id.txtChangePass);
        cvChangePassword = view.findViewById(R.id.cv_change_password);
        dialog = new ProgressDialog(getActivity());

        cvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibratePhoneOnClick(getContext(), (short) 100);
                String newPass = newPassword.getText().toString();
                String confPass = confirmPassword.getText().toString();

                if (!newPass.equals(confPass)) {
                    confirmPassword.setError("Password does not match");
                    confirmPassword.requestFocus();
                } else {
                    if (!newPass.isEmpty()) {
                        dialog.setMessage("Changing password, please wait!");
                        dialog.show();

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.updatePassword(newPass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(),
                                                        "Your password has been changed", Toast.LENGTH_SHORT).show();
//
//                                                mFirebaseAuth.signOut();
//
//                                                finishActivity();
//                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                            }
                                            else {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(),
                                                        "Password could not be changed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }

    //TODO check

    private void vibratePhoneOnClick(Context context, short vibrateMilliSeconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateMilliSeconds);
    }

    private void finishActivity() {
        if(getActivity() != null) {
            getActivity().finish();
        }
    }
}
