package com.genlz.xposeddemo.util

import android.app.Application
import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.reflect.Method
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KClass

/**
 * Wrapper for [XposedBridge.log]
 */
fun xlog(t: Throwable) = XposedBridge.log(t)

/**
 * Wrapper for [XposedBridge.log]
 */
fun xlog(msg: String? = null) = XposedBridge.log(msg)

fun xlogln(any: Any? = null) = XposedBridge.log("${any ?: ""}\n")

fun xlog(any: Any? = null) = XposedBridge.log("${any ?: ""}")

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
suspend fun getApplicationContext(): Context = suspendCancellableCoroutine {
    XposedHelpers.findAndHookMethod(
        Application::class.java,
        "onCreate",
        object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                try {
                    val context = param.thisObject as Context
                    it.resume(context)
                } catch (t: Throwable) {
                    it.resumeWithException(t)
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
        if (it.status == StatefulMethodHookParam.STATUS_BEFORE)
            beforeHook(it.base)
        if (it.status == StatefulMethodHookParam.STATUS_AFTER)
            afterHook(it.base)
    }
}