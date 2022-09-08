package com.np.namasteyoga.ui.health

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.np.namasteyoga.R
import com.np.namasteyoga.utils.GraphChartUtils
import java.util.*

/**
 * Created by Durgesh on 15/03/21.
 * Custom View for BarChart
 */
class GraphChart2 : FrameLayout {
    private var barSpaces = 0
    private var barTextColor = 0

    private var barDimension = 0
    private var barTextSize = 0f
    private var barColor = 0
    private var barMaxValue = 0
    private var linearParent: LinearLayout? = null
    private var isBarAdded = false
    private var onBarClickListener: OnBarClickListener? = null
    private val barChartModels: MutableList<GraphChartModel?> = ArrayList()

    constructor(context: Context?) : super(context!!) {

        initChart()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.GraphChart, 0, 0)
        barDimension = a.getDimensionPixelSize(
            R.styleable.GraphChart_bar_width,
            GraphChartUtils.convertDpToPix(20f, context) .toInt()
        )
        barColor = a.getColor(R.styleable.GraphChart_bar_color, GraphChartUtils.BAR_CHART_COLOR_DEFAULT)
        barTextSize = a.getDimensionPixelSize(
            R.styleable.GraphChart_bar_text_size,
            GraphChartUtils.convertDpToPix(13F, context).toInt()
        ).toFloat()
        barTextSize = GraphChartUtils.convertPixToDp(barTextSize, context)
        barTextColor = a.getColor(
            R.styleable.GraphChart_bar_text_color,
            GraphChartUtils.BAR_CHART_TEXT_COLOR_DEFAULT
        )
        barMaxValue = a.getInt(R.styleable.GraphChart_bar_max_value, 0)
        barSpaces = a.getDimensionPixelSize(
            R.styleable.GraphChart_bar_spaces,
            GraphChartUtils.convertDpToPix(GraphChartUtils.BAR_CHART_SPACE, context).toInt()
        )
        a.recycle()
        initChart()
    }


    fun setBarMaxValue(barMaxValue: Int) {
        this.barMaxValue = barMaxValue
        clearAll()
    }




    private fun initChart() {
        linearParent = LinearLayout(context)
        linearParent?.orientation = LinearLayout.HORIZONTAL
        linearParent?.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        linearParent!!.gravity = Gravity.BOTTOM

        this.addView(linearParent)
    }



    private fun createChart(position: Int, dimension: Int, barChartModel: GraphChartModel) {
        if (dimension == 0 || barMaxValue == 0) {
            return
        }
        val view = LayoutInflater.from(context).inflate(R.layout.chart_item_layout_2, linearParent, false)
        updateUi(position, dimension, null, barChartModel, view)
    }

    private fun updateUi(
        position: Int, dimension: Int, barChartModelInit: GraphChartModel?,
        barChartModel: GraphChartModel,
        view: View
    ) {

        val dimensionBar = dimension * barChartModel.barValue / barMaxValue
        val layoutParamsBar = view.layoutParams as MarginLayoutParams
        val monthName = view.findViewById<TextView>(R.id.monthName)
//        val monthValue = view.findViewById<TextView>(R.id.monthValue)

        monthName.textSize = barTextSize
        monthName.setTextColor(barTextColor)
        monthName.text = barChartModel.barText

//        monthValue.textSize = barTextSize
//        monthValue.setTextColor(barTextColor)
//        monthValue.text = barChartModel.barValue.toString()
//        monthValue.text = barChartModel.barText


        view.setOnClickListener {

            onBarClickListener?.onBarClick(barChartModel)
        }
        view.tag = barChartModel
        view.layoutParams.width = barDimension
        if (barChartModelInit == null) {
            if (isBarAdded) {
                layoutParamsBar.leftMargin = barSpaces
                layoutParamsBar.rightMargin = barSpaces
            }
            if (position == -1) {
                linearParent?.addView(view)
            } else if (position <= linearParent!!.childCount) {
                linearParent!!.addView(view, position)
            }
        }
        isBarAdded = true
    }



    private fun updateChart(
        dimension: Int,
        barChartModelInit: GraphChartModel?,
        barChartModel: GraphChartModel
    ) {
        val view = linearParent!!.findViewWithTag<View>(barChartModelInit)
        updateUi(-1, dimension, barChartModelInit, barChartModel, view)
    }

    private fun removeBarInternal(barChartModel: GraphChartModel?) {
        linearParent?.removeView(linearParent!!.findViewWithTag(barChartModel))
    }


    private fun getDimension(
        view: View?,
        listener: DimensionReceivedCallback
    ) {
        view!!.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                listener.onDimensionReceived(view.height)
            }
        })
    }

    fun setOnBarClickListener(onBarClickListener: OnBarClickListener?) {
        this.onBarClickListener = onBarClickListener
    }

    fun addBar(barChartModelList: List<GraphChartModel?>) {
        for (barChartModel in barChartModelList) {
            if (barChartModel != null) {
                addBar(barChartModels.size, barChartModel)
            }
        }
    }

    fun addBar(barChartModel: GraphChartModel?) {
        addBar(barChartModels.size, barChartModel)
    }

    fun addBar(position: Int, barChartModel: GraphChartModel?) {
        if (position > barChartModels.size) {
            return
        }
        barChartModels.add(position, barChartModel)
        if (barChartModel != null) {
            if (linearParent!!.height == 0) {
                getDimension( linearParent, object : DimensionReceivedCallback {
                    override fun onDimensionReceived(dimension: Int) {
                        createChart(position, dimension, barChartModel)
                    }
                })
            } else {
                createChart(position, linearParent!!.height, barChartModel)
            }
        }
    }

    fun updateBar(index: Int, barChartModel: GraphChartModel?) {
        if (index >= barChartModels.size || barChartModel == null) {
            return
        }
        val barChartModelInit = barChartModels[index]
        barChartModels[index] = barChartModel
        if (linearParent!!.height == 0) {
            getDimension( linearParent, object : DimensionReceivedCallback {
                override fun onDimensionReceived(dimension: Int) {
                    updateChart(dimension, barChartModelInit, barChartModel)
                }
            })
        } else {
            updateChart(linearParent!!.height, barChartModelInit, barChartModel)
        }
    }

    fun removeBar(barChartModel: GraphChartModel?) {
        barChartModels.remove(barChartModel)
        removeBarInternal(barChartModel)
    }

    fun removeBar(index: Int) {
        if (index < barChartModels.size) {
            val barChartModel = barChartModels.removeAt(index)
            removeBarInternal(barChartModel)

        }
    }

    fun clearAll() {
        barChartModels.clear()
        if (linearParent != null) {
            linearParent!!.removeAllViews()
        }

    }



    private interface DimensionReceivedCallback {
        fun onDimensionReceived(dimension: Int)
    }

    interface OnBarClickListener {
        fun onBarClick(barChartModel: GraphChartModel?)
    }
}