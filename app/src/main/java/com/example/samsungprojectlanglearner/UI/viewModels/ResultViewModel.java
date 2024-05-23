package com.example.samsungprojectlanglearner.UI.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samsungprojectlanglearner.UI.ResultItem;
import com.example.samsungprojectlanglearner.UI.activities.ActivityStudy;

import java.util.LinkedList;

public class ResultViewModel extends ViewModel {
//    public static String result;
//    public static LinkedList<ResultItem> resultItemLinkedList;
//
//    public static String getResult() {
//        return result;
//    }
//
//    public static void setResult(String result) {
//        ResultViewModel.result = result;
//    }
//
//    public static LinkedList<ResultItem> getResultItemLinkedList() {
//        return resultItemLinkedList;
//    }
//
//    public static void setResultItemLinkedList(LinkedList<ResultItem> resultItemLinkedList) {
//        ResultViewModel.resultItemLinkedList = resultItemLinkedList;
//    }
    private static final MutableLiveData<String> result = new MutableLiveData<>();
    private static final MutableLiveData<LinkedList<ResultItem>> resultItemLinkedList = new MutableLiveData<>();

    public MutableLiveData<String> getResult() {
        return result;
    }

    public MutableLiveData<LinkedList<ResultItem>> getResultItemLinkedList() {
        return resultItemLinkedList;
    }
    public static void setResult() {
        result.setValue(ActivityStudy.result);
    }
    public static void setResultItemLinkedList() {
        resultItemLinkedList.setValue(ActivityStudy.resultItemLinkedList);
    }
}
