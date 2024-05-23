package com.example.samsungprojectlanglearner.UI.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.samsungprojectlanglearner.UI.activities.MainActivity;

import com.example.samsungprojectlanglearner.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnNewActivity.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
        binding.btnLogin.setOnClickListener(v -> {
            if(binding.etEmail.getText().toString().isEmpty()
                    || binding.etPassword.getText().toString().isEmpty()){
                Toast.makeText(LoginActivity.this, "Fill email and password please", Toast.LENGTH_SHORT).show();
            }
            else{
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etEmail.getText().toString()
                        , binding.etPassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            }
                            task.addOnFailureListener(e -> {
                                Log.e("LoginExeption", Objects.requireNonNull(e.getMessage()));
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                        });
            }


        });

    }
}