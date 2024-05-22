package com.example.samsungprojectlanglearner.UI.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.samsungprojectlanglearner.UI.ResultItem;

import java.util.LinkedList;

public class ResultViewModel extends ViewModel {
    public static String result;
    public static LinkedList<ResultItem> resultItemLinkedList;

    public static String getResult() {
        return result;
    }

    public static void setResult(String result) {
        ResultViewModel.result = result;
    }

    public static LinkedList<ResultItem> getResultItemLinkedList() {
        return resultItemLinkedList;
    }

    public static void setResultItemLinkedList(LinkedList<ResultItem> resultItemLinkedList) {
        ResultViewModel.resultItemLinkedList = resultItemLinkedList;
    }
}
