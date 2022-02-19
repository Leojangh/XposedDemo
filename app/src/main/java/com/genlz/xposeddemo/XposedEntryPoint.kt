package com.genlz.xposeddemo

import android.os.Handler
import android.os.Looper
import com.genlz.xposeddemo.spi.HookerProvider
import com.genlz.xposeddemo.util.xlog
import com.genlz.xposeddemo.util.xposedDispatcher
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Executor

class XposedEntryPoint :
    IXposedHookLoadPackage,
    IXposedHookInitPackageResources,
    IXposedHookZygoteInit {

    private val uncaughtExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        xlog(throwable)
    }

    private val xposedScope =
        CoroutineScope(Dispatchers.xposedDispatcher + SupervisorJob() + uncaughtExceptionHandler)

    private val hooks = ServiceLoader.load(
        HookerProvider::class.java,
        javaClass.classLoader/*xposed plugin classloader*/
    ).map { it.provideHooker() }

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
    }
}


