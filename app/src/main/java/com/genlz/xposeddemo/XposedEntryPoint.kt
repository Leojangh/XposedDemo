package com.genlz.xposeddemo

import com.genlz.xposeddemo.di.ConfigModule
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
class XposedEntryPoint : IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    private val xposedScope = DaggerXposedEntryPoint_Scope.create().xposedScope()

    @Singleton
    @Component(modules = [CoroutinesModule::class])
    interface Scope {
        fun xposedScope(): CoroutineScope
    }

    @Singleton
    @Component(modules = [ConfigModule::class, HookModule::class])
    interface HookComponent {
        fun hooks(): Map<Class<*>, Hook>
    }

    private val hooks = DaggerXposedEntryPoint_HookComponent.create().hooks()

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        xposedScope.launch { hooks.values.forEach { it.onLoadPackage(lpparam) } }
    }

    /**
     * Note:The main looper is not initialized yet when initialize zygote.So the [Dispatchers.Main] is
     * not available.
     */
    @Throws(Throwable::class)
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        xposedScope.launch { hooks.values.forEach { it.onInitZygote(startupParam) } }
    }

    @Throws(Throwable::class)
    override fun handleInitPackageResources(
        resparam: XC_InitPackageResources.InitPackageResourcesParam
    ) {
        xposedScope.launch { hooks.values.forEach { it.onInitPackageRes(resparam) } }
    }
}


