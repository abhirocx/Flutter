package com.np.namasteyoga.ui.feedback.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.response.FeedbackItemModel
import org.jetbrains.anko.find

class FeedbackListBaseAdapter(private val list: ArrayList<FeedbackItemModel>?,private val context: Context):BaseAdapter() {

    override fun getCount(): Int =  list?.size ?: CommonInt.Zero

    override fun getItem(p0: Int): FeedbackItemModel? = list?.get(p0)

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
          val  view = LayoutInflater.from(context).inflate(R.layout.feedback_item_layout, parent, false)


        getItem(position)?.run {
            if (view!=null) {

                view.find<TextView>(R.id.txtQuestionNo).text =
                    context.getString(R.string.question_d, (position + 1))
                view.find<TextView>(R.id.txtQuestion).text = question
                view.find<RadioButton>(R.id.first).run {
                    text = feedback_questions_options?.get(0)?.options
                    tag = feedback_questions_options?.get(0)
                }
                view.find<RadioButton>(R.id.sec).run {
                    text = feedback_questions_options?.get(1)?.options
                    tag = feedback_questions_options?.get(1)
                }
                view.find<RadioButton>(R.id.third).run {
                    text = feedback_questions_options?.get(2)?.options
                    tag = feedback_questions_options?.get(2)
                }
                view.find<RadioButton>(R.id.four).run {
                    text = feedback_questions_options?.get(3)?.options
                    tag = feedback_questions_options?.get(3)
                }

            }
        }

        return view
    }
}