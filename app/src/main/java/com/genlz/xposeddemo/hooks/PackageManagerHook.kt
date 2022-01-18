package com.genlz.xposeddemo.hooks

import android.util.Log
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.util.hook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class PackageManagerHook(config: HookConfig) : Hooker(config) {

    override suspend fun hook(lpParam: XC_LoadPackage.LoadPackageParam) {
//        XposedHelpers.findMethodExact(
//            "com.android.server.pm.PackageManagerService",
//            lpParam.classLoader,
//            "checkSignatures",
//            String::class.java,
//            String::class.java
//        )
//            .hook {
//            val pkg1 = args[0]
//            val pkg2 = args[1]
//            Log.d(TAG, "hook: $pkg1,$pkg2")
//        }
    }

    companion object {
        private const val TAG = "PackageManagerHook"
    }
}