package com.genlz.xposeddemo

import android.os.Build
import com.genlz.xposeddemo.di.CoroutinesModule
import com.genlz.xposeddemo.di.HookModule
import dagger.Component
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

/**
 * System classloader:dalvik.system.PathClassLoader
 */
class XposedEntryPoint : IXposedHookLoadPackage,
    IXposedHookInitPackageResources,
    IXposedHookZygoteInit {

    companion object {
        @JvmStatic
        private val API = Build.VERSION.SDK_INT
    }

    private val xposedScope = DaggerXposedEntryPoint_Scope.create().xposedScope()

    @Singleton
    @Component(modules = [CoroutinesModule::class])
    interface Scope {
        fun xposedScope(): CoroutineScope
    }

    @Singleton
    @Component(modules = [HookModule::class])
    interface HookComponent {
        fun hooks(): Set<@JvmSuppressWildcards Hook>
    }

    private val hooks = DaggerXposedEntryPoint_HookComponent.create().hooks()

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        xposedScope.launch {
            hooks.forEach {
                if (it is SuspendableIXposedHookLoadPackage
                    && lpparam.packageName in it.config.supportedPackages
                    && API in it.config.supportSdkVersions
                ) {
                    it.handleLoadPackage(lpparam)
                }
            }
        }
    }

    /**
     * Note:The main looper is not initialized yet when initialize zygote.So the [Dispatchers.Main] is
     * not available.
     */
    @Throws(Throwable::class)
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        xposedScope.launch {
            hooks.forEach {
                if (it is SuspendableIXposedHookZygoteInit
                    && API in it.config.supportSdkVersions
                ) {
                    it.initZygote(startupParam)
                }
            }
        }
    }

    @Throws(Throwable::class)
    override fun handleInitPackageResources(
        resparam: XC_InitPackageResources.InitPackageResourcesParam
    ) {
        xposedScope.launch {
            hooks.forEach {
                if (it is SuspendableIXposedHookInitPackageResources
                    && resparam.packageName in it.config.supportedPackages
                    && API in it.config.supportSdkVersions
                ) {
                    it.handleInitPackageResources(resparam)
                }
            }
        }
        //lsp classloader
//        val classLoader = javaClass.classLoader!!
//        xlog("system classloader:${ClassLoader.getSystemClassLoader()}")
//        xlog("Lsp classloader:${classLoader}")
    }
}


