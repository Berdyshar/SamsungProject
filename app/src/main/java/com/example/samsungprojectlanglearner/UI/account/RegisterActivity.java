package com.example.samsungprojectlanglearner.UI.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.samsungprojectlanglearner.UI.activities.MainActivity;
import com.example.samsungprojectlanglearner.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSignUp.setOnClickListener(v -> {
            if(binding.etEmail.getText().toString().isEmpty()
                    || binding.etUsername.getText().toString().isEmpty()
                    || binding.etPassword.getText().toString().isEmpty()){
                Toast.makeText(RegisterActivity.this, "Fill email, username and password please", Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.etEmail.getText().toString()
                        , binding.etPassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("email", binding.etEmail.getText().toString());
                                hashMap.put("username", binding.etUsername.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                        .setValue(hashMap);
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                            else {
                                Log.d("Error", String.valueOf(task.getException()));
                                Toast.makeText(RegisterActivity.this, String.valueOf(task.getException()) , Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

    }
}