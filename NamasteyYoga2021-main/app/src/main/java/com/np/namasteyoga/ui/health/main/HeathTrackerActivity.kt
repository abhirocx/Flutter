package com.np.namasteyoga.ui.health.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.material.snackbar.Snackbar
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.ui.health.GraphChart
import com.np.namasteyoga.ui.health.GraphChartModel
import com.np.namasteyoga.ui.health.history.StepHistoryActivity
import com.np.namasteyoga.ui.health.settings.GoalSettingActivity
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_heath_tracker.*
import kotlinx.android.synthetic.main.toolbar_with_back_and_img.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

enum class FitActionRequestCode {
    SUBSCRIBE,
    READ_DATA
}

class HeathTrackerActivity : BaseActivity<HealthTrackerViewModel>(HealthTrackerViewModel::class),
    GraphChart.OnBarClickListener {

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .addDataType(DataType.TYPE_DISTANCE_DELTA)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
        .build()

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private fun checkPermissionsAndRun(fitActionRequestCode: FitActionRequestCode) {
        if (permissionApproved()) {
            fitSignIn(fitActionRequestCode)
        } else {
            requestRuntimePermissions(fitActionRequestCode)
        }
    }

    private fun fitSignIn(requestCode: FitActionRequestCode) {
        if (oAuthPermissionsApproved()) {
            if (NetworkUtil.isInternetAvailable(context))
                performActionForRequestCode(requestCode)
            else {
                toast(R.string.no_internet_connection)
            }

//            fetchStepsHistory()
        } else {
            requestCode.let {
                GoogleSignIn.requestPermissions(
                    this,
                    requestCode.ordinal,
                    getGoogleAccount(), fitnessOptions
                )
            }
        }
    }

    private fun fetchStepsHistory() {
        isShowDialog(true)
        readData()

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)


        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val endTime = calendar.timeInMillis

        val calendarEnd = Calendar.getInstance()
        calendarEnd.timeInMillis = endTime
        calendarEnd.add(Calendar.DAY_OF_YEAR, 6)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 24)
        calendarEnd.set(Calendar.MINUTE, 0)
        calendarEnd.set(Calendar.SECOND, 0)

