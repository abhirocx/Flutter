package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PollData {
    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("question_data")
    @Expose
    private List<PollQuestionDatum> questionData = null;

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public List<PollQuestionDatum> getQuestionData() {
        return questionData;
    }

    public void setQuestionData(List<PollQuestionDatum> questionData) {
        this.questionData = questionData;
    }

}


