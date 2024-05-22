package com.example.samsungprojectlanglearner.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    public static ActivityResultBinding binding;
    private String result;
    private LinkedList<ResultItem> thisResultList = new LinkedList<>();
    public static ResultAdapter resultAdapter = new ResultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dict dict = DictViewModel.dict;
        result = ResultViewModel.getResult();
        updateProgressBar();
        ResultActivity.binding.recyclerViewWrongAns.setAdapter(ResultActivity.resultAdapter);
        thisResultList = ResultViewModel.getResultItemLinkedList();
        ResultActivity.resultAdapter.setList(thisResultList);
        if (thisResultList.isEmpty()) {
            binding.tvWrongAnswers.setText("No mistakes! Congratulations!");
        }
        dict.setResult(String.valueOf(result));
        DictViewModel.dict.setResult(result);
        FragmentRecView.viewModel.update(dict);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DictViewModel.dict = null;
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finish();
            }
        });

    }
    private void updateProgressBar() {
        binding.progressBar.setProgress(Integer.parseInt(result));
        binding.textViewProgress.setText(result+"%");
    }
}