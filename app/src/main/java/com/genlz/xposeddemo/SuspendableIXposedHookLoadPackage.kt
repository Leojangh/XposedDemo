package com.genlz.xposeddemo

import de.robv.android.xposed.callbacks.XC_LoadPackage

fun interface SuspendableIXposedHookLoadPackage {
    suspend fun handleLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam)
}