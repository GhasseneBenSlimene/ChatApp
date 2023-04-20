package com.ghassene.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.ghassene.chatapp.R;

public class VerifyOTPActivity extends AppCompatActivity {

    private EditText[] inputCode= new EditText[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "+216-%s",
                getIntent().getStringExtra("mobile")
        ));

        inputCode[0] = findViewById(R.id.inputCode1);
        inputCode[1] = findViewById(R.id.inputCode2);
        inputCode[2] = findViewById(R.id.inputCode3);
        inputCode[3] = findViewById(R.id.inputCode4);
        inputCode[4] = findViewById(R.id.inputCode5);
        inputCode[5] = findViewById(R.id.inputCode6);

        setupOTPInputs();
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