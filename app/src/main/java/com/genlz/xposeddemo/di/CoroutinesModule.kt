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
        dispatcher: CoroutineDispatcher, coroutineExceptionHandler: CoroutineExceptionHandler
    ) = CoroutineScope(dispatcher + coroutineExceptionHandler + SupervisorJob())

    @Singleton
    @Provides
    fun provideXposedThreadPool(): ExecutorService {
        val availableProcessors = Runtime.getRuntime().availableProcessors()
        return ThreadPoolExecutor(
            availableProcessors,       // Initial pool size
            availableProcessors,       // Max pool size
            1L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            object : ThreadFactory {
                var i = 0
                override fun newThread(it: Runnable) = Thread(it).apply { name = "xposed-pool-thread-${i++}" }
            }) { r, _ ->
            Handler(Looper.getMainLooper()).post(r)//Run on main thread.
        }
    }

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(executor: ExecutorService): CoroutineDispatcher = executor.asCoroutineDispatcher()

    @OptIn(ExperimentalStdlibApi::class)
    @Singleton
    @Provides
    fun provideCoroutineExceptionHandler(
        dispatcher: CoroutineDispatcher
    ) = CoroutineExceptionHandler { context, throwable ->

        when (context[CoroutineDispatcher.Key]) {
            dispatcher -> xlog(throwable)
            else -> Log.d("TAG", "", throwable)
        }
    }
}