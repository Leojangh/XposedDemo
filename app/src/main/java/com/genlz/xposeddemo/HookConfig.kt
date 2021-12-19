package com.genlz.xposeddemo

interface HookConfig {

    fun getSupportApplications(): Set<String>

    class Builder {

        private val supportPackages = mutableSetOf<String>()

        fun addSupportApplication(packageName: String): Builder {
            supportPackages += packageName
            return this
        }

        fun build(): HookConfig {
            return object : HookConfig {
                override fun getSupportApplications() = supportPackages
            }
        }
    }
}