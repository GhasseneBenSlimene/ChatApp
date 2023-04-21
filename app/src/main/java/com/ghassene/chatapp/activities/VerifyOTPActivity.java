package com.ghassene.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ghassene.chatapp.R;
import com.ghassene.chatapp.utilities.Constants;
import com.ghassene.chatapp.utilities.PreferenceManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private EditText[] inputCode= new EditText[6];
    private String verificationId, name, email, password, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        preferenceManager = new PreferenceManager(getApplicationContext());

        TextView textMobile = findViewById(R.id.textMobile);


        textMobile.setText(String.format(
                "+216-%s",
                getIntent().getStringExtra("mobile")
        ));

        name = preferenceManager.getString(Constants.KEY_NAME);
        email = getIntent().getStringExtra(Constants.KEY_EMAIL);
        password = getIntent().getStringExtra(Constants.KEY_PASSWORD);
        image = getIntent().getStringExtra(Constants.KEY_IMAGE);

        Log.d("TAG", "onCreate: " + name + " " + email + " " + password + " " + image);

        inputCode[0] = findViewById(R.id.inputCode1);
        inputCode[1] = findViewById(R.id.inputCode2);
        inputCode[2] = findViewById(R.id.inputCode3);
        inputCode[3] = findViewById(R.id.inputCode4);
        inputCode[4] = findViewById(R.id.inputCode5);
        inputCode[5] = findViewById(R.id.inputCode6);

        setupOTPInputs();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button buttonVerify = findViewById(R.id.buttonVerify);

        verificationId = getIntent().getStringExtra("verificationId");

        buttonVerify.setOnClickListener(v -> {
            if(inputCode[0].getText().toString().trim().isEmpty()
                    || inputCode[1].getText().toString().trim().isEmpty()
                    || inputCode[2].getText().toString().trim().isEmpty()
                    || inputCode[3].getText().toString().trim().isEmpty()
                    || inputCode[4].getText().toString().trim().isEmpty()
                    || inputCode[5].getText().toString().trim().isEmpty()){
                Toast.makeText(VerifyOTPActivity.this, "Please enter valid code", Toast.LENGTH_LONG).show();
                return;
            }

            String code = inputCode[0].getText().toString() +
                    inputCode[1].getText().toString() +
                    inputCode[2].getText().toString() +
                    inputCode[3].getText().toString() +
                    inputCode[4].getText().toString() +
                    inputCode[5].getText().toString();

            if(verificationId != null){
                progressBar.setVisibility(View.VISIBLE);
                buttonVerify.setVisibility(View.INVISIBLE);

                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        verificationId,
                        code
                );

                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                signUp();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                buttonVerify.setVisibility(View.VISIBLE);
                                Toast.makeText(VerifyOTPActivity.this, "The verification code entered was invalid", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
        );

        findViewById(R.id.textResendOTP).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+216" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        VerifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                Toast.makeText(VerifyOTPActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        );

    }

    private void signUp() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, name);
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_PASSWORD, password);
        user.put(Constants.KEY_IMAGE, image);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupOTPInput(int index){
        inputCode[index].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    inputCode[index+1].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setupOTPInputs(){
        setupOTPInput(0);
        setupOTPInput(1);
        setupOTPInput(2);
        setupOTPInput(3);
        setupOTPInput(4);
    }
}