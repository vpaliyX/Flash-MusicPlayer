package com.vpaliy.mediaplayer.domain.executor

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchedulerProvider @Inject constructor():BaseScheduler{
    override fun io():Scheduler=Schedulers.io()
    override fun ui():Scheduler=AndroidSchedulers.mainThread()
}
