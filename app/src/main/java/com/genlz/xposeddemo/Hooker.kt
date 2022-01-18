package com.genlz.xposeddemo

import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class Hooker(val config: HookConfig) {

    abstract suspend fun hook(lpParam: XC_LoadPackage.LoadPackageParam)
}