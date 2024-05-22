package com.example.samsungprojectlanglearner.UI.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.samsungprojectlanglearner.data.Dict.Dict;

public class DictViewModel extends ViewModel {
    public static Dict dict;

    public void setDict(Dict dict) {
        DictViewModel.dict = dict;
    }
    public static boolean creatingDict;

    public static boolean isCreatingDict() {
        return creatingDict;
    }

    public static void setCreatingDict(boolean creatingDict) {
        DictViewModel.creatingDict = creatingDict;
    }
}
