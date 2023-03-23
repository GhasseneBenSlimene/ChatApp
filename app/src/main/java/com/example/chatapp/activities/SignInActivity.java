package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= ActivitySignInBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.textCreateNewAccount.setOnClickListener(v ->
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));
    }

}