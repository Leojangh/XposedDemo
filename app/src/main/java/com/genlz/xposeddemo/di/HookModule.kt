package com.genlz.xposeddemo.di

import com.genlz.xposeddemo.Hook
import com.genlz.xposeddemo.XposedEntryPoint
import com.genlz.xposeddemo.hooks.PackageManagerHook
import com.genlz.xposeddemo.util.xlog
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import de.robv.android.xposed.XposedBridge
import java.util.Arrays
import java.util.stream.Collectors
import javax.inject.Singleton

@Module
@Container(value = [PackageManagerHook::class])
object HookModule {

    @Provides
    @Singleton
    @ElementsIntoSet
    fun hooks(): Set<@JvmSuppressWildcards Hook> {
        val classLoader = javaClass.classLoader as BaseDexClassLoader
        val classes = javaClass.getAnnotation(Container::class.java)!!.value
        val set = Arrays.stream(classes).map {
            it.java.getConstructor().newInstance()
        }.collect(Collectors.toUnmodifiableSet())
        XposedBridge.log("Loading hooks:$set")
        return set
    }
}