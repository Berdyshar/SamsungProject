package com.example.samsungprojectlanglearner.UI.fragments;

import static com.example.samsungprojectlanglearner.data.Comp.CompList.toArray;
import static com.example.samsungprojectlanglearner.data.Comp.CompList.toStr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.samsungprojectlanglearner.UI.viewModels.DictViewModel;
import com.example.samsungprojectlanglearner.UI.activities.MainActivity;
import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompAdapter;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.databinding.FragmentAddDictBinding;

import java.util.LinkedList;


public class FragmentAddDict extends Fragment {
    public static String key = "";

    public static void setName(String name) {
        FragmentAddDict.name = name;
    }

    public static String name = "";
    public static String comps = "";
    public static String result = "";
    public static int id = 0;

    private Dict dictActivity;
    private static LinkedList<Comp> compList;
    private CompAdapter compAdapter;
    private FragmentAddDictBinding binding;
    private DictViewModel dictViewModel;
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dictViewModel = new ViewModelProvider(this).get(DictViewModel.class);
        if (DictViewModel.dict != null) {
            dictActivity = DictViewModel.dict;
        }
        compList = new LinkedList<>();
        compAdapter = new CompAdapter();
        binding.recyclerViewComp.setAdapter(compAdapter);
        binding.recyclerViewComp.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.etInputDictionaryName.setText(dictActivity.getName());
        compList = (LinkedList<Comp>) toArray(dictActivity.getComps());
        showList();
        createTextWatcher();
        addComps();
        changeComp();
        createItemTouchHelper();


        binding.btnSave.setOnClickListener(v -> {
            if (key.equals("add")) {
                FragmentRecView.viewModel.add(dictActivity);
            }
            else {
                FragmentRecView.viewModel.update(dictActivity);
            }
            DictViewModel.setCreatingDict(false);
            dictViewModel.setDict(null);
            MainActivity.key = "";
            requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();


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
                showList();
                binding.etInputWord.setText("");
                binding.etInputTranslation.setText("");
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
                        showList();
                        binding.etInputWord.setText("");
                        binding.etInputTranslation.setText("");
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
        dictViewModel.setDict(dictActivity);
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