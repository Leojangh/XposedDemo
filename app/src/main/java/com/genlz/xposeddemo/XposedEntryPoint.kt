package com.genlz.xposeddemo

import com.genlz.xposeddemo.spi.HookerProvider
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

class XposedEntryPoint : IXposedHookLoadPackage {

    private val hookConfig = HookConfig.Builder()
        .addSupportApplication("com.genlz.jetpacks.debug")
        .addSupportApplication("com.google.android.youtube")
        .build()

    /**
     * The xposed class loader.
     */
    private val xposedClassLoader = javaClass.classLoader ?: error("Xposed class loader is null!")

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName !in hookConfig.getSupportApplications()) return

        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()).launch {
            for (provider in ServiceLoader.load(HookerProvider::class.java, xposedClassLoader)) {
                provider.provideHooker().hook(lpparam)
            }
        }
    }

}


