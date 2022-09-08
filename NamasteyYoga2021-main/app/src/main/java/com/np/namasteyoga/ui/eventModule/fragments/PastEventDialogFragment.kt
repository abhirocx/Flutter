package com.np.namasteyoga.ui.eventModule.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.ui.eventModule.adapter.PastEventImagesAdapter
import com.np.namasteyoga.ui.eventModule.eventVideo.EventVideoActivity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getDistanceFormat
import com.np.namasteyoga.utils.UIUtils.navigateLocation
import com.np.namasteyoga.utils.UIUtils.shareData
import com.np.namasteyoga.utils.Util.getDecryptData
import kotlinx.android.synthetic.main.past_event_details_layout.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast


class PastEventDialogFragment : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.past_event_details_layout, container, false)
    }

    @SuppressLint("SetTextI18n")
    fun setData(event: Event?) {
        event?.let { item ->
            tvEventName.text = item.eventName ?: CommonString.NA
            val number3digits = if (item.nearest == CommonInt.One) {
                tvDistance.show()
                item.nearest_distance?.getDistanceFormat()
            } else {
                tvDistance.hide()
                CommonString.Empty
            }
            tvDistance.text = "${
                getString(
                    R.string.s_km_away,
                    number3digits.toString()
                )
            } ${item.cityName ?: CommonString.Empty}"

            val sDate = if (item.startTime.isNullOrEmpty()) {
                CommonString.NA
            } else {
                val date =
                    DateUtils.getDateToString(item.startTime ?: CommonString.Empty)?.time ?: 0
                DateUtils.getDataString(
                    getString(R.string.hh_mm_a_dd_mmm_yyyy),
                    date
                )
            }

            val eDate = if (item.endTime.isNullOrEmpty()) {
                CommonString.NA
            } else {
                val date = DateUtils.getDateToString(item.endTime ?: CommonString.Empty)?.time ?: 0
                DateUtils.getDataString(
                    getString(R.string.hh_mm_a_dd_mmm_yyyy),
                    date
                )
            }
            tvCalender.text = "$sDate ${getString(R.string.to)} $eDate"
            etAddress.text = item.address ?: CommonString.NA
            tvName.text = item.contactPerson?.getDecryptData() ?: CommonString.NA
            tvEventType.text = item.mode ?: CommonString.NA
            tvSittingCapacity.text = item.sittingCapacity ?: CommonString.NA
            tvDiscription.text = item.short_description ?: CommonString.NA
            tvPhonenum.text = item.contactNo?.getDecryptData() ?: CommonString.NA
            tvEmail.text = item.email?.getDecryptData() ?: CommonString.NA

            if (item.videoId.isNullOrEmpty()) {
                videoLayout.hide()
            } else {
                videoLayout.show()
            }
            if (item.event_images.isNullOrEmpty()) {
                photoLayout.hide()
            } else {
                photoLayout.show()
            }

            youtubeImage.load(C.ASANA_THUMB_BASE_URL + event.videoId + C.ASANA_THUMB_BASE_URL_END) {
                crossfade(true)
                placeholder(R.drawable.ic_place_holder)
                transformations(RoundedCornersTransformation(14f))
            }

            recyclerViewLoadImage.run {
                layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                adapter = PastEventImagesAdapter(item.event_images)
            }

            lessDetails.paintFlags = lessDetails.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            moreDetails.paintFlags = moreDetails.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            lessDetails.setOnClickListener {
                moreDetails.show()
                eventDescription.hide()
            }
            moreDetails.setOnClickListener {
                moreDetails.hide()
                eventDescription.show()
            }

            navigation.onClick {
                navigation.context.navigateLocation(item.lat.toString() + CommonString.COMMA + item.lng.toString())
            }


            share.onClick {
                val data = share.context.getString(
                    R.string.share_event_data,
                    item.eventName,
                    item.contactNo?.getDecryptData() ?: CommonString.NA,
                    item.email?.getDecryptData() ?: CommonString.NA,
                    item.address
                )
                share.context.shareData(data)
            }

            youtubeClick.setOnClickListener {
                if (NetworkUtil.isInternetAvailable(requireContext())) {
                    if (item.videoId.isNotEmpty()) {
                        val intent = Intent(requireContext(), EventVideoActivity::class.java)
                        intent.putExtra(IntentUtils.SHARE_STRING, item.videoId)
                        startActivity(intent)
                    } else {
                        toast(R.string.something_went_wrong)
                    }
                } else {
                    toast(R.string.no_internet_connection)
                }
            }

        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                mBehavior = (this@PastEventDialogFragment.dialog as BottomSheetDialog).behavior
                mBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onResume() {
        super.onResume()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }




}