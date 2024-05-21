package com.example.samsungprojectlanglearner.UI;

import static com.example.samsungprojectlanglearner.data.Comp.CompList.toArray;
import static com.example.samsungprojectlanglearner.data.Comp.CompList.toStr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.Translation;
import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompAdapter;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.databinding.FragmentAddDictBinding;

import java.util.LinkedList;
import java.util.List;


public class FragmentAddDict extends Fragment {
    public static AddDictViewModel viewModel;
    private Dict dictActivity;
    private static LinkedList<Comp> compList;
    private CompAdapter compAdapter;
    private FragmentAddDictBinding binding;
    public FragmentAddDict() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddDictBinding.inflate(getLayoutInflater());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddDictBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //        viewModel = new ViewModelProvider(this).get(AddDictViewModel.class);
//        viewModel.getDict().observe(getViewLifecycleOwner(), new Observer<Dict>() {
//            @Override
//            public void onChanged(Dict dict) {
//                dictActivity = dict;
//                Toast.makeText(getContext(), dictActivity.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        viewModel.setDict(dictActivity);


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compAdapter = new CompAdapter();
        binding.recyclerViewComp.setAdapter(compAdapter);
        binding.recyclerViewComp.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(AddDictViewModel.class);
        dictActivity = new Dict("", "", "");
    //    dictActivity = viewModel.getDict();
        String name = viewModel.getName();
        String comps = viewModel.getComps();
        String result = viewModel.getResult();
        int id = viewModel.getId();

        dictActivity.setName(name);
        dictActivity.setComps(comps);
        dictActivity.setResult(result);
        dictActivity.setId(id);
        Toast.makeText(getContext(), name + " " + comps + " " + result + " " + id +" ", Toast.LENGTH_SHORT).show();

        binding.etInputDictionaryName.setText(dictActivity.getName());
        compList = (LinkedList<Comp>) toArray(dictActivity.getComps());
        showList();
        addComps();
        changeComp();
        createItemTouchHelper();


        binding.btnSave.setOnClickListener(v -> {
            updateRepository();
           // dictActivity.setName(binding.etInputDictionaryName.getText().toString());
            FragmentRecView.viewModel.update(dictActivity);


            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
         //   getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, MainActivity.fragmentRecView).commit();

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showList();
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
                compList.remove(compList.get(position));
                showList();
                updateRepository();
            }
        }).attachToRecyclerView(binding.recyclerViewComp);
    }
    private void addComps() {
//        binding.btnLoadTranslation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Translation translation = new Translation();
//                translation.translate(binding.etInputWord.getText().toString(), "en-ru", new Translation.Callback<List<String>>() {
//                    @Override
//                    public void onSuccess(List<String> result) {
//                        String translatedText = result.get(0);
//                        binding.etInputTranslation.setText(translatedText);
//                        Log.d("Translation", "Translated text: " + translatedText);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.e("Translation", "Error translating text: " + t.getMessage());
//                        binding.etInputTranslation.setText("Error");
//                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        binding.btnAddComp.setOnClickListener(v -> {
            Comp comp = new Comp(binding.etInputWord.getText().toString(),
                    binding.etInputTranslation.getText().toString());
            if (notNullCheck(comp)) {
                compList.add(comp);
                updateRepository();
            }
            else {
                Toast.makeText(getContext(), "Sorry, but word and translation cannot be null", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void changeComp() {
        compAdapter.setCompItemClickListener(position -> {
            Comp comp = compList.get(position);
            binding.etInputWord.setText(comp.getWord());
            binding.etInputTranslation.setText(comp.getTranslation());
            binding.btnAddComp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (notNullCheck(comp)) {
                        comp.setWord(binding.etInputWord.getText().toString());
                        comp.setTranslation(binding.etInputTranslation.getText().toString());
                        compList.set(position, comp);
                        updateRepository();
                    }
                    else {
                        Toast.makeText(getContext(), "Sorry, but word and translation cannot be null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
    private void updateRepository() {
        dictActivity.setName(binding.etInputDictionaryName.getText().toString());
        dictActivity.setComps(toStr(compList));
        dictActivity.setResult("0");
        binding.etInputWord.setText("");
        binding.etInputTranslation.setText("");
        showList();
    }
    public boolean notNullCheck(Comp comp) {
        boolean Check = true;
        if (comp.getWord().isEmpty() || comp.getTranslation().isEmpty()) {
            Check = false;
        }
        return Check;
    }
    private void showList() {
        compAdapter.setCompList(compList);
    }
}