package com.genlz.xposeddemo.di

import android.os.Build
import com.genlz.xposeddemo.BuildConfig
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.hooks.LocationHook
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object XposedInjector {

    /**
     * Config [LocationHook] at here.
     */
    @Singleton
    @Provides
    fun provideLocationHookConfig() = object : HookConfig {

        override var supportApplications = setOf("android")

        override var supportSdkVersions = Build.VERSION_CODES.P..Build.VERSION_CODES.R
    }

    @Singleton
    @Provides
    fun provideHooks(hook: LocationHook): Set<Hooker> {
        //TODO:How to return all Hooker?
        return setOf(hook)
    }
}