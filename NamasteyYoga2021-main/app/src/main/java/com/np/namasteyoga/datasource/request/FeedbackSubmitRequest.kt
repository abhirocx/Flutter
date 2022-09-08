package com.np.namasteyoga.datasource.request

data class FeedbackSubmitRequest(
    var questions: List<Question>? = null,
    var rating: String? = null,
    var rating_description: String? = null,
    var users_id: String? = null
)