package com.np.namasteyoga.ui.eventModule.adapter


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
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.interfaces.ClickItem
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.utils.DateUtils
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import com.np.namasteyoga.utils.Util
import com.np.namasteyoga.utils.invisible
import com.np.namasteyoga.utils.show
import org.jetbrains.anko.find


class PastEventImagesAdapter(
    private val list: List<String>?,
) :
    RecyclerView.Adapter<PastEventImagesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.past_event_images_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return

        holder.image.load(item) {
            crossfade(true)
            placeholder(R.drawable.ic_place_holder)
            transformations(RoundedCornersTransformation(8f))
        }

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.find(R.id.image)


        init {

        }

    }

}
