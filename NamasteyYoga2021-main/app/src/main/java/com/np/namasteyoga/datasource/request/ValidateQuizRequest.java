package com.np.namasteyoga.datasource.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ValidateQuizRequest implements Serializable {
    @SerializedName("quiz_id")
    @Expose
    private Integer quiz_id;

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }
}
