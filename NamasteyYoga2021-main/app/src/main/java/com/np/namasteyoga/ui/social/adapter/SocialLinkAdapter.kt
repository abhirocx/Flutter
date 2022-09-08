package com.np.namasteyoga.ui.social.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.SocialLinkModel
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import com.np.namasteyoga.utils.hide
import com.np.namasteyoga.utils.show
import org.jetbrains.anko.find


class SocialLinkAdapter(
        private val list: List<SocialLinkModel>?,
        private val itemClickListener: OnClickItem<SocialLinkModel>,
        private val paginationListener: PaginationListener
) :
        RecyclerView.Adapter<SocialLinkAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.social_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return

        item.run {

            holder.name.text = (organization_name?:CommonString.NA)//.getCapitalized()

            if (org_facebook.isNullOrBlank()){
                holder.fb.hide()
            }
            else{
                holder.fb.show()
            }

            if (org_twitter.isNullOrBlank()){
                holder.twitter.hide()
            }
            else{
                holder.twitter.show()
            }
            if (org_instagram.isNullOrBlank()){
                holder.linkden.hide()
            }
            else{
                holder.linkden.show()
            }
            if (org_other.isNullOrBlank()){
                holder.others.hide()
            }
            else{
                holder.others.show()
            }

            if (org_youtube.isNullOrBlank()){
                holder.youtubeClick.hide()
            }
            else{
                holder.youtubeClick.show()
            }


        }
        if ((position + 1) >= list.size)
            paginationListener.page()

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fb: ImageView = view.find(R.id.fb)
        val twitter: ImageView = view.find(R.id.twitter)
        val linkden: ImageView = view.find(R.id.linkden)
        val others: ImageView = view.find(R.id.others)
        val youtubeClick: ImageView = view.find(R.id.youtubeClick)
        val name: TextView = view.find(R.id.name)


        init {
            itemView.setOnClickListener {
                itemClickListener.onClick(adapterPosition,list?.get(adapterPosition))
            }
            fb.setOnClickListener {
                list?.get(adapterPosition)?.org_facebook?.run {
                    itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
//                    getOpenFacebookIntent(itemView.context,this)
                }
            }
            twitter.setOnClickListener {
                list?.get(adapterPosition)?.org_twitter?.run {
                    itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
                }
            }
            linkden.setOnClickListener {
                list?.get(adapterPosition)?.org_instagram?.run {
                    itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
                }
            }
            others.setOnClickListener {
                list?.get(adapterPosition)?.org_other?.run {
                    itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
                }
            }
            youtubeClick.setOnClickListener {
                list?.get(adapterPosition)?.org_youtube?.run {
                    itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
                }
            }
        }

    }


}
