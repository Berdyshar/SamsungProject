package com.example.samsungprojectlanglearner.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.data.Comp.CompList;
import com.example.samsungprojectlanglearner.databinding.ActivityStudyBinding;

import java.util.LinkedList;
import java.util.Random;


public class ActivityStudy extends AppCompatActivity {
    private ActivityStudyBinding binding;
    private LinkedList<Comp> compList;
    private Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        random = new Random();
        compList = (LinkedList<Comp>) CompList.toArray(getIntent().getStringExtra("COMPS"));
        if (compList.isEmpty()) {
            Toast.makeText(this, "Sorry, but you can't study empty dict", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            test(compList);
        }


    }

    private void test(LinkedList<Comp> list) {
        int number = random.nextInt(list.size());
        Comp comp = compList.get(number);
        binding.tvQuestion.setText(comp.getWord());
        binding.btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(binding.etInputTranslationStudy.getText()).toLowerCase().equals(comp.getTranslation().toLowerCase())) {
                    Toast.makeText(ActivityStudy.this, "Right!", Toast.LENGTH_SHORT).show();
                    list.remove(number);

                }
                else {
                    Toast.makeText(ActivityStudy.this, "Wrong(   Right answer was "+comp.getTranslation(), Toast.LENGTH_SHORT).show();
                    list.remove(number);
                }
                if (list.size() == 0) {
                    startActivity(new Intent(ActivityStudy.this, ResultActivity.class));
                    return ;
                }
                else {
                    test(list);
                }
                binding.etInputTranslationStudy.setText("");
            }
        });
    }

    public static Intent newIntent(Context context, String comps) {
        Intent intent = new Intent(context, ActivityStudy.class);
        intent.putExtra("COMPS", comps);
        return intent;
    }
}