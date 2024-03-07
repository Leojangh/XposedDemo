package com.genlz.xposeddemo.di

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.genlz.xposeddemo.util.xlog
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.*
import java.util.concurrent.*
import javax.inject.Singleton

@Module
object CoroutinesModule {

    @Singleton
    @Provides
    fun provideXposedScope(
        coroutineExceptionHandler: CoroutineExceptionHandler
    ) = CoroutineScope(Dispatchers.Default + coroutineExceptionHandler + SupervisorJob())

    @Singleton
    @Provides
    fun provideCoroutineExceptionHandler(
    ) = CoroutineExceptionHandler { _, throwable ->
        xlog(throwable)
    }
}