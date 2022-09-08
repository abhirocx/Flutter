package com.np.namasteyoga.ui.health


class GraphChartModel {
    var barValue = 0
    var barColor = 0
    var barText: String? = null

    constructor(barColor: Int, barValue: Int) {
        this.barValue = barValue
        this.barColor = barColor
    }

    constructor(barValue: Int, barColor: Int, barTag: Any?) {
        this.barValue = barValue
        this.barColor = barColor
    }

    constructor(barValue: Int, barColor: Int, barText: String?, barTag: Any?) {
        this.barValue = barValue
        this.barColor = barColor
        this.barText = barText
    }

    constructor(barColor : Int, barValue: Int, barText: String?) {
        this.barValue = barValue
        this.barColor = barColor
        this.barText = barText
    }
    constructor(barColor: Int,monthModel: FitnessDataModel?){
        this.barValue = monthModel?.stat?.toInt()?:0
        this.barColor = barColor
        this.barText = monthModel?.month
    }



    constructor()
}