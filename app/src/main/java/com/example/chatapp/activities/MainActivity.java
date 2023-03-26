package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.Constants;

import android.util.Base64;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    private void loadUserDetails() {
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);

    }

}