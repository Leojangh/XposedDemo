package com.genlz.xposeddemo.spi

import com.genlz.xposeddemo.Hooker

interface HookerProvider<T : Hooker> {
    fun provideHooker(): T
}