package com.np.namasteyoga.datasource.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedbackQuestionsOption(
    val feedback_questions_id: Int? = null,
    val id: Int? = null,
    val options: String? = null
):Parcelable