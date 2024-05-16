package com.example.samsungprojectlanglearner.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.data.Comp.Comp;
import com.example.samsungprojectlanglearner.databinding.CompItemBinding;
import com.example.samsungprojectlanglearner.databinding.ResultItemBinding;

import java.util.LinkedList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {
    private List<ResultItem> wrongList = new LinkedList<>();
    public void setList(LinkedList<ResultItem> list) {
        wrongList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);

        return new ResultAdapter.ResultHolder(ResultItemBinding.bind(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        ResultItem comp = wrongList.get(position);
        holder.binding.tvQuestion.setText(comp.getWord());
        holder.binding.tvRight.setText(comp.getRightAnswer());
        holder.binding.tvWrong.setText(comp.getWrongAnswer());
    }

    @Override
    public int getItemCount() {
        return wrongList.size();
    }

    public class ResultHolder extends RecyclerView.ViewHolder {
        ResultItemBinding binding;
        public ResultHolder(ResultItemBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
