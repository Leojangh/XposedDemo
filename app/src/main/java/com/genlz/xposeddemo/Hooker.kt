package com.genlz.xposeddemo

import de.robv.android.xposed.callbacks.XC_LoadPackage

fun interface Hooker {

    val xposedClassLoader: ClassLoader
        get() = javaClass.classLoader ?: error("Xposed class loader is null!")

    suspend fun hook(lpparam: XC_LoadPackage.LoadPackageParam)
}