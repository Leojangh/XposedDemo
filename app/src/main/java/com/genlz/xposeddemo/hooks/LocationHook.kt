package com.genlz.xposeddemo.hooks

import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.util.xlog
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationHook @Inject constructor(config: HookConfig) : Hooker(config) {

    override suspend fun onLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam) {
//        if (lpParam.packageName !in config.getSupportApplications()) return
//        if (Build.VERSION.SDK_INT !in config.getSupportSdkVersions()) return
        val packageName = "com.genlz.xposeddemo.hooks".replace("[.]".toRegex(), "/")
        xlog(ClassLoader.getSystemClassLoader())
        xlog(javaClass.classLoader)
        val dexFile = DexFile(".")
        for (entry in dexFile.entries()) {
            xlog(entry)
        }
    }
}