package com.np.namasteyoga.datasource.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionDatum {

    @SerializedName("que_id")
    @Expose
    private Integer queId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("ans_data")
    @Expose
    private List<AnsDatum> ansData = null;


    public Integer getQueId() {
        return queId;
    }

    public void setQueId(Integer queId) {
        this.queId = queId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnsDatum> getAnsData() {
        return ansData;
    }

    public void setAnsData(List<AnsDatum> ansData) {
        this.ansData = ansData;
    }

}
