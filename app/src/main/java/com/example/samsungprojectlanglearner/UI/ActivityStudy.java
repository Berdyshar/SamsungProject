package com.example.samsungprojectlanglearner.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompList;
import com.example.samsungprojectlanglearner.databinding.ActivityStudyBinding;

import java.util.LinkedList;
import java.util.Random;


public class ActivityStudy extends AppCompatActivity {
    private Context context;
    AlertDialog dialog;
    private ActivityStudyBinding binding;
    private LinkedList<Comp> compList;
    private LinkedList<String> testList;

    private Random random;
    private int position;
    private int right = 0;
    private int wrong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        random = new Random();
        compList = (LinkedList<Comp>) CompList.toArray(getIntent().getStringExtra("COMPS"));
        testList = new LinkedList<String>();
        position = getIntent().getIntExtra("POSITION", 0);
        if (compList.isEmpty()) {
            Toast.makeText(this, "Sorry, but you can't study empty dict", Toast.LENGTH_LONG).show();
            finish();
        }
        test(compList);



    }


    private void test(LinkedList<Comp> list) {
        int number = random.nextInt(list.size());
        Comp comp = compList.get(number);
        binding.tvQuestion.setText(comp.getWord());

        binding.btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = String.valueOf(binding.etInputTranslationStudy.getText());
                if (str.isEmpty()) {
                    str = "No answer given";
                }
                if (str.toLowerCase().equals(comp.getTranslation().toLowerCase())) {
                    right++;
                    list.remove(number);
                    dialog = new AlertDialog.Builder(context)
                            .setView(R.layout.window_dialog_right)
                            .create();
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 2000);
                }
                else {
                    testList.add(ResultItem.createString(new ResultItem(
                            comp.getWord(),
                            comp.getTranslation(),
                            str))
                    );
                    wrong++;
                    list.remove(number);
                    dialog = new AlertDialog.Builder(context)
                            .setView(R.layout.window_dialog_wrong)
                            .create();
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1500);
                }
                if (list.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(ResultActivity.newIntent(
                                    ActivityStudy.this,
                                    position,
                                    testList,
                                    right * 100 / (right + wrong)));
                            finish();

                        }
                    }, 1500);
                    return ;
                }
                binding.etInputTranslationStudy.setText("");
                test(list);
            }


        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    public static Intent newIntent(Context context, String comps, int position) {
        Intent intent = new Intent(context, ActivityStudy.class);
        intent.putExtra("COMPS", comps);
        intent.putExtra("POSITION", position);
        return intent;
    }
}