package com.ghassene.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ghassene.chatapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);

        final EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetOTP = findViewById(R.id.buttonGetOTP);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonGetOTP.setOnClickListener(v -> {
            if (inputMobile.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(android.view.View.VISIBLE);
            buttonGetOTP.setVisibility(android.view.View.INVISIBLE);

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+216" + inputMobile.getText().toString(),
                    60,
                    TimeUnit.SECONDS,
                    SendOTPActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(android.view.View.GONE);
                            buttonGetOTP.setVisibility(android.view.View.VISIBLE);
                            Toast.makeText(SendOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId,
                                               @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            progressBar.setVisibility(android.view.View.GONE);
                            buttonGetOTP.setVisibility(android.view.View.VISIBLE);
                            Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                            intent.putExtra("mobile", inputMobile.getText().toString());
                            intent.putExtra("verificationId", verificationId);
                            startActivity(intent);
                        }
                    });
            Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
            intent.putExtra("mobile", inputMobile.getText().toString());
            startActivity(intent);
        });
    }
}