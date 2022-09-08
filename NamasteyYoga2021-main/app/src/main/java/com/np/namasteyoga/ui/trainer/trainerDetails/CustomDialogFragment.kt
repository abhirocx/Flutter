package com.np.namasteyoga.ui.trainer.trainerDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.User
import com.np.namasteyoga.utils.Util
import com.np.namasteyoga.utils.Util.getDecryptData
import com.np.namasteyoga.utils.hide
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.center_details_layout.*

class CustomDialogFragment : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trainer_details_layout, container, false)
    }

    fun setData(user: User?,startLatLng:LatLng) {
        user?.let { item ->
            trainerType.text = item.user_type ?: CommonString.NA
            name.text = item.name ?: CommonString.NA
            etEmailId.text = item.email?.getDecryptData() ?: CommonString.NA
            etAddress.text = item.address ?: CommonString.NA
            etMobile.text = item.phone?.getDecryptData() ?: CommonString.NA

//            val lat = (item.lat?:CommonString.Double).toDouble()
//            val long = (item.lng?:CommonString.Double).toDouble()
//            val endLatLng = LatLng(lat,long)
//            val distance = Util.distance(startLatLng,endLatLng)
//            val number3digits = String.format("%.1f", distance)
//            txtDistance.text = getString(R.string.s_km_away, number3digits)

            val number3digits =   if (item.nearest== CommonInt.One){
                txtDistance.show()
                item.nearest_distance
            }else{
                txtDistance.hide()
                CommonString.Empty
            }
            txtDistance.text = getString(R.string.s_km_away, number3digits .toString())
        }
    }


}