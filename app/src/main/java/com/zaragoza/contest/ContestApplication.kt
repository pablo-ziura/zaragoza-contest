package com.zaragoza.contest

import android.app.Application
import com.zaragoza.contest.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ContestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ContestApplication)
            modules(userModule).allowOverride(true)
        }
    }
}