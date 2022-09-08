package com.np.namasteyoga.ui.asana.catagory.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import org.jetbrains.anko.find


class AsanaCatagoryAdapter(
    private val list: List<AsanaCategoryModel>?,
    //private val list2: List<AsanaCategoryModel>?,

    private val itemClickListener: OnClickItem<AsanaCategoryModel>,
    private val paginationListener: PaginationListener
) :
    RecyclerView.Adapter<AsanaCatagoryAdapter.MyViewHolder>() {
    private val list2: List<AsanaCategoryModel>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.asasa_category_item_layout, parent, false)
        view.find<ImageView>(R.id.cardImg).setBackgroundColor(ContextCompat.getColor(parent.context,R.color.bg_grey))
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardImg.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.bg_grey))
        val item = list?.get(position) ?: return


        item.run {

            var videosSum = 0
            for (i in 0..(sub_category_data?.size?.minus(1)!!)) {
                videosSum += sub_category_data.get(i).total_aasana
            }
            holder.name.text = (category_name ?: CommonString.NA).getCapitalized()
            holder.image.load(category_image_path) {
                crossfade(true)
                placeholder(R.drawable.ic_place_holder)
                error(R.drawable.ic_place_holder)
                transformations(RoundedCornersTransformation(8f))
            }
            holder.videoCount.text = if (videosSum > 1) {
                "$videosSum ${holder.itemView.context.getString(R.string.exercises)}"
            } else {
                "$videosSum ${holder.itemView.context.getString(R.string.exercise)}"
            }

        }
        if ((position + 1) >= list.size)
            paginationListener.page()

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.find(R.id.image)
        val cardImg: ImageView = view.find(R.id.cardImg)
        val name: TextView = view.find(R.id.name)
        val videoCount: TextView = view.find(R.id.tvVideosCount)

        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(adapterPosition, list?.get(adapterPosition))
            }
        }

    }

}
