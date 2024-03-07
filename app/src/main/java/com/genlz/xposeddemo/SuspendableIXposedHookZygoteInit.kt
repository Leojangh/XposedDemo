package com.genlz.xposeddemo

import de.robv.android.xposed.IXposedHookZygoteInit

fun interface SuspendableIXposedHookZygoteInit {
    suspend fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam)
}