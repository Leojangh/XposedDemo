package com.genlz.xposeddemo.util

import de.robv.android.xposed.XC_MethodHook

/**
 * A simple wrapper for [XC_MethodHook.MethodHookParam],it indicates [status] additionally,
 * before or after.
 */
internal data class StatefulMethodHookParam(
    val base: XC_MethodHook.MethodHookParam,
    val status: Int
) {
    companion object {
        const val STATUS_BEFORE = 0
        const val STATUS_AFTER = 1
    }
}