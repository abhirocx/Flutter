package com.np.namasteyoga.ui.health.history

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.StepHistoryModel
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.ui.health.history.adapter.StepHistoryAdapter
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_step_history.*
import org.jetbrains.anko.toast
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class StepHistoryActivity : BaseActivity<StepHistoryViewModel>(StepHistoryViewModel::class),
    OnClickItem<StepHistoryModel> {


    private val list = ArrayList<StepHistoryModel>()
    private lateinit var historyAdapter: StepHistoryAdapter
    private lateinit var startCalendar: Calendar
    private lateinit var endCalendar: Calendar

    //    private var minMonth: Int = 0
    private var currentMonth: Int = 0
//    private var maxMonth: Int = minMonth + 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        historyAdapter = StepHistoryAdapter(
            list,
            this,
            model.goalSettingValue.value ?: CommonInt.GoalDefaultValue
        )


        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        startCalendar = Calendar.getInstance()
        startCalendar.time = date
        startCalendar.run {

            set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0)
        }
        startCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        val cal = Calendar.getInstance()
        cal.timeInMillis = startCalendar.timeInMillis
        endCalendar = cal
        endCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)

        setMonthTitle()


        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
        getHistoryData()
        nextArrow.setOnClickListener {
            val calendarTemp = Calendar.getInstance()
            calendarTemp.time = Date()
            val monthStart = endCalendar.get(Calendar.MONTH)
            val month = calendarTemp.get(Calendar.MONTH)
            if (monthStart > month) {
                toast(R.string.no_more_data_available)
                return@setOnClickListener
            }
            if (startCalendar.timeInMillis >= Date().time) {
                toast(R.string.no_more_data_available)
                return@setOnClickListener
            }
//            minMonth += 1
//            maxMonth += 1

//            startCalendar.set(Calendar.MONTH, minMonth)
            startCalendar.add(Calendar.MONTH, 1)
//            endCalendar.set(Calendar.MONTH, maxMonth)
            endCalendar.add(Calendar.MONTH, 1)
            setMonthTitle()
            getHistoryData()
        }
        rightArrow.setOnClickListener {
            if (startCalendar.timeInMillis < MIN_TIME) {
                toast(R.string.no_more_data_available)
                return@setOnClickListener
            }
//            minMonth -= 1
//            maxMonth -= 1

            startCalendar.add(Calendar.MONTH, -1)
//            startCalendar.set(Calendar.MONTH, minMonth)
//            endCalendar.set(Calendar.MONTH, maxMonth)
            endCalendar.add(Calendar.MONTH, -1)
            setMonthTitle()
            getHistoryData()
        }
        recycleViewHandle()
    }

    private fun getHistoryData() {
        if (NetworkUtil.isInternetAvailable(context))
            fetchStepsHistory()
        else {
            toast(R.string.no_internet_connection)
        }
    }

    private fun setMonthTitle() {
        monthName.text = DateUtils.getDataString(
            getString(R.string.history_title_date_format),
            startCalendar.timeInMillis
        )

    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)


    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .addDataType(DataType.TYPE_DISTANCE_DELTA)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
        .build()


    private fun fetchStepsHistory() {
        isShowDialog(true)

//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.time = Date()
//        calendar.add(Calendar.MONTH, -1)
//        val endTime = calendar.timeInMillis
//
//        val calendarEnd = Calendar.getInstance()
//        calendarEnd.time = Date(endTime)
//        calendarEnd.set(Calendar.HOUR_OF_DAY, 23)
        val dateStstartTime = DateUtils.getDataString(
            getString(R.string.graph_date_format),
            startCalendar.timeInMillis
        )
        val dateStendTime =
            DateUtils.getDataString(getString(R.string.graph_date_format), endCalendar.timeInMillis)
        Logger.Debug("setGoalData::  $dateStendTime $dateStstartTime")

        val dataReadRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis,
                TimeUnit.MILLISECONDS
            )
            .build()
        Fitness.getHistoryClient(this, getGoogleAccount()).readData(dataReadRequest)
            .addOnSuccessListener {
                list.clear()
                it.buckets.forEach { a ->
                    a?.dataSets?.forEach { b ->

                        b.dataPoints.forEach { c ->
                            val data = c.getValue(Field.FIELD_STEPS)

                            val date = c.getStartTime(TimeUnit.MILLISECONDS)
                            val model = StepHistoryModel(data.asInt(), date)
                            list.add(model)
//                            val cal = Calendar.getInstance()
//                            cal.timeInMillis = date
//
//                            val dateSt = DateUtils.getDataString(getString(R.string.graph_date_format), date)
                            Logger.Debug("setGoalData::  $data  $date ")


                        }


                    }
                }
                list.reverse()
                recyclerView.adapter?.notifyDataSetChanged()
                recycleViewHandle()
                isShowDialog(false)
            }
            .addOnCanceledListener {
                list.clear()
                recycleViewHandle()
                recyclerView.adapter?.notifyDataSetChanged()
                isShowDialog(false)
                Logger.Debug(tag = TAG, msg = "setGoalData: ")
            }
    }

    private fun recycleViewHandle() {
        if (list.isEmpty()) {
            recyclerView.hide()
        } else {
            recyclerView.show()
        }
    }

    companion object {
        private const val TAG = "StepHistoryActivity:::  "
        private const val MIN_TIME: Long = 1609439400971
    }

    override fun layout(): Int = R.layout.activity_step_history

    override fun tag(): String = TAG

    override val title: String
        get() = getString(R.string.history)
    override val isShowTitle: Boolean
        get() = true

    override fun onClick(position: Int, t: StepHistoryModel?) {

    }
}