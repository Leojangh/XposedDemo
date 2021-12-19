package com.genlz.xposeddemo.config

import com.genlz.xposeddemo.HookConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainConfig {

    @Provides
    @Singleton
    fun provideHookConfig(): HookConfig {
        return object : HookConfig {
            override fun getSupportApplications(): Set<String> {
                return setOf(
                    "com.genlz.jetpacks.debug"
                )
            }
        }
    }
}