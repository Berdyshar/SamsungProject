package com.example.samsungprojectlanglearner.data.Comp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.databinding.CompItemBinding;

import java.util.LinkedList;
import java.util.List;

public class CompAdapter extends RecyclerView.Adapter<CompAdapter.CompViewHolder> {

    public interface CompItemClickListener {
         void onClick(int position);
    }

    private CompItemClickListener compItemClickListener;
    public void setCompItemClickListener(CompItemClickListener compItemClickListener) {
        this.compItemClickListener = compItemClickListener;
    }

    public List<Comp> getCompList() {
        return compList;
    }

    public void setCompList(List<Comp> compList) {
        this.compList = compList;
        notifyDataSetChanged();
    }

    private List<Comp> compList = new LinkedList<>();
    @NonNull
    @Override
    public CompViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_item, parent, false);

        return new CompViewHolder(CompItemBinding.bind(view));
    }

    @Override
    public void onBindViewHolder(@NonNull CompViewHolder holder, int position) {
        Comp comp = compList.get(position);
        holder.binding.tvWord.setText(comp.getWord());
        holder.binding.tvTranslation.setText(comp.getTranslation());
        holder.binding.getRoot().setOnClickListener(v -> compItemClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return compList.size();
    }

    public static class CompViewHolder extends RecyclerView.ViewHolder {
        CompItemBinding binding;
        public CompViewHolder(CompItemBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
