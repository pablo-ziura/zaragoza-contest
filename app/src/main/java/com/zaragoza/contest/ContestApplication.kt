package com.zaragoza.contest

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.zaragoza.contest.di.questionModule
import com.zaragoza.contest.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ContestApplication : Application(), CameraXConfig.Provider {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ContestApplication)
            modules(userModule, questionModule).allowOverride(true)
        }
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}