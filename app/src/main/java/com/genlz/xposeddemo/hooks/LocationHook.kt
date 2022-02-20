package com.genlz.xposeddemo.hooks

import android.os.Build
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.spi.HookerProvider
import com.genlz.xposeddemo.util.getApplicationContext
import de.robv.android.xposed.callbacks.XC_LoadPackage

class LocationHook internal constructor(config: HookConfig) : Hooker(config) {

    override suspend fun onLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam) {
        if (lpParam.packageName !in config.getSupportApplications()) return
        if (Build.VERSION.SDK_INT !in config.getSupportSdkVersions()) return

        val context = getApplicationContext()
    }

    class Provider : HookerProvider<LocationHook> {
        override fun provideHooker() = LocationHook(HookConfig.Builder().apply {
            supportApplications = setOf("android")
            supportSdkVersions = Build.VERSION_CODES.P..Build.VERSION_CODES.R
        }.build())
    }
}