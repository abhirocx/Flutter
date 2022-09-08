package com.np.namasteyoga.ui.celebrityTestimonial.adapter


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
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import com.np.namasteyoga.interfaces.ClickItem
import com.np.namasteyoga.interfaces.ClickItem2
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import org.jetbrains.anko.find


class CelebrityTestimonialListAdapter(
    private val list: List<CelebrityTestimonialModel>?,
    private val itemClickListener: ClickItem2<CelebrityTestimonialModel>,
    private val paginationListener: PaginationListener
) :
        RecyclerView.Adapter<CelebrityTestimonialListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celebrity_testimonial_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return

        item.run {

            holder.name.text = (name ?: CommonString.NA).getCapitalized()
            val url = C.ASANA_THUMB_BASE_URL + vedio_id + C.ASANA_THUMB_BASE_URL_END
            holder.youtubeImage.load(url) {
                crossfade(true)
                placeholder(R.drawable.ic_place_holder)
                transformations(RoundedCornersTransformation(8f))
            }

        }

        if ((position + 1) >= list.size)
            paginationListener.page()
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val youtubeImage: ImageView = view.find(R.id.youtubeImage)
        private val youtubeClick: ImageView = view.find(R.id.youtubeClick)
        val name: TextView = view.find(R.id.name)


        init {
            youtubeClick.setOnClickListener {
                itemClickListener.onClickPositionNo(adapterPosition,list?.get(adapterPosition))
            }
        }

    }

}
