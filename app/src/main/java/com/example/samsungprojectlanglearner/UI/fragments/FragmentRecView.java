package com.example.samsungprojectlanglearner.UI.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.samsungprojectlanglearner.UI.activities.ActivityAddDict;
import com.example.samsungprojectlanglearner.UI.activities.ActivityStudy;
import com.example.samsungprojectlanglearner.UI.viewModels.DictViewModel;
import com.example.samsungprojectlanglearner.UI.activities.MainActivity;
import com.example.samsungprojectlanglearner.UI.viewModels.MainViewModel;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.data.Dict.DictAdapter;
import com.example.samsungprojectlanglearner.databinding.FragmentRecViewBinding;

import java.util.LinkedList;
import java.util.List;

public class FragmentRecView extends Fragment {
    private FragmentRecViewBinding binding;
    public static DictAdapter adapter;
    public static List<Dict> dictList;
    public static MainViewModel viewModel;
    private static DictViewModel dictViewModel;
    public FragmentRecView() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentRecViewBinding.inflate(getLayoutInflater());
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRecViewBinding.bind(view);
        dictList = new LinkedList<>();
        dictViewModel = new ViewModelProvider(this).get(DictViewModel.class);
        createViewModel();
        createRecyclerView();
        createDictItemClickListener();
        createItemTouchHelper();
        createTextChangedListener();
        binding.btnAddDictionary.setOnClickListener(v -> {
            Dict dict = new Dict("", "", "");
            dictViewModel.setDict(dict);
            DictViewModel.setCreatingDict(true);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Intent intent = new Intent(getContext(), ActivityAddDict.class);
                ActivityAddDict.key = "add";
                startActivity(intent);
            }
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                MainActivity.startAddDictFragment();
                FragmentAddDict.key = "add";
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecViewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    private void createDictItemClickListener() {
        adapter.setDictItemClickListener(new DictAdapter.DictItemClickListener() {
            @Override
            public void onClick(int position) {
                Dict dict = adapter.getDictList().get(position);
                new AlertDialog.Builder(getContext())
                        .setMessage("Choose activity")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dictViewModel.setDict(dict);
                                ActivityAddDict.key = "edit";
                                DictViewModel.setCreatingDict(true);

                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    Intent intent = new Intent(getContext(), ActivityAddDict.class);
                                    startActivity(intent);
                                }
                                else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                    MainActivity.startAddDictFragment();
                                    FragmentAddDict.key = "edit";
                                }
                            }
                        })
                        .setNeutralButton("Study dict", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = ActivityStudy.newIntent(getContext(),
//                                        dict.getComps(),
//                                        position);
                                DictViewModel.dict = dict;
                                startActivity(new Intent(getContext(), ActivityStudy.class));
                            }
                        })
                        .show();
            }
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

                new AlertDialog.Builder(getContext())
                        .setMessage("Delete dictionary " + dict.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            viewModel.remove(dict);
                            Toast.makeText(getContext(),
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
    }
    private void createViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getDicts().observe(getViewLifecycleOwner(), new Observer<List<Dict>>() {
            @Override
            public void onChanged(List<Dict> dicts) {
                dictList = dicts;
                adapter.setDictList(dicts);
                binding.etSearchDict.setText("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshList();
    }
}