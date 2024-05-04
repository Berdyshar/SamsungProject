package com.example.samsungprojectlanglearner.data.Dict;

import static com.example.samsungprojectlanglearner.data.Comp.CompList.toArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungprojectlanglearner.R;
import com.example.samsungprojectlanglearner.data.Comp.CompList;
import com.example.samsungprojectlanglearner.databinding.DictionaryItemBinding;

import java.util.LinkedList;
import java.util.List;

public class DictAdapter extends RecyclerView.Adapter<DictAdapter.MyViewHolder> {
    public interface DictItemClickListener {
        void onClick(int position);
    }
    public DictItemClickListener dictItemClickListener;
    public void setDictItemClickListener(DictItemClickListener dictItemClickListener) {
        this.dictItemClickListener = dictItemClickListener;
    }






    public void setDictList(List<Dict> dictList) {
        this.dictList = dictList;
        notifyDataSetChanged();
    }

    public List<Dict> getDictList() {
        return dictList;
    }

    private List<Dict> dictList = new LinkedList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.dictionary_item, parent, false);

        return new MyViewHolder(DictionaryItemBinding.bind(view));
    }





    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Dict dict = dictList.get(position);
        holder.binding.tvDictionaryName.setText(dict.getName());
        String key = "";
        if (toArray(dict.getComps()).size()%10==1) {
            key = "item";
        } else {
            key = "items";
        }
        holder.binding.tvDictSize.setText((dict.getComps().isEmpty()? "0 "+key:String.valueOf(toArray(dict.getComps()).size()) + " " + key));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dictItemClickListener.onClick(position);
            }
        }); {

        }
    }





    @Override
    public int getItemCount() {
        return dictList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        DictionaryItemBinding binding;
        public MyViewHolder(DictionaryItemBinding bin) {
            super(bin.getRoot());
            binding = bin;
        }
    }
}
