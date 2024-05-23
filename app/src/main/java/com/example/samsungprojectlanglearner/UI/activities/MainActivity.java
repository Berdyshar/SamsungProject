package com.example.samsungprojectlanglearner.UI.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.UI.viewModels.DictViewModel;
import com.example.samsungprojectlanglearner.UI.account.LoginActivity;
import com.example.samsungprojectlanglearner.UI.fragments.FragmentAddDict;
import com.example.samsungprojectlanglearner.UI.fragments.FragmentRecView;
import com.example.samsungprojectlanglearner.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends FragmentActivity {
    public static String key = "";
    public ActivityMainBinding binding;
    public static FragmentRecView fragmentRecView;
    public static FragmentAddDict fragmentAddDict;
    public static FragmentManager fragmentManager;

    public static void startAddDictFragment() {
        fragmentManager.beginTransaction().add(R.id.fragment_containerAddDict, fragmentAddDict).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.samsungprojectlanglearner.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && DictViewModel.isCreatingDict()) {
            startActivity(new Intent(this, ActivityAddDict.class));
        }
        fragmentRecView = new FragmentRecView();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_containerRecView, fragmentRecView).commit();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && key.equals("add")){
            fragmentAddDict = new FragmentAddDict();
            fragmentManager.beginTransaction().add(R.id.fragment_containerAddDict, fragmentAddDict).commit();
        }



    }

}