package com.zaragoza.contest

import android.app.Application
import com.zaragoza.contest.di.questionModule
import com.zaragoza.contest.di.scoreModule
import com.zaragoza.contest.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ContestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger(Level.DEBUG)
            androidContext(this@ContestApplication)
            modules(userModule, scoreModule, questionModule).allowOverride(true)
        }
    }
}