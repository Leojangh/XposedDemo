package com.genlz.xposeddemo.di

import com.genlz.xposeddemo.Hook
import com.genlz.xposeddemo.hooks.LocationHook
import com.genlz.xposeddemo.hooks.PackageManagerHook
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface HookModule {

    @Binds
    @IntoMap
    @ClassKey(LocationHook::class)
    fun provideLocationHook(hook: LocationHook): Hook

    @Binds
    @IntoMap
    @ClassKey(PackageManagerHook::class)
    fun providePackageManagerHook(hook: PackageManagerHook): Hook
}