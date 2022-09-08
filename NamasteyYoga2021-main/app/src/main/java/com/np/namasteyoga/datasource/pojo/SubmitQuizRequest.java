package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitQuizRequest {

    @SerializedName("data")
    @Expose
    private QuizData data;

    public QuizData getData() {
        return data;
    }

    public void setData(QuizData data) {
        this.data = data;
    }


}
