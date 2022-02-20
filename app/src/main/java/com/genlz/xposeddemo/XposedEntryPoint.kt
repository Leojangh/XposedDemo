package com.genlz.xposeddemo

import com.genlz.xposeddemo.di.CoroutinesModule
import com.genlz.xposeddemo.di.XposedInjector
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
    @Component(modules = [XposedInjector::class])
    interface Hooks {
        fun hooks(): Set<Hooker>
    }

    private val hooks = DaggerXposedEntryPoint_Hooks.create().hooks()

    @Volatile
    private var handleLoadPackageInvoked = false //Maybe wrong

    @Volatile
    private var handleInitPackageResourcesInvoked = false //Maybe wrong

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        //Main looper is available if this is in 'Binder:interceptor' thread.
        //But it seems that duplicate invocation happens and I don't know why.
//        if (Thread.currentThread().name != INTERCEPTOR_THREAD || handleLoadPackageInvoked) return
//        handleLoadPackageInvoked = true
        //
        xposedScope.launch { hooks.forEach { it.onLoadPackage(lpparam) } }
    }

    /**
     * Note:The main looper is not initialized yet when initialize zygote.So the [Dispatchers.Main] is
     * not available.
     */
    @Throws(Throwable::class)
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        xposedScope.launch { hooks.forEach { it.onInitZygote(startupParam) } }
    }

    @Throws(Throwable::class)
    override fun handleInitPackageResources(
        resparam: XC_InitPackageResources.InitPackageResourcesParam
    ) {
//        if (Thread.currentThread().name != INTERCEPTOR_THREAD || handleInitPackageResourcesInvoked) return
//        handleInitPackageResourcesInvoked = true

        xposedScope.launch { hooks.forEach { it.onInitPackageRes(resparam) } }
    }

    companion object {
        private const val INTERCEPTOR_THREAD = "Binder:interceptor"
        private const val TAG = "LSP"
    }
}


