package com.genlz.xposeddemo.spi

import com.genlz.xposeddemo.Hooker

@Deprecated("Use DI instead.SPI configuration is so redandunt.")
interface HookerProvider<T : Hooker> {
    fun provideHooker(): T
}