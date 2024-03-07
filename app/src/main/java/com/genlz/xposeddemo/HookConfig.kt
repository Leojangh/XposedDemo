package com.genlz.xposeddemo

interface HookConfig {

    val supportedPackages: Set<String>

    val supportSdkVersions: IntRange
}