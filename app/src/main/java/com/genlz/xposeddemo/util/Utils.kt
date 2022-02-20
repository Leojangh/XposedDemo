package com.genlz.xposeddemo.util

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.lang.reflect.Method
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * Wrapper for [XposedBridge.log]
 */
fun xlog(t: Throwable) = XposedBridge.log(t)

/**
 * Wrapper for [XposedBridge.log]
 */
fun xlogln(any: Any? = null) = XposedBridge.log("${any ?: ""}\n")

fun xlog(any: Any? = null) = XposedBridge.log("${any ?: "null"}")

/**
 * Return the class loadedBy by specified class loader.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<out T>.loadedBy(
    classLoader: ClassLoader
) = classLoader.loadClass(qualifiedName) as Class<T>

/**
 * Retrieve application context by hooking [Application.onCreate]
 */
@OptIn(InternalCoroutinesApi::class)
suspend fun getApplicationContext(): Context = suspendCancellableCoroutine {
    XposedHelpers.findAndHookMethod(
        Application::class.java,
        "onCreate",
        object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                try {
                    val context = param.thisObject as Context
                    it.tryResume(context)?.let { token ->
                        it.completeResume(token)
                    }
                } catch (t: Throwable) {
                    it.tryResumeWithException(t)?.let { token ->
                        it.completeResume(token)
                    }
                }
            }
        })
}

/**
 * Powered by coroutines.
 *
 * Note:It will never complete.
 */
suspend fun Method.hook(
    afterHook: suspend XC_MethodHook.MethodHookParam.() -> Unit = {},
    beforeHook: suspend XC_MethodHook.MethodHookParam.() -> Unit = {},
) {
    callbackFlow {
        val callback = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                trySend(
                    StatefulMethodHookParam(
                        param,
                        StatefulMethodHookParam.STATUS_BEFORE
                    )
                )
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                trySend(
                    StatefulMethodHookParam(
                        param,
                        StatefulMethodHookParam.STATUS_AFTER
                    )
                )
            }
        }
        XposedBridge.hookMethod(this@hook, callback)
        awaitClose()
    }.collect {
        when (it.status) {
            StatefulMethodHookParam.STATUS_BEFORE -> beforeHook(it.base)

            StatefulMethodHookParam.STATUS_AFTER -> afterHook(it.base)
        }
    }
}

/**
 * A simple wrapper for [XC_MethodHook.MethodHookParam],it indicates [status] additionally,
 * before or after.
 */
internal data class StatefulMethodHookParam(
    val base: XC_MethodHook.MethodHookParam,
    val status: Int
) {
    companion object {
        const val STATUS_BEFORE = 0
        const val STATUS_AFTER = 1
    }
}

private val availableProcessors = Runtime.getRuntime().availableProcessors()

internal val xposedDispatcherInternal = ThreadPoolExecutor(
    availableProcessors,       // Initial pool size
    availableProcessors,       // Max pool size
    1L,
    TimeUnit.SECONDS,
    LinkedBlockingQueue(),
    object : ThreadFactory {
        var i = 0
        override fun newThread(it: Runnable) =
            Thread(it).apply { name = "plugin-pool-thread-${i++}" }
    }
) { r, _ ->
    Handler(Looper.getMainLooper()).post(r)//Run on main thread.
}.asCoroutineDispatcher()


@Suppress("unused")
val Dispatchers.xposedDispatcher: CoroutineDispatcher
    get() = xposedDispatcherInternal