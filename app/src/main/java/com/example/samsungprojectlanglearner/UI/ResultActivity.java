package com.example.samsungprojectlanglearner.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.samsungprojectlanglearner.data.Dict.Dict;
import com.example.samsungprojectlanglearner.databinding.ActivityResultBinding;

import java.util.ArrayList;
import java.util.LinkedList;

public class ResultActivity extends AppCompatActivity {
    public static ActivityResultBinding binding;
    private int result;
    private LinkedList<ResultItem> testList = new LinkedList<>();
    private ArrayList<String> tempList = new ArrayList<>();
    public static ResultAdapter resultAdapter = new ResultAdapter();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tempList = new ArrayList<>();
        tempList = (ArrayList<String>) getIntent().getSerializableExtra("List");
        for (int i = 0; i < tempList.size(); i++)  {
            testList.add(ResultItem.fromString(tempList.get(i)));
        }

        Dict dict = MainActivity.adapter.getDictList().get(position);
        position = getIntent().getIntExtra("Position", 0);
        result = getIntent().getIntExtra("Result", 0);

        updateProgressBar();
        ResultActivity.binding.recyclerViewWrongAns.setAdapter(ResultActivity.resultAdapter);
        ResultActivity.resultAdapter.setList(testList);
        if (testList.isEmpty()) {
            binding.tvWrongAnswers.setText("No mistakes! Congratulations!");
        }

        dict.setResult(String.valueOf(result));
        MainActivity.viewModel.update(dict);


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    private void updateProgressBar() {
        binding.progressBar.setProgress(Integer.parseInt(String.valueOf(result)));
        binding.textViewProgress.setText(String.valueOf(result)+"%");
    }
    public static  Intent newIntent(
            Context context,
            int position,
            LinkedList<String> list,
            int result
    ){
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("Position", position);
        intent.putExtra("List", list);
        intent.putExtra("Result", result);
        return intent;
    }
    public static  Intent newIntent(
            Context context,
            int position,
            int result
    ){
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("Position", position);
        intent.putExtra("Result", result);
        return intent;
    }
}