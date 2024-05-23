package com.example.samsungprojectlanglearner.UI.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.samsungprojectlanglearner.UI.ResultAdapter;
import com.example.samsungprojectlanglearner.UI.ResultItem;
import com.example.samsungprojectlanglearner.UI.fragments.FragmentRecView;
import com.example.samsungprojectlanglearner.UI.viewModels.DictViewModel;
import com.example.samsungprojectlanglearner.UI.viewModels.ResultViewModel;
import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.databinding.ActivityResultBinding;

import java.util.LinkedList;

public class ResultActivity extends AppCompatActivity {
    public ActivityResultBinding binding;
    private String result;
    public static ResultAdapter resultAdapter = new ResultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dict dict = DictViewModel.dict;
        result = ResultViewModel.getResult();
        updateProgressBar();
        binding.recyclerViewWrongAns.setAdapter(ResultActivity.resultAdapter);
        LinkedList<ResultItem> thisResultList = ResultViewModel.getResultItemLinkedList();
        ResultActivity.resultAdapter.setList(thisResultList);
        if (thisResultList.isEmpty()) {
            binding.tvWrongAnswers.setText("No mistakes! Congratulations!");
        }
        dict.setResult(String.valueOf(result));
        DictViewModel.dict.setResult(result);
        FragmentRecView.viewModel.update(dict);

        binding.btnBack.setOnClickListener(v -> {
            DictViewModel.dict = null;
            binding.progressBar.setProgress(0);
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        });

    }
    private void updateProgressBar() {
        binding.progressBar.setProgress(Integer.parseInt(result));
        String text = result + "%";
        binding.textViewProgress.setText(text);
    }
}