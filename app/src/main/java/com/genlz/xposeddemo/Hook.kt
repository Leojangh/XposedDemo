package com.genlz.xposeddemo

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import java.util.concurrent.Executor

abstract class Hook {

    //Wait to main looper initialized.
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    init {
        @OptIn(ExperimentalCoroutinesApi::class)
        Dispatchers.setMain(Executor {
            handler.post(it)
        }.asCoroutineDispatcher())
    }

    abstract val config: HookConfig
}