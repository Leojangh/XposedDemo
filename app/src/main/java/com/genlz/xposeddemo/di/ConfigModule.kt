package com.genlz.xposeddemo.di

import android.os.Build
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.hooks.LocationHook
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ConfigModule {

    /**
     * Config [LocationHook] at here.
     */
    @Provides
    @Singleton
    fun provideLocationHookConfig() = object : HookConfig {

        override var supportApplications = setOf("android")

        override var supportSdkVersions = Build.VERSION_CODES.P..Build.VERSION_CODES.R
    }
}