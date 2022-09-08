package com.np.namasteyoga.ui.eventModule.adapter

import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.Rating
import com.np.namasteyoga.interfaces.Rating2
import com.np.namasteyoga.utils.DateUtils
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import com.np.namasteyoga.utils.UIUtils.getDistanceFormat
import com.np.namasteyoga.utils.hide
import com.np.namasteyoga.utils.show
import org.jetbrains.anko.find
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventListAdapter(
    private val list: ArrayList<Event>?,
    private val onClickItem: OnClickItem<Event>,
    private val sharedPreferences: SharedPreferences,
    private val isRate: Boolean = false,
    private val eventType: Int = 0,
//    private val ratingClick: Rating? = null,
    private val ratingClick: Rating2? = null,
    private val isSearch: Boolean = false

) :
    RecyclerView.Adapter<EventListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val body = view.find<View>(R.id.body)
        val name = view.find<TextView>(R.id.name)
        val accountType = view.find<TextView>(R.id.accountType)
        val txtDistance = view.find<TextView>(R.id.txtDistance)
        val txtDateTime = view.find<TextView>(R.id.txtDateTime)
        val rating = view.find<RatingBar>(R.id.rating)
        val rate = view.find<ImageView>(R.id.rate)
        val hilightView = view.find<View>(R.id.hilightView)

        init {
            body.setOnClickListener {
                onClickItem.onClick(eventType, list?.get(adapterPosition))
            }
            rate.setOnClickListener {
//                ratingClick?.ratingDialog("", adapterPosition)
                ratingClick?.ratingDialog(list?.get(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item_layout, parent, CommonBoolean.FALSE)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return

        holder.run {

            name.text = (item.eventName ?: CommonString.NA).getCapitalized()
            accountType.text = item.mode ?: CommonString.NA
            txtDateTime.text = if (item.startTime.isNullOrEmpty()) {
                CommonString.NA
            } else {
                val date =
                    DateUtils.getDateToString(item.startTime ?: CommonString.Empty)?.time ?: 0
                DateUtils.getDataString(
                    itemView.context.getString(R.string.hh_mm_a_dd_mmm_yyyy),
                    date
                )
            }
            rating.rating = item.rating
            val number3digits = if (item.nearest == CommonInt.One) {
                txtDistance.show()
                item.nearest_distance?.getDistanceFormat()
            } else {
                txtDistance.hide()
                CommonString.Empty
            }
//            txtDistance.text = itemView.context.getString(
//                R.string.s_km_away,
//                number3digits.toString()
//            )
            txtDistance.text = "${
                itemView.context.getString(
                    R.string.s_km_away,
                    number3digits.toString()
                )
            } ${item.cityName ?: CommonString.Empty}"
            if (item.isHighlight == "1") {
                hilightView.show()
                body.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.higlited_event_bg
                    )
                )
            } else {
                hilightView.hide()
                body.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.white
                    )
                )
            }
        }

        if (isRate) {
            holder.rating.show()
        } else {
            holder.rating.hide()
        }
        if (isSearch) {
            val isCurrent = convertStringToTimestamp(item.endTime) > Date().time
            if (isCurrent){
                holder.rating.hide()
            }else{
                holder.rating.show()
            }
        }

    }

    override fun getItemCount(): Int = list?.size ?: CommonInt.Zero

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItem(pos: Int): Event? {
        return list?.get(pos)
    }

    fun addItem(temp: ArrayList<Event>?) {
        temp?.run {
            list?.addAll(this)
        }
        notifyDataSetChanged()
    }

    private fun convertStringToTimestamp(str_date: String?): Long {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = formatter.parse(str_date)
            date.time
        } catch (e: ParseException) {
            println("Exception :$e")
            0
        }
    }
}