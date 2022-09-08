package com.np.namasteyoga.common

import com.np.namasteyoga.interfaces.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ApplicationSchedulerProvider : SchedulerProvider {
    override fun io() = Schedulers.io()

    override fun ui() = AndroidSchedulers.mainThread()

    override fun computation() = Schedulers.computation()
}