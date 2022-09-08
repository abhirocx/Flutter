package com.np.namasteyoga.ui.feedback.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.response.FeedbackItemModel
import com.np.namasteyoga.interfaces.PaginationListener
import org.jetbrains.anko.find

class FeedbackListAdapter(
    private val list: ArrayList<FeedbackItemModel>?,
    private val paginationListener: PaginationListener
) :
    RecyclerView.Adapter<FeedbackListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtQuestionNo = view.find<TextView>(R.id.txtQuestionNo)
        val txtQuestion = view.find<TextView>(R.id.txtQuestion)
        val options = view.find<RadioGroup>(R.id.options)
        val first = view.find<RadioButton>(R.id.first)
        val sec = view.find<RadioButton>(R.id.sec)
        val third = view.find<RadioButton>(R.id.third)
        val four = view.find<RadioButton>(R.id.four)

        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.feedback_item_layout, parent, CommonBoolean.FALSE)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return



        holder.run {
            txtQuestionNo.text = itemView.context.getString(R.string.question_d,(adapterPosition+1))
            txtQuestion.text = item.question
            first.text = item.feedback_questions_options?.get(0)?.options
            first.tag = item.feedback_questions_options?.get(0)
            sec.text = item.feedback_questions_options?.get(1)?.options
            sec.tag = item.feedback_questions_options?.get(1)
            third.text = item.feedback_questions_options?.get(2)?.options
            third.tag = item.feedback_questions_options?.get(2)
            four.text = item.feedback_questions_options?.get(3)?.options
            four.tag = item.feedback_questions_options?.get(3)
        }

//        if ((position + 1) >= list.size)
//            paginationListener.page()

    }

    override fun getItemCount(): Int = list?.size ?: CommonInt.Zero

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}