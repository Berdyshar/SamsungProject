package com.example.samsungprojectlanglearner.UI.activities;

import static com.example.samsungprojectlanglearner.data.Comp.CompList.toArray;
import static com.example.samsungprojectlanglearner.data.Comp.CompList.toStr;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungprojectlanglearner.Translation;
import com.example.samsungprojectlanglearner.UI.viewModels.DictViewModel;
import com.example.samsungprojectlanglearner.UI.fragments.FragmentAddDict;
import com.example.samsungprojectlanglearner.UI.fragments.FragmentRecView;
import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompAdapter;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.databinding.ActivityAddDicitonaryBinding;

import java.util.LinkedList;
import java.util.List;

public class ActivityAddDict extends AppCompatActivity implements LifecycleOwner {
    public static String key;
    private ActivityAddDicitonaryBinding binding;
    public static Dict dictActivity;
    private static LinkedList<Comp> compList;
    private CompAdapter compAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDicitonaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, MainActivity.class);
            MainActivity.key = "add";
            FragmentAddDict.key = "add";
            startActivity(intent);
            finish();
        }


        getValues();
        createRecyclerView();
        createTextWatcher();
        addComps();
        changeComp();
        createItemTouchHelper();


        binding.btnSave.setOnClickListener(v -> {

            if(!dictActivity.getName().isEmpty()) {
                    if (key.equals("add")) {
                        FragmentRecView.viewModel.add(dictActivity);
                    }
                    else if (key.equals("edit")) {
                        FragmentRecView.viewModel.update(dictActivity);
                    }
                    DictViewModel.setDict(null);
                DictViewModel.setCreatingDict(false);
                MainActivity.key = "";
                finish();
            }
            else {
                Toast.makeText(ActivityAddDict.this,
                        "Sorry, but dict name cannot be null",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        dictActivity = DictViewModel.dict;
    }

    private void createTextWatcher() {
        binding.etInputDictionaryName.setText(dictActivity.getName());
        binding.etInputDictionaryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateRepository();

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createRecyclerView() {
        compAdapter = new CompAdapter();
        binding.recyclerViewComp.setAdapter(compAdapter);
        binding.recyclerViewComp.setLayoutManager(new LinearLayoutManager(ActivityAddDict.this));
        compList = (LinkedList<Comp>) toArray(dictActivity.getComps());
        showList();
    }

    private void getValues() {
        dictActivity = new Dict("", "", "");
        if (DictViewModel.dict != null) {
            dictActivity = DictViewModel.dict;
        }
    }

    private void createItemTouchHelper() {
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
                updateRepository();
            }
        }).attachToRecyclerView(binding.recyclerViewComp);
    }


    private void updateRepository() {
        dictActivity.setName(binding.etInputDictionaryName.getText().toString());
        dictActivity.setComps(toStr(compList));
        dictActivity.setResult("0");
        DictViewModel.setDict(dictActivity);
    }
    private void showList() {
        compAdapter.setCompList(compList);
    }

    private void changeComp() {
        compAdapter.setCompItemClickListener(position -> {
            Comp comp = compList.get(position);
            binding.etInputWord.setText(comp.getWord());
            binding.etInputTranslation.setText(comp.getTranslation());
            binding.btnAddComp.setOnClickListener(v -> {
                if (notNullCheck(comp)) {
                    comp.setWord(binding.etInputWord.getText().toString());
                    comp.setTranslation(binding.etInputTranslation.getText().toString());
                    compList.set(position, comp);
                    showList();
                    binding.etInputWord.setText("");
                    binding.etInputTranslation.setText("");
                    updateRepository();
                    addComps();
                }
                else {
                    Toast.makeText(ActivityAddDict.this, "Sorry, but word and translation cannot be null", Toast.LENGTH_SHORT).show();
                }

            });
        });
    }


    private void addComps() {
        binding.btnLoadTranslation.setOnClickListener(v -> {
            Translation translation = new Translation();
            translation.translate(binding.etInputWord.getText().toString(), "en-ru", new Translation.Callback<List<String>>() {
                @Override
                public void onSuccess(List<String> result) {
                    String translatedText = result.get(0);
                    binding.etInputTranslation.setText(translatedText);
                    Log.d("Translation", "Translated text: " + translatedText);
                }

                @Override
                public void onError(Throwable t) {
                    Log.e("Translation", "Error translating text: " + t.getMessage());
                    binding.etInputTranslation.setText("Error");
                    Toast.makeText(ActivityAddDict.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        binding.btnAddComp.setOnClickListener(v -> {
            String word = binding.etInputWord.getText().toString();
            String translation = binding.etInputTranslation.getText().toString();
            Comp comp = new Comp(word, translation);
            if (notNullCheck(comp)) {
                compList.add(comp);
                showList();
                binding.etInputWord.setText("");
                binding.etInputTranslation.setText("");
                updateRepository();
            }
            else {
                Toast.makeText(ActivityAddDict.this, "Sorry, but word and translation cannot be null", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public boolean notNullCheck(Comp comp) {
        return !comp.getWord().isEmpty() && !comp.getTranslation().isEmpty();
    }
}