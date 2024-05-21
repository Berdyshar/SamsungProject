package com.example.samsungprojectlanglearner.UI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samsungprojectlanglearner.data.Dict.Dict;

public class AddDictViewModel extends ViewModel {
    //  private  MutableLiveData<Dict> dict = new MutableLiveData<>();
//    private Dict dict = new Dict("","","");
//
//
//    public Dict getDict() {
//        return dict;
//    }
//
//    public void setDict(Dict dict) {
//        this.dict=dict;
//    }
    private static String name;
    private static String Comps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        AddDictViewModel.name = name;
    }

    public String getComps() {
        return Comps;
    }

    public void setComps(String comps) {
        Comps = comps;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        AddDictViewModel.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        AddDictViewModel.id = id;
    }

    private static String result;
    private static  int id;

}
