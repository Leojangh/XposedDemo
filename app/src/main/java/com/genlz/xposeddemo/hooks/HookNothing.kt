package com.genlz.xposeddemo.hooks

import android.view.View
import android.widget.Toast
import com.genlz.xposeddemo.HookConfig
import com.genlz.xposeddemo.Hooker
import com.genlz.xposeddemo.spi.HookerProvider
import com.genlz.xposeddemo.util.getApplicationContext
import com.genlz.xposeddemo.util.hook
import com.genlz.xposeddemo.util.xlog
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HookNothing private constructor(
    config: HookConfig
) : Hooker(config) {

    override suspend fun hook(lpParam: XC_LoadPackage.LoadPackageParam) {
        val applicationContext = getApplicationContext()
        val id = applicationContext.resources.getIdentifier(
            "content_thumbnail",
            "id",
            applicationContext.packageName
        )
        //Because inner coroutines will never complete.
        CoroutineScope(Dispatchers.Main).launch {
            View::class.java.getMethod(
                "setOnClickListener",
                View.OnClickListener::class.java
            ).hook {

            }
        }
        Toast.makeText(applicationContext, "666", Toast.LENGTH_SHORT).show()
        //R.id.countdown_text textview
        //R.id.content_thumbnail ImageView
    }

    class Provider : HookerProvider<HookNothing> {
        override fun provideHooker(): HookNothing {
            return HookNothing(HookConfig.Builder().build())
        }
    }
}