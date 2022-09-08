package com.np.namasteyoga.comman


import com.np.namasteyoga.interfaces.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single


fun Completable.with(schedulerProvider: SchedulerProvider): Completable =
    this.observeOn(schedulerProvider.ui()).subscribeOn(schedulerProvider.io())


fun <T> Single<T>.with(schedulerProvider: SchedulerProvider): Single<T> =
    this.observeOn(schedulerProvider.ui()).subscribeOn(schedulerProvider.io())