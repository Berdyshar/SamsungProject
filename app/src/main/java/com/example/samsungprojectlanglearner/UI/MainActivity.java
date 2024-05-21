package com.example.samsungprojectlanglearner.UI;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.UI.account.LoginActivity;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    public static FragmentRecView fragmentRecView;
    private static FragmentAddDict fragmentAddDict;
    public static FragmentManager fragmentManager;
    private AddDictViewModel viewModel;

    public static void stopFragmentAddDict() {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentRecView).commit();
        binding.btnAddDictionary.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        viewModel = new ViewModelProvider(this).get(AddDictViewModel.class);

        fragmentRecView = new FragmentRecView();
        fragmentAddDict = new FragmentAddDict();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentRecView).commit();

        binding.btnAddDictionary.setOnClickListener(v -> {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Dict dict = new Dict("Word_Word", "Name", "0");
                viewModel.setName(dict.getName());
                viewModel.setComps(dict.getComps());
                viewModel.setResult(dict.getResult());
                viewModel.setId(dict.getId());
                FragmentRecView.viewModel.add(dict);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentAddDict).commit();
                binding.btnAddDictionary.setVisibility(View.INVISIBLE);

            }
        });

    }

}