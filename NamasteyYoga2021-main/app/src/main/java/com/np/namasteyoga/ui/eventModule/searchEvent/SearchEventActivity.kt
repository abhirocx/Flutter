package com.np.namasteyoga.ui.eventModule.searchEvent

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.datasource.pojo.RatingRequest
import com.np.namasteyoga.datasource.pojo.RatingResponse
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.Rating
import com.np.namasteyoga.interfaces.Rating2
import com.np.namasteyoga.ui.eventModule.adapter.EventListAdapter
import com.np.namasteyoga.ui.eventModule.fragments.CustomDialogEventFragment
import com.np.namasteyoga.ui.login.LoginActivity
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.NetworkUtil
import com.np.namasteyoga.utils.Util
import kotlinx.android.synthetic.main.activity_search_event.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchEventActivity : BaseActivity<SearchEventViewModel>(SearchEventViewModel::class),
    OnClickItem<Event>, Rating, Rating2 {

    private val list = ArrayList<Event>()
    private val adapterSearch by lazy {
        EventListAdapter(
            list,
            this,
            model.sharedPreferences,
            true,
            0,
            this,
            true
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        ivBack.setOnClickListener {
            onBackPressed()
            hideKeyboard()
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@SearchEventActivity)
            adapter = adapterSearch
        }

        etSelectCity.requestFocus()
        etSelectCity.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println("Text [$s]")
                if (s.isNotEmpty()) searchCity(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })

        model.response.observe(this, Observer {
//            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@Observer
            }
            list.clear()

            try {
                val status = it.status ?: C.NP_STATUS_FAIL
                if (status == C.NP_STATUS_SUCCESS) {
                    it.data?.run {
                        list.addAll(this)
                    }
                } else {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
            recyclerView.adapter?.notifyDataSetChanged()
        })

    }

    private fun searchCity(toString: String) {
        if (NetworkUtil.isInternetAvailable(context)) {
//            isShowDialog(true,false)
            model.searchEvent(toString, Util.citySelected ?: City())
        } else {
            toast(R.string.no_internet_connection)
        }

    }


    override fun layout(): Int = R.layout.activity_search_event

    companion object {
        private const val TAG = "SearchEventActivity :: "
    }

    override fun tag(): String = TAG

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = false

    private val myRoundedBottomSheet = CustomDialogEventFragment()

    override fun onClick(position: Int, t: Event?) {
        t?.run {
            if (myRoundedBottomSheet.isAdded || myRoundedBottomSheet.isVisible) {
                myRoundedBottomSheet.dismiss()
            }
            myRoundedBottomSheet.show(supportFragmentManager, myRoundedBottomSheet.getTag())
            Handler(Looper.getMainLooper()).postDelayed({
                val currentTime = Date().time
                val eventType = if (convertStringToTimestamp(endTime) > currentTime) {
                    C.CURRENT
                } else {
                    C.PAST
                }
                myRoundedBottomSheet.setData(
                    this,
                    eventType
                )
            }, 300)
        }
    }

    override fun ratingDialog(pos: String?, position: Int) {
        model.getUserDetails()
        if (model.userDetails.value != null) ratingDialogUpdate(list[position]) else {
            startActivityForResult(Intent(this, LoginActivity::class.java), 100)
        }
    }
    override fun ratingDialog(event: Event?) {
        model.getUserDetails()
        if (model.userDetails.value != null) ratingDialogUpdate(event) else {
            startActivityForResult(Intent(this, LoginActivity::class.java), 100)
        }
    }


    @SuppressLint("SimpleDateFormat")
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


    private var rating = 0
    private fun ratingDialogUpdate(event: Event?) {
        val dialog = Dialog(this@SearchEventActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.rating_dialog_2)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        lp.windowAnimations = R.style.DialogAnimation
        dialog.window?.attributes = lp
        val ivStar1 = dialog.findViewById<ImageView>(R.id.ivStar1)
        val ivStar2 = dialog.findViewById<ImageView>(R.id.ivStar2)
        val ivStar3 = dialog.findViewById<ImageView>(R.id.ivStar3)
        val ivStar4 = dialog.findViewById<ImageView>(R.id.ivStar4)
        val ivStar5 = dialog.findViewById<ImageView>(R.id.ivStar5)
        val ivCross = dialog.findViewById<ImageView>(R.id.ivCross)
        val cancel_action = dialog.findViewById<ImageView>(R.id.cancel_action)
        val rlSubmit = dialog.findViewById<View>(R.id.rlSubmit)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        ivStar1.setOnClickListener {
            ivStar1.setImageResource(R.drawable.feedback_active)
            ivStar2.setImageResource(R.drawable.feedback_deactive)
            ivStar3.setImageResource(R.drawable.feedback_deactive)
            ivStar4.setImageResource(R.drawable.feedback_deactive)
            ivStar5.setImageResource(R.drawable.feedback_deactive)
            rating = 1
        }
        ivStar2.setOnClickListener {
            ivStar1.setImageResource(R.drawable.feedback_active)
            ivStar2.setImageResource(R.drawable.feedback_active)
            ivStar3.setImageResource(R.drawable.feedback_deactive)
            ivStar4.setImageResource(R.drawable.feedback_deactive)
            ivStar5.setImageResource(R.drawable.feedback_deactive)
            rating = 2
        }
        ivStar3.setOnClickListener {
            ivStar1.setImageResource(R.drawable.feedback_active)
            ivStar2.setImageResource(R.drawable.feedback_active)
            ivStar3.setImageResource(R.drawable.feedback_active)
            ivStar4.setImageResource(R.drawable.feedback_deactive)
            ivStar5.setImageResource(R.drawable.feedback_deactive)
            rating = 3
        }
        ivStar4.setOnClickListener {
            ivStar1.setImageResource(R.drawable.feedback_active)
            ivStar2.setImageResource(R.drawable.feedback_active)
            ivStar3.setImageResource(R.drawable.feedback_active)
            ivStar4.setImageResource(R.drawable.feedback_active)
            ivStar5.setImageResource(R.drawable.feedback_deactive)
            rating = 4
        }
        ivStar5.setOnClickListener {
            ivStar1.setImageResource(R.drawable.feedback_active)
            ivStar2.setImageResource(R.drawable.feedback_active)
            ivStar3.setImageResource(R.drawable.feedback_active)
            ivStar4.setImageResource(R.drawable.feedback_active)
            ivStar5.setImageResource(R.drawable.feedback_active)
            rating = 5
        }
        cancel_action.setOnClickListener { v: View? ->
            hideKeyboard()
            dialog.dismiss()
        }
        rlSubmit.setOnClickListener { doRating2Update(rating, event, dialog) }
        dialog.show()
    }
    private fun doRating2Update(rating: Int, event: Event?, dialog: Dialog) {
        if (ConstUtility.isNetworkConnectivity(this@SearchEventActivity)) {
            val userDetail = model.userDetails.value
            val ratingRequest = RatingRequest()
            ratingRequest.email = userDetail?._email
            ratingRequest.eventId = Integer.valueOf(event?.id?:0)
            ratingRequest.rating = rating
            ratingRequest.name = userDetail?.name
            ratingRequest.type = "event"
            val gson = GsonBuilder().disableHtmlEscaping().create()
            val json = gson.toJson(ratingRequest)
            var jsonObj: JSONObject? = null
            try {
                jsonObj = JSONObject(json)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val req: JsonObjectRequest = object : JsonObjectRequest(
                BuildConfig.BASE_URL + C.API_ADD_RATING, jsonObj,
                Response.Listener { response ->
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4))
                        val ratingResponse = gson.fromJson(
                            response.toString(),
                            RatingResponse::class.java
                        )
                        if (ratingResponse.getStatus() == C.NP_STATUS_SUCCESS) {
                            toast(R.string.thanks_for_rating)
                            dialog.dismiss()

                        } else {
                            toast(ratingResponse.getMessage())
                            dialog.dismiss()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    VolleyLog.e("Error: ", error.message)
                    dialog.dismiss()
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return ConstUtility.getMD5EncryptedString(json)
                }
            }
            val policy: RetryPolicy = DefaultRetryPolicy(
                C.API_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            req.retryPolicy = policy
            val requestQueue = Volley.newRequestQueue(this@SearchEventActivity)
            requestQueue.add(req)
        } else {
            toast(R.string.no_internet_connection)
        }
    }


}