//        val dateStstartTime= DateUtils.getDataString(getString(R.string.graph_date_format),calendarEnd.timeInMillis)
//        val dateStendTime= DateUtils.getDataString(getString(R.string.graph_date_format),endTime)
//        Logger.Debug("setGoalData::  $dateStendTime $dateStstartTime")
        val dataReadRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(endTime, calendarEnd.timeInMillis, TimeUnit.MILLISECONDS)
            .build()
        Fitness.getHistoryClient(this, getGoogleAccount()).readData(dataReadRequest)
            .addOnSuccessListener {
                it.buckets.forEach { a ->
                    a?.dataSets?.forEach { b ->
                        b.dataPoints.forEach { c ->
                            val data = c.getValue(Field.FIELD_STEPS)

                            val date = c.getStartTime(TimeUnit.MILLISECONDS)
                            val cal = Calendar.getInstance()
                            cal.timeInMillis = date

                            val dateSt =
                                DateUtils.getDataString(getString(R.string.graph_date_format), date)
                            Logger.Debug("setGoalData::  $dateSt ")
                            val position = getPosition(dateSt)
                            if (position >= 0) {
                                updateListData(position, data.asInt())
                            }
                            Logger.Debug(
                                tag = TAG,
                                msg = "setGoalData: $data Date: ${cal.get(Calendar.DATE)} $dateSt"
                            )
                        }
                        updateGraphUI()
                    }
                }
                isShowDialog(false)
            }
            .addOnCanceledListener {
                isShowDialog(false)
                Logger.Debug(tag = TAG, msg = "setGoalData: ")
            }
    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
    private fun oAuthPermissionsApproved() =
        GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)

    private fun subscribe() {
        Fitness.getRecordingClient(this, getGoogleAccount())
            .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    eventType.enable()
                    Logger.Debug("Successfully subscribed!")
                    fetchStepsHistory()
                } else {
                    Logger.Debug("There was a problem subscribing.")
                }
            }


    }

    private fun readData() {


        Fitness.getHistoryClient(this, getGoogleAccount())
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->

                when {
                    dataSet.isEmpty -> return@addOnSuccessListener
                    else -> dataSet.dataPoints.forEach {
                        val dataCount = it?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0
                        Logger.Debug(tag = TAG, msg = "Total steps: $dataCount")
                        model.stepCountValue.postValue(dataCount)
                    }
                }
                swipeRefreshLayout.isRefreshing = false

            }
            .addOnFailureListener { e ->
                swipeRefreshLayout.isRefreshing = false
                Logger.Debug("There was a problem getting the step count.")
            }


    }

    private fun performActionForRequestCode(requestCode: FitActionRequestCode) =
        when (requestCode) {
            FitActionRequestCode.READ_DATA -> readData()
            FitActionRequestCode.SUBSCRIBE -> subscribe()
        }

    private fun oAuthErrorMsg(requestCode: Int, resultCode: Int) {
        val message = """
            There was an error signing into Fit. Check the troubleshooting section of the README
            for potential issues.
            Request code was: $requestCode
            Result code was: $resultCode
        """.trimIndent()
        Logger.Debug(tag = TAG, msg = message)
    }

    private fun permissionApproved(): Boolean {
        return if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        } else {
            true
        }
    }

    private fun requestRuntimePermissions(requestCode: FitActionRequestCode) {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            )

        requestCode.let {
            if (shouldProvideRationale) {
                Logger.Debug(
                    tag = TAG,
                    msg = "Displaying permission rationale to provide additional context."
                )
                Snackbar.make(
                    find(R.id.main_activity_view),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.ok) {
                        // Request permission
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                            requestCode.ordinal
                        )
                    }
                    .show()
            } else {
                Logger.Debug(tag = TAG, msg = "Requesting permission")
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    requestCode.ordinal
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when {
            grantResults.isEmpty() -> {
                Logger.Debug(tag = TAG, msg = "User interaction was cancelled.")
            }
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                // Permission was granted.
                val fitActionRequestCode = FitActionRequestCode.values()[requestCode]
                fitActionRequestCode.let {
                    fitSignIn(fitActionRequestCode)
                }
            }
            else -> {

                Snackbar.make(
                    find(R.id.main_activity_view),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            C.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }

    private val list = ArrayList<GraphChartModel>()
    private val listKey = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list.clear()
        chart.run {


            val date = Calendar.getInstance()
            date.time = Date()
            date.add(Calendar.WEEK_OF_YEAR, -1)
            for (i in 1..7) {

                val dateStr = DateUtils.getDataString(
                    getString(R.string.graph_date_format),
                    date.timeInMillis
                )
                date.add(Calendar.DAY_OF_YEAR, 1)
                list.add(
                    GraphChartModel(
                        GraphChartUtils.getColor(),
//                        GraphChartUtils.getRandomValue(),
                        CommonInt.Zero,
                        dateStr
                    )
                )
                listKey.add(dateStr)
            }

//            updateGraphUI()


        }
        settings.setOnClickListener {
            eventType.disable()
            startGoalSettingActivity()
        }
        model.run {
            goalSettingValue.observe(this@HeathTrackerActivity, {
                calculateDataAccordingSteps(model.stepCountValue.value ?: CommonInt.Zero)
            })
            stepCountValue.observe(this@HeathTrackerActivity, {
                it?.run(::calculateDataAccordingSteps)
            })
        }

        checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
        eventType.setOnClickListener {
            eventType.disable()
            startStepHistoryActivity()
        }

        swipeRefreshLayout.setOnRefreshListener {
            readData()
        }
    }

    private fun startStepHistoryActivity() {
        startActivity(Intent(context, StepHistoryActivity::class.java))
    }

    private fun calculateDataAccordingSteps(steps: Int) {
        stepCount.text = "$steps"
        when {
            steps <= CommonInt.Zero -> {
                circularProgressBar.progress = 0f
                txtDistance.text = getString(R.string._s_km, "0")
                txtCalories.text = getString(R.string._d_cal, 0)
            }
            steps > model.goalSettingValue.value?:0 -> {
                circularProgressBar.progress = 100f
                updateDistanceUI(steps)
                updateEnergyUI(steps)
            }
            else -> {
                updateCurrentDayUI(steps)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        eventType.enable()
        settings.enable()
    }


    private fun updateCurrentDayUI(steps: Int) {
        stepCount.text = "$steps"
        updateProgressUI(steps)
        updateDistanceUI(steps)
        updateEnergyUI(steps)
    }

    private fun updateProgressUI(steps: Int) {
        val goalValue = (model.goalSettingValue.value ?: CommonInt.GoalDefaultValue).toFloat()
        val percent = (steps.toFloat() / goalValue) * 100f
        circularProgressBar.progress = percent
    }

    private fun updateDistanceUI(steps: Int) {
        val km = (steps.toFloat() / CommonInt.StepOneKM)
        val kmStr = String.format("%.1f", km)
        txtDistance.text = getString(R.string._s_km, kmStr)
    }

    private fun updateEnergyUI(steps: Int) {
        val burnCal = steps / CommonInt.StepOneCal
        txtCalories.text = getString(R.string._d_cal, burnCal)
    }

    private fun updateGraphUI() {
        var max = Int.MIN_VALUE
        list.forEach {
            max = max.coerceAtLeast(it.barValue)
        }
        max += 50
        chart.let {
            it.setBarMaxValue(max)
            it.addBar(list)
            it.setOnBarClickListener(this@HeathTrackerActivity)
        }
        chart3.let {
            it.setBarMaxValue(1)
            it.addBar(list)
        }

    }

    private fun getPosition(string: String): Int {
        return listKey.indexOf(string)
    }

    private fun getModel(position: Int): GraphChartModel {
        return list[position]
    }

    private fun updateListData(position: Int, value: Int) {
        list[position].barValue = value
    }

    private fun startGoalSettingActivity() {
        startActivityForResult(Intent(context, GoalSettingActivity::class.java), CommonInt.hundred)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonInt.hundred) {
            if (resultCode == RESULT_OK) {
                val value = data?.getIntExtra(
                    IntentUtils.SHARE_GOAL_SETTINGS_VALUE,
                    CommonInt.GoalDefaultValue
                ) ?: CommonInt.GoalDefaultValue
                model.updateStepGoalData(value)
            }

        } else {
            when (resultCode) {
                RESULT_OK -> {
                    val postSignInAction = FitActionRequestCode.values()[requestCode]
                    postSignInAction.let {
                        performActionForRequestCode(postSignInAction)
                    }
                }
                else -> oAuthErrorMsg(requestCode, resultCode)
            }
        }


    }

    companion object {
        private const val TAG = "HeathTrackerActivity:: "
    }

    override fun layout(): Int = R.layout.activity_heath_tracker

    override fun tag(): String = TAG

    override val title: String
        get() = getString(R.string.fitness_activity)

    override val isShowTitle: Boolean
        get() = true

    override fun onBarClick(barChartModel: GraphChartModel?) {
        barChartModel?.barValue?.run {
            UIUtils.showCustomToast(context, this)
        }
    }
}