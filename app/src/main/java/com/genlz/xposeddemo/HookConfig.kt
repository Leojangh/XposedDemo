package com.genlz.xposeddemo

interface HookConfig {

    fun getSupportApplications(): Set<String>

    fun getSupportSdkVersions(): IntRange

    class Builder {

        var supportApplications = setOf<String>()

        var supportSdkVersions = IntRange(BuildConfig.MIN_SDK, BuildConfig.TARGET_SDK)

        fun build(): HookConfig = object : HookConfig {

            override fun getSupportApplications() = supportApplications

            override fun getSupportSdkVersions() = supportSdkVersions
        }
    }
}