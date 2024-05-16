package com.example.samsungprojectlanglearner.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungprojectlanglearner.UI.Account.LoginActivity;
import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompList;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.data.Dict.DictAdapter;
import com.example.samsungprojectlanglearner.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    public static DictAdapter adapter;
    public static List<Dict> dictList;
    public static MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        createViewModel();
        createRecyclerView();
        createItemTouchHelper();
        createTextChangedListener();
        createDictItemClickListener();

//        binding.tabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                switch (tab.getPosition()){
//                    case 0:
//
//                        break;
//                    case 1:
//
//                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        //  binding.viewPager.setAdapter(new MyPagerAdapter(this));



        binding.btnAddDictionary.setOnClickListener(v -> {
            Dict dict = new Dict("", "", "");

            Intent intent = ActivityAddDict.addDictionaryIntent(MainActivity.this,
                    dict.getName(),
                    dict.getComps(),
                    dict.getId(),
                    "add"
            );
            startActivity(intent);
           // finish();
        });

    }

    private void createDictItemClickListener() {
        adapter.setDictItemClickListener(position -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Choose activity")
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dict dict = adapter.getDictList().get(position);
                            Intent intent = ActivityAddDict.addDictionaryIntent(MainActivity.this,
                                    dict.getName(),
                                    dict.getComps(),
                                    dict.getId(),
                                    "edit"
                            );
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("Study dict", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = ActivityStudy.newIntent(MainActivity.this,
                                    adapter.getDictList().get(position).getComps(), position);
                            startActivity(intent);
                        }
                    })
                    .show();
        });
    }

    private void createTextChangedListener() {
        binding.etSearchDict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Dict> thisList = new LinkedList<>();
                for (int i = 0; i < dictList.size(); i++) {
                    if (dictList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        thisList.add(dictList.get(i));
                    }
                }
                adapter.setDictList(thisList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                int position = viewHolder.getAdapterPosition();
                Dict dict = adapter.getDictList().get(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Delete dictionary " + dict.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            viewModel.remove(dict);
                            Toast.makeText(MainActivity.this,
                                    "Dictionary "+
                                            dict.getName()+
                                            " was deleted",
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", (dialog, which) -> new Thread(() ->
                                viewModel.update(dict)
                        ).start())
                        .show();

            }
        });
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewDictionaries);
    }

    private void createRecyclerView() {
        adapter = new DictAdapter();
        binding.recyclerViewDictionaries.setAdapter(adapter);
        binding.recyclerViewDictionaries.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getDicts().observe(this, new Observer<List<Dict>>() {
            @Override
            public void onChanged(List<Dict> dicts) {
                dictList = dicts;
                adapter.setDictList(dicts);
                binding.etSearchDict.setText("");
            }
        });
    }

    public static void add(Dict dict) {
        viewModel.add(dict);
    }
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshList();
    }
}