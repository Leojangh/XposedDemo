package com.genlz.xposeddemo

import android.os.Handler
import android.os.Looper
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import java.util.concurrent.Executor

abstract class Hooker(val config: HookConfig) {

    //Wait to main looper initialized.
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    init {
        @OptIn(ExperimentalCoroutinesApi::class)
        Dispatchers.setMain(Executor {
            handler.post(it)
        }.asCoroutineDispatcher())
    }

    open suspend fun onLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam) {}

    open suspend fun onInitZygote(startupParam: IXposedHookZygoteInit.StartupParam) {}

    open suspend fun onInitPackageRes(resParam: XC_InitPackageResources.InitPackageResourcesParam) {}
}