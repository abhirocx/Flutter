package com.np.namasteyoga.ui.eventModule.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.ui.joiningInstructions.JoiningInstructionsActivity
import com.np.namasteyoga.utils.DateUtils
import com.np.namasteyoga.utils.IntentUtils
import com.np.namasteyoga.utils.UIUtils.getDistanceFormat
import com.np.namasteyoga.utils.UIUtils.navigateLocation
import com.np.namasteyoga.utils.UIUtils.shareData
import com.np.namasteyoga.utils.Util.getDecryptData
import com.np.namasteyoga.utils.hide
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.event_details_layout.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.ParseException
import java.text.SimpleDateFormat


//import kotlinx.android.synthetic.main.fragment_events.*

class CustomDialogEventFragment : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_details_layout, container, false)
    }

    @SuppressLint("SetTextI18n")
    fun setData(event: Event?, eventType: String) {
        event?.let { item ->
            tvEventName.text = item.eventName ?: CommonString.NA
//            tvDistance.text = item.nearest_distance ?: CommonString.NA
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
//            tvCalender.text = "${item.startTime ?: CommonString.NA} to ${item.endTime ?: CommonString.NA}"

           val sDate = if (item.startTime.isNullOrEmpty()) {
                CommonString.NA
            } else {
                val date = DateUtils.getDateToString(item.startTime ?: CommonString.Empty)?.time ?: 0
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

            /* val number3digits =   if (item.nearest== CommonInt.One){
                 txtDistance.show()
                 item.nearest_distance
             }else{
                 txtDistance.hide()
                 CommonString.Empty
             }
             txtDistance.text = getString(R.string.s_km_away, number3digits .toString())*/


            if (activity?.javaClass?.simpleName.equals("FragmentUserEvents") &&
                (eventType == CommonString.UPCOMING || eventType == CommonString.CURRENT)
            ) {
                if (eventType == CommonString.UPCOMING)
                tvEditEvent.show()
                if (eventType == CommonString.CURRENT)
                    tvEditEvent.hide()

                tvIntrested.hide()
                addToCalender.hide()
            } else {
                tvEditEvent.visibility = View.GONE
                if ((eventType == CommonString.PAST)) {
                    tvIntrested.hide()
                    addToCalender.hide()
                } else {
                    tvIntrested.visibility = View.VISIBLE
                    addToCalender.show()
                }
            }




            tvEditEvent.onClick {
                if (activity?.javaClass?.simpleName.equals("FragmentEvents")) {
                    (activity as FragmentEvents).openEditEvent(event)
                } else if (activity?.javaClass?.simpleName.equals("FragmentUserEvents")) {
                    (activity as FragmentUserEvents).openEditEvent(event)
                }
            }
            tvIntrested.onClick {
//                if (activity?.javaClass?.simpleName.equals("FragmentEvents")) {
//                    //  (activity as Joining_activity).openJoiningPage(event)
//
//                }
                val intent = Intent(context, JoiningInstructionsActivity::class.java)
                intent.putExtra(IntentUtils.SHARE_RESULT, event)
                startActivityForResult(intent, 100)
            }


            navigation.onClick {
                navigation.context.navigateLocation(item.lat.toString() + "," + item.lng.toString())
            }
            addToCalender.setOnClickListener {
                showPopUpToAddEventToCalendar(event)
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

            /* navigation.setOnClickListener {
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
            }*/

        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                mBehavior = (this@CustomDialogEventFragment.dialog as BottomSheetDialog).behavior
                mBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onResume() {
        super.onResume()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun showPopUpToAddEventToCalendar(event: Event) {
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(R.string.alert)
        alertBuilder.setMessage(R.string.are_you_sure_you_want_to_add)
        alertBuilder.setPositiveButton(
            R.string.yes
        ) { dialog, _ ->
            onAddEventClicked(event)
            dialog.dismiss()
        }
        alertBuilder.setNegativeButton(
            R.string.no
        ) { dialog, _ -> dialog.dismiss() }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun onAddEventClicked(event: Event) {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = "vnd.android.cursor.item/event"
        val startTime = event.startTime
        val endTime = event.endTime
        intent.putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            convertStringToTimestamp(startTime)
        )
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, convertStringToTimestamp(endTime))
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
        intent.putExtra(CalendarContract.Events.TITLE, event.eventName)
        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.short_description);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.address)
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY;COUNT=1")
        activity?.startActivity(intent)
    }

    private fun convertStringToTimestamp(str_date: String?): Long {
        return try {
           val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = formatter.parse(str_date)
            date.time
        } catch (e: ParseException) {
            println("Exception :$e")
            0
        }
    }

}