package com.np.namasteyoga.ui.trainer.trainerList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.User
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import com.np.namasteyoga.utils.UIUtils.getDistanceFormat
import com.np.namasteyoga.utils.UIUtils.navigateLocation
import com.np.namasteyoga.utils.UIUtils.shareData
import com.np.namasteyoga.utils.Util
import com.np.namasteyoga.utils.Util.getDecryptData
import com.np.namasteyoga.utils.hide
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.center_details_layout.*
import org.jetbrains.anko.find

class TrainerListAdapter(
    private val list: ArrayList<User>?,
    private val onClickItem: OnClickItem<User>,
    private val paginationListener: PaginationListener,
    private val startLatLng: LatLng
) :
    RecyclerView.Adapter<TrainerListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val body = view.find<View>(R.id.bodyLayout)
        val trainerType = view.find<TextView>(R.id.trainerType)
        val name = view.find<TextView>(R.id.name)
        val etMobile = view.find<TextView>(R.id.etMobile)
        val etEmailId = view.find<TextView>(R.id.etEmailId)
        val etAddress = view.find<TextView>(R.id.etAddress)
        private val navigation = view.find<ImageView>(R.id.navigation)
        private val share = view.find<ImageView>(R.id.share)

        val txtDistance = view.find<TextView>(R.id.txtDistance)

        init {
            body.setOnClickListener {
                onClickItem.onClick(adapterPosition, list?.get(adapterPosition))
            }
            navigation.setOnClickListener {
                list?.get(adapterPosition)?.run {
                    itemView.context.navigateLocation("$lat,$lng")
                }
            }
            share.setOnClickListener {
                list?.get(adapterPosition)?.run {
                    val data = itemView.context.getString(
                        R.string.share_trainer_data,
                        name,
                        phone?.getDecryptData() ?: CommonString.NA,
                        email?.getDecryptData() ?: CommonString.NA,
                        address
                    )
                    itemView.context.shareData(data)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.user_list_item_layout, parent, CommonBoolean.FALSE)
            .inflate(R.layout.trainer_item_layout, parent, CommonBoolean.FALSE)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list?.get(position) ?: return


        item.phone?.run {

        }

        holder.run {
            trainerType.text = item.user_type ?: CommonString.NA
            name.text = (item.name ?: CommonString.NA).getCapitalized()
            etAddress.text = item.address ?: CommonString.NA
            etEmailId.text = item.email?.getDecryptData() ?: CommonString.NA
            etMobile.text = item.phone?.getDecryptData() ?: CommonString.NA
//            val lat = (item.lat?:CommonString.Double).toDouble()
//            val long = (item.lng?:CommonString.Double).toDouble()
//            val endLatLng = LatLng(lat,long)
//            val distance = Util.distance(startLatLng,endLatLng)
//            val number3digits = String.format("%.1f", distance)
//            txtDistance.text = itemView.context.getString(R.string.s_km_away, number3digits .toString())

            val number3digits =   if (item.nearest==CommonInt.One){
                txtDistance.show()
                item.nearest_distance?.getDistanceFormat()
            }else{
                txtDistance.hide()
                CommonString.Empty
            }
//            txtDistance.text = itemView.context.getString(R.string.s_km_away, number3digits .toString()) + item.city_name?:CommonString.Empty
            txtDistance.text =  "${ itemView.context.getString(R.string.s_km_away, number3digits.toString())} ${item.city_name?:CommonString.Empty}"
        }
        if ((position + 1) >= list.size)
            paginationListener.page()

    }

    override fun getItemCount(): Int = list?.size ?: CommonInt.Zero
}