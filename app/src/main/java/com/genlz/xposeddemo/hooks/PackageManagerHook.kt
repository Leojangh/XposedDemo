package com.genlz.xposeddemo.hooks

import com.genlz.xposeddemo.Hook
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.util.xlog
import de.robv.android.xposed.callbacks.XC_LoadPackage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PackageManagerHook @Inject constructor(config: HookConfig) : Hook(config) {

    override suspend fun onLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam) {
        xlog(777)
    }
}