package com.genlz.xposeddemo.di;

import com.genlz.xposeddemo.Hook;
import com.genlz.xposeddemo.hooks.PackageManagerHook;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import de.robv.android.xposed.XposedBridge;
import kotlin.jvm.JvmSuppressWildcards;

@Retention(RetentionPolicy.RUNTIME)
public @interface Container {
    Class<? extends Hook>[] value();
}
