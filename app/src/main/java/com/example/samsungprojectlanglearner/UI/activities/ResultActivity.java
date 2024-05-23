package com.example.samsungprojectlanglearner.UI.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.samsungprojectlanglearner.UI.ResultAdapter;
import com.example.samsungprojectlanglearner.UI.ResultItem;
import com.example.samsungprojectlanglearner.UI.fragments.FragmentRecView;
import com.example.samsungprojectlanglearner.UI.viewModels.DictViewModel;
import com.example.samsungprojectlanglearner.UI.viewModels.ResultViewModel;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.databinding.ActivityResultBinding;

import java.util.LinkedList;

public class ResultActivity extends AppCompatActivity implements LifecycleOwner {
    public ActivityResultBinding binding;
    private String result;
    public static ResultAdapter resultAdapter = new ResultAdapter();
    public static ResultViewModel resultViewModel;
    private LinkedList<ResultItem> thisResultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Dict dict = DictViewModel.dict;
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        resultViewModel.getResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                result = s;
                updateProgressBar();
                dict.setResult(String.valueOf(result));
                DictViewModel.dict.setResult(result);
                FragmentRecView.viewModel.update(dict);
            }
        });
        resultViewModel.getResultItemLinkedList().observe(this, new Observer<LinkedList<ResultItem>>() {
            @Override
            public void onChanged(LinkedList<ResultItem> resultItems) {
                thisResultList = resultItems;
                ResultActivity.resultAdapter.setList(thisResultList);
                if (thisResultList.isEmpty()) {
                    binding.tvWrongAnswers.setText("No mistakes! Congratulations!");
                }
            }
        });
    //    result = ResultViewModel.getResult();
        binding.recyclerViewWrongAns.setAdapter(ResultActivity.resultAdapter);
      //  LinkedList<ResultItem> thisResultList = ResultViewModel.getResultItemLinkedList();



        binding.btnBack.setOnClickListener(v -> {
            DictViewModel.dict = null;
            binding.progressBar.setProgress(0);
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            MainActivity.key="";
            DictViewModel.setCreatingDict(false);
            DictViewModel.setDict(null);
            finish();
        });

    }
    private void updateProgressBar() {
        binding.progressBar.setProgress(Integer.parseInt(result));
        String text = result + "%";
        binding.textViewProgress.setText(text);
    }
}