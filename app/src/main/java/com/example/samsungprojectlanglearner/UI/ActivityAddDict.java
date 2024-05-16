package com.example.samsungprojectlanglearner.UI;

import static com.example.samsungprojectlanglearner.data.Comp.CompList.toArray;
import static com.example.samsungprojectlanglearner.data.Comp.CompList.toStr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompAdapter;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.data.DictDataBase;
import com.example.samsungprojectlanglearner.databinding.ActivityAddDicitonaryBinding;

import java.util.LinkedList;
import java.util.List;

public class ActivityAddDict extends AppCompatActivity {
    private String key;
    private ActivityAddDicitonaryBinding binding;
    private Dict dictActivity;
    private static LinkedList<Comp> compList;
    private CompAdapter compAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDicitonaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        compAdapter = new CompAdapter();
        binding.recyclerViewComp.setAdapter(compAdapter);
        binding.recyclerViewComp.setLayoutManager(new LinearLayoutManager(ActivityAddDict.this));

        dictActivity = new Dict("", "", "");
        int idIntent = getIntent().getIntExtra("ID", 0);
        String compsIntent = getIntent().getStringExtra("COMPS");
        String nameIntent = getIntent().getStringExtra("NAME");
        key = getIntent().getStringExtra("KEY");
        dictActivity.setName(nameIntent);
        dictActivity.setComps(compsIntent);
        dictActivity.setId(idIntent);
        binding.etInputDictionaryName.setText(dictActivity.getName());
        compList = (LinkedList<Comp>) toArray(dictActivity.getComps());
        showList();
        addComps();
        changeComp();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Comp comp = compList.get(position);
                compList.remove(comp);
                showList();
                updateRepository(false);
            }
        }).attachToRecyclerView(binding.recyclerViewComp);
        binding.btnSave.setOnClickListener(v -> {
            updateRepository(false);
            if(notNullCheck(dictActivity)) {
                    if (key.equals("add")) {
                        dictActivity.setResult("0");
                        MainActivity.viewModel.add(dictActivity);
                        startActivity(new Intent(ActivityAddDict.this, MainActivity.class));
                        finish();

                    }
                    else if (key.equals("edit")) {
                        updateRepository(true);
                    }

                }
                else {
                    Toast.makeText(ActivityAddDict.this,
                            "Sorry, but dict name cannot be null",
                            Toast.LENGTH_SHORT).show();
                }
            });

    }
    private void updateRepository(boolean key) {
        dictActivity.setName(binding.etInputDictionaryName.getText().toString());
        dictActivity.setComps(toStr(compList));
        dictActivity.setResult("0");
        MainActivity.viewModel.update(dictActivity);
        if (key) {
            finish();
        }
    }
    private void showList() {
        compAdapter.setCompList(compList);
    }

    private void changeComp() {
        compAdapter.setCompItemClickListener(position -> {
            Comp comp = compList.get(position);
            binding.etInputWord.setText(comp.getWord());
            binding.etInputTranslation.setText(comp.getTranslation());
            binding.btnAddComp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comp.setWord(binding.etInputWord.getText().toString());
                    comp.setTranslation(binding.etInputTranslation.getText().toString());
                    if (notNullCheck(comp)) {
                        compList.set(position, comp);
                        showList();
                        binding.etInputWord.setText("");
                        binding.etInputTranslation.setText("");
                        updateRepository(false);
                        addComps();
                    }
                    else {
                        Toast.makeText(ActivityAddDict.this, "Sorry, but word and translation cannot be null", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        });
    }


    private void addComps() {
        binding.btnAddComp.setOnClickListener(v -> {
            String word = binding.etInputWord.getText().toString();
            String translation = binding.etInputTranslation.getText().toString();
            Comp comp = new Comp(word, translation);
            if (notNullCheck(comp)) {
                compList.add(comp);
                showList();
                binding.etInputWord.setText("");
                binding.etInputTranslation.setText("");
                updateRepository(false);
            }
            else {
                Toast.makeText(ActivityAddDict.this, "Sorry, but word and translation cannot be null", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public static Intent addDictionaryIntent(Context context, String name, String comps, int id, String key) {
        Intent intent = new Intent(context, ActivityAddDict.class);
        intent.putExtra("NAME", name);
        intent.putExtra("COMPS", comps);
        intent.putExtra("ID", id);
        intent.putExtra("KEY", key);
        return intent;
    }
    public boolean notNullCheck(Comp comp) {
        boolean Check = true;
        if (comp.getWord().isEmpty() || comp.getTranslation().isEmpty()) {
            Check = false;
        }
        return Check;
    }
    public boolean notNullCheck(Dict dict) {
        boolean Check = true;
        if (dict.getName().isEmpty()) {
            Check = false;
        }
        return Check;
    }
}