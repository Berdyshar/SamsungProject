package com.example.samsungprojectlanglearner.UI;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ResultItem implements Serializable {
    String word;

    @NonNull
    @Override
    public String toString() {
        return "ResultItem{" +
                "word='" + word + '\'' +
                ", rightAnswer='" + rightAnswer + '\'' +
                ", wrongAnswer='" + wrongAnswer + '\'' +
                '}';
    }

    String rightAnswer;
    String wrongAnswer;

    public ResultItem(String word, String rightAnswer, String wrongAnswer) {
        this.word = word;
        this.rightAnswer = rightAnswer;
        this.wrongAnswer = wrongAnswer;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }
}
