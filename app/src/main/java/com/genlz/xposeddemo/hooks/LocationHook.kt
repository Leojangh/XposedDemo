package com.genlz.xposeddemo.hooks

import android.widget.Toast
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.spi.HookerProvider
import com.genlz.xposeddemo.util.getApplicationContext
import com.genlz.xposeddemo.util.xlog
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LocationHook : Hooker() {

    override suspend fun onLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam) {
        val context = getApplicationContext()
        xlog(context)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "666", Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun onInitZygote(startupParam: IXposedHookZygoteInit.StartupParam) {

    }

    override suspend fun onInitPackageRes(resParam: XC_InitPackageResources.InitPackageResourcesParam) {

    }

    class Provider : HookerProvider<LocationHook> {
        override fun provideHooker() = LocationHook
    }
}