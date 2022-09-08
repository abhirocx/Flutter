package com.np.namasteyoga.ui.main.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.StepHistoryModel
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.utils.DateUtils
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import com.np.namasteyoga.utils.Util
import com.np.namasteyoga.utils.invisible
import com.np.namasteyoga.utils.show
import org.jetbrains.anko.find


class AsanaCatagoryAdapter(
        private val list: List<AsanaCategoryModel>?,
        private val itemClickListener: OnClickItem<AsanaCategoryModel>
) :
        RecyclerView.Adapter<AsanaCatagoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_asana_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return

        item.run {

//            holder.name.text = (category_name?:CommonString.NA).getCapitalized()
//            holder.image.load(category_image_path){
////                crossfade(true)
//                placeholder(R.drawable.logo)
////                transformations(RoundedCornersTransformation(32f))
//            }



            var videosSum = 0
            for (i in 0..(sub_category_data?.size?.minus(1)!!)) {
                videosSum += sub_category_data.get(i).total_aasana
            }
            holder.name.text = (category_name ?: CommonString.NA).getCapitalized()
            holder.image.load(category_image_path) {
                crossfade(true)
                placeholder(R.drawable.ic_place_holder)
//                transformations(RoundedCornersTransformation(32f))
            }
            holder.videoCount.text = if (videosSum > 1) {
                "$videosSum ${holder.itemView.context.getString(R.string.exercises)}"
            } else {
                "$videosSum ${holder.itemView.context.getString(R.string.exercise)}"
            }
        }


    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.find(R.id.image)
        val name: TextView = view.find(R.id.name)
        val videoCount: TextView = view.find(R.id.count)


        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(adapterPosition,list?.get(adapterPosition))
            }
        }

    }

}
