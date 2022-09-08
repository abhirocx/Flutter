package com.np.namasteyoga.datasource.response

data class FeedbackResponseModel(
    val `data`: List<FeedbackItemModel>? = null,
    val message: String? = null,
    val status: String? = null,
    val total_count: Int? = null
)