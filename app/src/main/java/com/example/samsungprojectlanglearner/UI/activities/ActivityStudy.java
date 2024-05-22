package com.example.samsungprojectlanglearner.UI.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.UI.ResultItem;
import com.example.samsungprojectlanglearner.UI.fragments.FragmentRecView;
import com.example.samsungprojectlanglearner.UI.viewModels.DictViewModel;
import com.example.samsungprojectlanglearner.UI.viewModels.ResultViewModel;
import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompList;
import com.example.samsungprojectlanglearner.databinding.ActivityStudyBinding;

import java.util.LinkedList;
import java.util.Random;


public class ActivityStudy extends AppCompatActivity {
    private static Context context;
    private static AlertDialog dialog;
    private ActivityStudyBinding binding;
    private LinkedList<Comp> compList;
    private LinkedList<ResultItem> resultItemLinkedList;
    private Random random;

    private int right = 0;
    private int wrong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        random = new Random();
        compList = (LinkedList<Comp>) CompList.toArray(DictViewModel.dict.getComps());
        resultItemLinkedList = new LinkedList<>();
        if (compList.isEmpty()) {
            Toast.makeText(this, "Sorry, but you can't study empty dict", Toast.LENGTH_LONG).show();
            finish();
        }
        test(compList);
    }

    private void test(LinkedList<Comp> compList) {
        int number = random.nextInt(compList.size());
        Comp comp = compList.get(number);
        binding.tvQuestion.setText(comp.getWord());
        binding.btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = String.valueOf(binding.etInputTranslationStudy.getText());
                if (str.isEmpty()) {
                    str = "No answer given";
                }
                if (str.equalsIgnoreCase(comp.getTranslation())) {
                    right++;
                    showDialog("Right");
                }
                else {
                    ResultItem resultItem = new ResultItem(
                            comp.getWord(),
                            comp.getTranslation(),
                            str
                    );
                    resultItemLinkedList.add(resultItem);
                    wrong++;
                    showDialog("wrong");
                }
                compList.remove(number);
                binding.etInputTranslationStudy.setText("");
                if (compList.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ResultViewModel.setResult(String.valueOf(right * 100/ (right + wrong)));
                            ResultViewModel.setResultItemLinkedList(resultItemLinkedList);
                            startActivity(new Intent(ActivityStudy.this, ResultActivity.class));
                            finish();

                        }
                    }, 1500);
                    return;
                }
                test(compList);
            }
        });
    }

    private static void showDialog(String which) {
        if (which.equals("Right")) {
            dialog = new AlertDialog.Builder(context)
                    .setView(R.layout.window_dialog_right)
                    .create();
        }
        else {
            dialog = new AlertDialog.Builder(context)
                    .setView(R.layout.window_dialog_wrong)
                    .create();
        }
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}