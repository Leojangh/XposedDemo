package com.genlz.xposeddemo.hooks

import android.location.Location
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.spi.HookerProvider
import com.genlz.xposeddemo.util.xlog
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class LocationHook private constructor(
    config: HookConfig
) : Hooker(config) {

    override suspend fun hook(lpParam: XC_LoadPackage.LoadPackageParam) {
        val clazz = XposedHelpers.findClass(
            "android.location.LocationManager\$GetCurrentLocationTransport",
            lpParam.classLoader
        )
        val onLocation = XposedHelpers.findMethodExact(
            clazz,
            "onLocation",
            Location::class.java
        )
        xlog(onLocation)
    }

    class Provider : HookerProvider<LocationHook> {
        override fun provideHooker(): LocationHook {
            return LocationHook(HookConfig.Builder().build())
        }
    }
}