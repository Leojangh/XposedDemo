package com.genlz.xposeddemo.hooks

import com.genlz.xposeddemo.Hook
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.util.xlog
import de.robv.android.xposed.callbacks.XC_LoadPackage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationHook @Inject constructor(config: HookConfig) : Hook(config) {

    override suspend fun onLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam) {
//        if (lpParam.packageName !in config.getSupportApplications()) return
//        if (Build.VERSION.SDK_INT !in config.getSupportSdkVersions()) return
        xlog(666)
    }
}