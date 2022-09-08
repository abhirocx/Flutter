package com.np.namasteyoga.ui.health.history.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.StepHistoryModel
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.utils.DateUtils
import com.np.namasteyoga.utils.Util
import com.np.namasteyoga.utils.invisible
import com.np.namasteyoga.utils.show
import org.jetbrains.anko.find


class StepHistoryAdapter(
        private val locations: List<StepHistoryModel>?,
        private val itemClickListener: OnClickItem<StepHistoryModel>,
        private val goalCount:Int=CommonInt.GoalDefaultValue
) :
        RecyclerView.Adapter<StepHistoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = locations?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = locations?.get(position) ?: return

        item.run {
            if (steps>=goalCount){
                holder.trophy.show()
            }else{
                holder.trophy.invisible()
            }
            holder.txtSteps.text = "$steps"
            holder.txtCalories.text = "${Util.calculateStepToCal(steps)}"
            holder.txtDistance.text = Util.calculateStepToDistance(steps)
            holder.dateTxt.text = DateUtils.getDataString(holder.itemView.context.getString(R.string.history_item_date_format),date)

        }

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val trophy: ImageView = view.find(R.id.trophy)
        val dateTxt: TextView = view.find(R.id.dateTxt)
        val txtSteps: TextView = view.find(R.id.txtSteps)
        val txtCalories: TextView = view.find(R.id.txtCalories)
        val txtDistance: TextView = view.find(R.id.txtDistance)

        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(adapterPosition)
            }
        }

    }

}
