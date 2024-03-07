package com.genlz.xposeddemo.hooks

import com.genlz.xposeddemo.Hook
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.SuspendableIXposedHookLoadPackage
import com.genlz.xposeddemo.util.getApplicationContext
import com.genlz.xposeddemo.util.printClass
import com.genlz.xposeddemo.util.xlog
import de.robv.android.xposed.callbacks.XC_LoadPackage
import javax.inject.Inject
import javax.inject.Singleton

class PackageManagerHook : Hook(),
    SuspendableIXposedHookLoadPackage {

    override val config: HookConfig = object : HookConfig {
        override val supportedPackages: Set<String> = setOf(
            "com.google.android.play.games",
        )
        override val supportSdkVersions = 28..34
    }

    override suspend fun handleLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam) {
        val context = getApplicationContext()
        printClass(context.javaClass)
    }
}