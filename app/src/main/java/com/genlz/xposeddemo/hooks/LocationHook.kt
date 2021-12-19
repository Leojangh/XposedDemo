package com.genlz.xposeddemo.hooks

import android.location.Location
import android.location.LocationListener
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.spi.HookerProvider
import com.genlz.xposeddemo.util.hook
import de.robv.android.xposed.callbacks.XC_LoadPackage

class LocationHook : Hooker {

    override suspend fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        val method =
            LocationListener::class.java.getMethod("onLocationChanged", Location::class.java)
        method.hook {
            val location = args[0] as Location
            location.latitude = .0
            location.longitude = .0
        }
    }

    class Provider : HookerProvider<LocationHook> {
        override fun provideHooker(): LocationHook {
            return LocationHook()
        }
    }
}