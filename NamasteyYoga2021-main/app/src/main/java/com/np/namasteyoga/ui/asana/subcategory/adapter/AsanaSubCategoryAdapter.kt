package com.np.namasteyoga.ui.asana.subcategory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.np.namasteyoga.R
import com.np.namasteyoga.datasource.response.SubCategoryData
import com.np.namasteyoga.utils.RoundedCornersTransform
import com.squareup.picasso.Picasso

class AsanaSubCategoryAdapter(
    var mContext: Context,
    var subCategoryDataList: List<SubCategoryData>,
    protected var mListener: ItemListener?
) : RecyclerView.Adapter<AsanaSubCategoryAdapter.ViewHolder>() {
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var name: TextView
        var image: ImageView
        var item: SubCategoryData? = null
        fun setData(item: SubCategoryData) {
            this.item = item
            name.text = item.subcategory_name
//            Picasso.get().load(item.sub_category_image_path)
//                .placeholder(R.drawable.logo)
//                .error(R.drawable.logo)
//                .transform(RoundedCornersTransform(10, 0))
//                .into(image)

           image.load(item.sub_category_image_path) {
                crossfade(true)
                placeholder(R.drawable.ic_place_holder)
                error(R.drawable.ic_place_holder)
                transformations(RoundedCornersTransformation(8f))
            }
        }

        override fun onClick(view: View) {
            mListener?.onItemClick(item)
        }

        init {
            v.setOnClickListener(this)
            name = v.findViewById<View>(R.id.name) as TextView
            image = v.findViewById<View>(R.id.image) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.asasa_sub_category_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(subCategoryDataList[position])
    }

    override fun getItemCount(): Int {
        return subCategoryDataList.size
    }

    interface ItemListener {
        fun onItemClick(item: SubCategoryData?)
    }
}