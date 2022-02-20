package com.genlz.xposeddemo

interface HookConfig {

    var supportApplications: Set<String>

    var supportSdkVersions: IntRange
}