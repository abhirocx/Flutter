package com.np.namasteyoga.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.DisplayMetrics
import java.util.*

/**
 * Created by Durgesh on 10/02/21.
 * Chart Utils
 */
object GraphChartUtils {

    val BAR_CHART_COLOR_DEFAULT: Int = Color.parseColor("#303030")
//    val BAR_CHART_TEXT_COLOR_DEFAULT: Int = Color.parseColor("#808080")
    val BAR_CHART_TEXT_COLOR_DEFAULT: Int = Color.parseColor("#363636")
    const val BAR_CHART_SPACE = 5F


    fun convertDpToPix(dp: Float, context: Context): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    fun getRandomValue(): Int {
        val rnd = Random()
        return rnd.nextInt(6000)
    }
    fun getColor(): Int {
        return Color.argb(80, 78, 149, 51)
    }
}