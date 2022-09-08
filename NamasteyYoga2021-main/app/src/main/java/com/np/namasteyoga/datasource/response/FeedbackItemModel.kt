package com.np.namasteyoga.datasource.response

data class FeedbackItemModel(
    val feedback_questions_options: List<FeedbackQuestionsOption>? = null,
    val id: Int? = null,
    val question: String? = null,
    var resourceId : Int?=null
)