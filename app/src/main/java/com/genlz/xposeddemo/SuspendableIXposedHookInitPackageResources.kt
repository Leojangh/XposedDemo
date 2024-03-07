package com.genlz.xposeddemo

import de.robv.android.xposed.callbacks.XC_InitPackageResources

fun interface SuspendableIXposedHookInitPackageResources {
    suspend fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam)
}