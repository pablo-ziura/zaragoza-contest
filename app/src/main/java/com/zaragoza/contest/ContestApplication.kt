package com.zaragoza.contest

import android.app.Application
import com.zaragoza.contest.di.questionModule
import com.zaragoza.contest.di.scoreModule
import com.zaragoza.contest.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ContestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger()
            androidContext(this@ContestApplication)
            modules(userModule, questionModule, scoreModule).allowOverride(true)
        }
    }
}