package com.np.namasteyoga.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.interfaces.ConnectionListener
import org.jetbrains.anko.find
import java.util.*


object UIUtils {

    fun setStatusBarGradient(activity: Activity, @ColorRes id: Int) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, id)
        //window.navigationBarColor = ContextCompat.getColor(activity, R.color.white)
    }

    fun setStatusBarGradientDash(activity: Activity, @ColorRes id: Int) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, id)
//        window.navigationBarColor = ContextCompat.getColor(activity, android.R.color.background_dark)
    }

    fun singleButtonPopup(
        ctx: Context?,
        @StringRes msg: Int = R.string.something_went_wrong,
        @StringRes title: Int = R.string.alert,
        @StringRes buttonText: Int = R.string.ok,
        isCancelable: Boolean = true,
        connectionListener: ConnectionListener? = null,
        msg2: String? = null
    ) {
        val dialog = AlertDialog.Builder(ctx)
        dialog.setTitle(title)
        if (msg2 == null)
            dialog.setMessage(msg)
        else
            dialog.setMessage(msg2)
        dialog.setCancelable(isCancelable)
        dialog.setPositiveButton(
            buttonText
        ) { dialog1: DialogInterface, _: Int ->
            connectionListener?.onClick()
            dialog1.dismiss()
        }
        dialog.create().also {
            it.show()
            it.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(ContextCompat.getColor(ctx!!, R.color.black))
                setBackgroundColor(ContextCompat.getColor(ctx, R.color.white))
            }
        }
    }

    fun yesOrNo(
        ctx: Context?,
        @StringRes msg: Int,
        @StringRes title: Int = R.string.alert,
        @StringRes buttonText: Int = R.string.ok,
        @StringRes buttonTextNegative: Int = R.string.no,
        isCancelable: Boolean = true,
        connectionListener: ConnectionListener? = null
    ) {
        val dialog = AlertDialog.Builder(ctx)
        dialog.setTitle(title)
        dialog.setMessage(msg)
        dialog.setCancelable(isCancelable)
        dialog.setPositiveButton(
            buttonText
        ) { dialog1: DialogInterface, _: Int ->
            connectionListener?.onClick()
            dialog1.dismiss()
        }
        dialog.setNegativeButton(
            buttonTextNegative
        ) { dialog1: DialogInterface, _: Int ->
            dialog1.dismiss()
        }
        dialog.create().also { a ->
            a.show()
            a.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                setTextColor(ContextCompat.getColor(ctx!!, R.color.light_black_text))
                setBackgroundColor(ContextCompat.getColor(ctx, R.color.white))
            }
            a.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(ContextCompat.getColor(ctx!!, R.color.black))
                setBackgroundColor(ContextCompat.getColor(ctx, R.color.white))
            }

        }

    }

    fun setColorSpan(
        view: TextView,
        fulltext: String,
        subtext: String
    ): Spannable {
        view.setText(fulltext, TextView.BufferType.SPANNABLE)
        val str = view.text as Spannable
        val i = fulltext.indexOf(subtext)
        str.setSpan(
            ForegroundColorSpan(Color.parseColor("#0091D9")),
            i,
            i + subtext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return str
    }

    fun showCustomToast(context: Context, steps: Int) {

        val customView = LayoutInflater.from(context).inflate(R.layout.toast_step_layout, null)
        val stepMsg = context.getString(R.string._d_steps, steps)
        customView.find<TextView>(R.id.stepCount).text = stepMsg

        val calMsg = context.getString(R.string._d_cal, Util.calculateStepToCal(steps))
        customView.find<TextView>(R.id.calCount).text = calMsg

        val disMsg = context.getString(R.string._s_km, Util.calculateStepToDistance(steps))
        customView.find<TextView>(R.id.distanceCount).text = disMsg

        Toast(context).apply {
            view = customView
            setGravity(Gravity.BOTTOM, 0, 120)
            duration = Toast.LENGTH_SHORT
            show()
        }
    }

    fun setToken(sharedPreferences: SharedPreferences) {


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Logger.Debug("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Logger.Debug(msg = token)
            SharedPreferencesUtils.setPushTokenNew(sharedPreferences, token)

        })
    }

    fun getUserType(role: Int?): Int {
        return when (role) {
            2 -> R.string.yoga_center
            3 -> R.string.yoga_trainer
            5 -> R.string.guest_user
            else -> R.string.empty
        }
    }

    fun String.getCapitalized(): String {
        val trimValue = trim().split(CommonString.SPACE)
        var output = trimValue[CommonInt.Zero].capitalize(Locale.getDefault())
        for (i in CommonInt.One..trimValue.lastIndex) {
            output += " ${trimValue[i].capitalize(Locale.getDefault())}"
        }
        return output

    }

    fun String.capitalizeString(): String {
        var retStr = this
        try { // We can face index out of bound exception if the string is null
            retStr = substring(0, 1).toUpperCase(Locale.getDefault()) + substring(1)
        } catch (e: Exception) {
        }
        return retStr
    }

    fun String.getSplit(): String {
        val trimValue = trim().split(CommonString.ATtheRate)
        var output = CommonString.Empty
        val data = trimValue[CommonInt.Zero].trim().capitalize(Locale.getDefault())
        if (data.isNotEmpty())
            output += data
        for (i in CommonInt.One..trimValue.lastIndex) {
            val data2 = trimValue[i].trim().capitalize(Locale.getDefault())
            if (data2.isNotEmpty())
                output += "${CommonString.ATtheRate}$data2"
        }
        return output

    }

    fun String.getDistanceFormat(): String {
        val trimValue = trim().split(CommonString.DOT)
        var output = trimValue[CommonInt.Zero]
        if (trimValue.size > CommonInt.One) {
            val tempString = trimValue[trimValue.lastIndex]
            if (tempString.length > 2) {
                output += ".${tempString.substring(0, 2)}"
            } else {
                output += ".${tempString}"
            }
        }

        return output

    }

    fun Context.navigateLocation(location: String) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$location")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }

    fun Context.shareData(data: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }


}