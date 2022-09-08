package com.np.namasteyoga.ui.feedback.feedbackRating

import android.app.AlertDialog
import android.os.Bundle
import androidx.core.view.isVisible
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FeedbackSubmitRequest
import com.np.namasteyoga.datasource.request.Question
import com.np.namasteyoga.datasource.response.FeedbackQuestionsOption
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_feedback_rating.*
import kotlinx.android.synthetic.main.button_colored_layout_without_round.*
import org.jetbrains.anko.toast

class FeedbackRatingActivity :
    BaseActivity<FeedbackRatingViewModel>(FeedbackRatingViewModel::class) {

    private var selectedList: ArrayList<FeedbackQuestionsOption>? = null
    private val sendData = ArrayList<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIUtils.setStatusBarGradient(this, R.color.white)

        intent?.run {
            if (hasExtra(IntentUtils.SHARE_LIST)) {
                selectedList = getParcelableArrayListExtra(IntentUtils.SHARE_LIST)

                selectedList?.run {
                    forEach {
                        val question = Question(it.feedback_questions_id, it.id.toString())
                        sendData.add(question)
                    }
                }
            }
        }
        titleDes.text = getString(R.string.tell_us_about_a_bit_more_about_why_you_choose_4_star, 0)
        buttonColorLayoutTxt.setText(R.string.submit)

        ratingView.setOnRatingBarChangeListener { _, value, _ ->
            val rating = value.toInt()
            titleDes.text = getString(R.string.tell_us_about_a_bit_more_about_why_you_choose_4_star, rating)
//            if (rating == 5) {
//                isShowDes(false)
//            } else {
//                isShowDes(true)
//            }
        }

        buttonColorLayout.setOnClickListener {

//            if (description.isVisible) {
//
//                if (PatternUtil.isNullOrEmpty(description.text.toString())) {
//                    TextUtils.errorEmptyText(description, getString(R.string.brief_description))
//                    return@setOnClickListener
//                }
//                if (PatternUtil.isValidAddress(description.text.toString())) {
//                    TextUtils.lengthValidation(description, getString(R.string.brief_description))
//                    return@setOnClickListener
//                }
//
//            }
            if (ratingView.rating.toInt()<CommonInt.One){
                toast(R.string.please_select_rating)
                return@setOnClickListener
            }
            val submitRequest = FeedbackSubmitRequest().apply {
                users_id = model.userDetails.value?.id.toString()
                rating = ratingView.rating.toInt().toString()
                rating_description = description.text.toString().trim()
                questions = sendData
            }
            getFeedbackList(submitRequest)
        }

        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            feedbackPopup(it)
//            try {
//                val status = (it.status ?: C.NP_STATUS_FAIL)
//                if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
//                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
//                    setResult(RESULT_OK)
//                    finish()
//                    return@observe
//                }
//
//                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
//                    toast(it.message ?: getString(R.string._successfully))
//                    setResult(RESULT_OK)
//                    finishAfterTransition()
//                } else {
//                    toast(it.message ?: getString(R.string.something_went_wrong))
//                }



//            } catch (e: Exception) {
//                toast(R.string.something_went_wrong)
//                if (C.DEBUG)
//                    e.printStackTrace()
//            }
        })
    }

    private fun isShowDes(boolean: Boolean) {
        if (boolean) {
            description.show()
            titleDes.show()
            dividerBottom.show()
        } else {
            description.hide()
            titleDes.hide()
            dividerBottom.hide()
        }
    }

    private fun feedbackPopup(it: BaseResponseNamasteYoga<EmptyResponse>?) {
        val alertBuilder = AlertDialog.Builder(context, R.style.MyAppTheme)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(R.string.empty)
        alertBuilder.setMessage(it?.message ?: getString(R.string.something_went_wrong))
        alertBuilder.setPositiveButton(
            R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            try {
                val status = (it?.status ?: C.NP_STATUS_FAIL)
                if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                    setResult(RESULT_OK)
                    finish()
                }
                if ((it?.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    setResult(RESULT_OK)
                    finishAfterTransition()

                }

            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun getFeedbackList(submitRequest: FeedbackSubmitRequest) {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.submitFeedback(submitRequest)
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_feedback_rating

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "FeedbackRatingActivity"
    }

    override val title: String
        get() = getString(R.string.overall_feedback)
    override val isShowTitle: Boolean
        get() = true
}