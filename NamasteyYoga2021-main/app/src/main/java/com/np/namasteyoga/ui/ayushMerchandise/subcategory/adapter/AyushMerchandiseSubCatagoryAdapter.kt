package com.np.namasteyoga.ui.ayushMerchandise.subcategory.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.StepHistoryModel
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.AyushSubCategoryList
import com.np.namasteyoga.interfaces.ClickItem
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.utils.DateUtils
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import com.np.namasteyoga.utils.Util
import com.np.namasteyoga.utils.invisible
import com.np.namasteyoga.utils.show
import org.jetbrains.anko.find


class AyushMerchandiseSubCatagoryAdapter(
    private val list: List<AyushSubCategoryList>?,
    private val itemClickListener: ClickItem<AyushSubCategoryList>,
    private val paginationListener: PaginationListener
) :
        RecyclerView.Adapter<AyushMerchandiseSubCatagoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ayush_sub_category_item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return

        item.run {

            holder.name.text = (product_name ?: CommonString.NA).getCapitalized()
            holder.des.text = (product_description ?: CommonString.NA)

            if (product_image?.isNotEmpty()==true)
//            holder.image.load(product_image.get(0)?.product_image?:CommonString.Empty) {
//                crossfade(true)
//                placeholder(R.drawable.logo)
////                transformations(RoundedCornersTransformation(8f))
//            }
            Glide.with(holder.itemView.context).load(product_image.get(0)?.product_image?:CommonString.Empty)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(holder.image)

        }

        if ((position + 1) >= list.size)
            paginationListener.page()
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.find(R.id.image)
        val name: TextView = view.find(R.id.tvName)
        val des: TextView = view.find(R.id.tvDesc)


        init {
            itemView.setOnClickListener {
                itemClickListener.onClickPosition(adapterPosition,list?.get(adapterPosition))
            }
        }

    }

}
