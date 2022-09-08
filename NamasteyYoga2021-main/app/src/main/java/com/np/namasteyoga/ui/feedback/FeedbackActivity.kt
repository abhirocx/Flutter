package com.np.namasteyoga.ui.feedback

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FeedbackSubmitRequest
import com.np.namasteyoga.datasource.request.Question
import com.np.namasteyoga.datasource.response.FeedbackItemModel
import com.np.namasteyoga.datasource.response.FeedbackQuestionsOption
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.ui.feedback.adapter.FeedbackListBaseAdapter
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_feedback.description
import kotlinx.android.synthetic.main.activity_feedback.ratingView
import kotlinx.android.synthetic.main.button_colored_layout_without_round.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


class FeedbackActivity : BaseActivity<FeedbackViewModel>(FeedbackViewModel::class),
    PaginationListener {

    private val list = ArrayList<FeedbackItemModel>()
    private val selectedList = ArrayList<FeedbackQuestionsOption>()
    private val adapter by lazy { FeedbackListBaseAdapter(list, context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIUtils.setStatusBarGradient(this, R.color.white)


//        recyclerView.run {
//            layoutManager = LinearLayoutManager(this@FeedbackActivity)
//            adapter = FeedbackListAdapter(list,this@FeedbackActivity)
//        }
//        recyclerView.adapter = adapter
//
//        recyclerView.onItemClickListener =
//            OnItemClickListener { parent, viewClicked, position, id ->
//
//                list[position].resourceId =
//                    viewClicked.find<RadioGroup>(R.id.options).checkedRadioButtonId
//                adapter.notifyDataSetChanged()
//            }

        buttonColorLayout.setOnClickListener {
            var isAllValid = false
            selectedList.clear()



            for (i in 0 until list.size) {
                recyclerView.getChildAt(i)?.run {
                    val group = find<RadioGroup>(R.id.options)
                    if (group.checkedRadioButtonId == CommonInt.MinusOne) {
                        isAllValid = true
                    } else {
                        val view = find<RadioButton>(group.checkedRadioButtonId)
                        (view.tag as FeedbackQuestionsOption?)?.run {
                            selectedList.add(this)
                        }
                    }
                }
            }


            //            for (i in 0 until recyclerView.childCount ) {
//                ( recyclerView.findViewHolderForAdapterPosition(i) as FeedbackListAdapter.MyViewHolder).run {
//                   if(options.checkedRadioButtonId == CommonInt.MinusOne){
//                       isAllValid = true
//                   }else{
//
//                       val view = options.find<RadioButton>(options.checkedRadioButtonId)
//                       (view.tag as FeedbackQuestionsOption?)?.run {
//                           selectedList.add(this)
//                       }
//                   }
//
//                }
//            }

            if (isAllValid) {
                toast(R.string.feedback_validation_msg)
                return@setOnClickListener
            }

            if (ratingView.rating.toInt() < CommonInt.One) {
                toast(R.string.please_select_rating)
                return@setOnClickListener
            }
            val sendData = ArrayList<Question>()
            selectedList.forEach {
                val question = Question(it.feedback_questions_id, it.id.toString())
                sendData.add(question)
            }

            val submitRequest = FeedbackSubmitRequest().apply {
                users_id = model.userDetails.value?.id.toString()
                rating = ratingView.rating.toInt().toString()
                rating_description = description.text.toString().trim()
                questions = sendData
            }
            getFeedbackList(submitRequest)
//            val intent = Intent(this, FeedbackRatingActivity::class.java)
//            intent.putParcelableArrayListExtra(IntentUtils.SHARE_LIST, selectedList)
//            startActivityForResult(intent, 100)


        }
//        buttonColorLayoutTxt.setText(R.string.next)
        buttonColorLayoutTxt.setText(R.string.submit)
        getFeedbackList()

        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }

            try {
                val status = (it.status ?: C.NP_STATUS_FAIL)
                if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                    setResult(RESULT_OK)
                    finish()
                    return@observe
                }

                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.run {
                            list.addAll(this)
                        }
                    }
                    addViews()
//                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
        model.response2.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            feedbackPopup(it)
        })

        starDetails.setOnClickListener {
            hideKeyboard()
        }
        recyclerView.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun addViews() {
        val inflater = LayoutInflater.from(context)
        for (i in 0 until list.size) {
            val layout = inflater.inflate(R.layout.feedback_item_layout, null, false) as View
            layout.run {
                id = i

                find<TextView>(R.id.txtQuestionNo).text =
                    context.getString(R.string.question_d, (i + 1))
                find<TextView>(R.id.txtQuestion).text = list[i].question
                find<RadioButton>(R.id.first).run {
                    text = list[i].feedback_questions_options?.get(0)?.options
                    tag = list[i].feedback_questions_options?.get(0)
                }
                find<RadioButton>(R.id.sec).run {
                    text = list[i].feedback_questions_options?.get(1)?.options
                    tag = list[i].feedback_questions_options?.get(1)
                }
                find<RadioButton>(R.id.third).run {
                    text = list[i].feedback_questions_options?.get(2)?.options
                    tag = list[i].feedback_questions_options?.get(2)
                }
                find<RadioButton>(R.id.four).run {
                    text = list[i].feedback_questions_options?.get(3)?.options
                    tag = list[i].feedback_questions_options?.get(3)
                }
            }

            recyclerView.addView(layout)
        }
        starDetails.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finishAfterTransition()
        }
    }

    private fun getFeedbackList() {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.getFeedbackList()
        } else
            toast(R.string.no_internet_connection)
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

    override fun layout(): Int = R.layout.activity_feedback

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "FeedbackActivity"
    }

    override val title: String
        get() = getString(R.string.feedback)
    override val isShowTitle: Boolean
        get() = true

    override fun page() {
        getFeedbackList()
    }
}