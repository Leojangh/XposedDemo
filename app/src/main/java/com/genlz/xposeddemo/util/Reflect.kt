package com.genlz.xposeddemo.util

import java.lang.reflect.Modifier


fun printClass(clazz: Class<*>) {
    val superclass = clazz.superclass
    val modifiers = Modifier.toString(clazz.modifiers)
    val interfaces = clazz.interfaces
    val i = if (interfaces.isNullOrEmpty()) "" else "implements ${interfaces.joinToString()}"
    if (modifiers.isNotEmpty()) xlog("$modifiers class ${clazz.canonicalName} extends ${superclass?.canonicalName} $i {")
    printFields(clazz)
    xlogln()
    printConstructors(clazz)
    xlogln()
    printMethods(clazz)
    xlogln("}")
}

fun printConstructors(clazz: Class<*>) {
    for (c in clazz.declaredConstructors) {
        val modifiers = Modifier.toString(c.modifiers)
        val params = c.parameterTypes.joinToString { "${it.canonicalName}" }
        xlog("\t$modifiers ${c.name}($params);")
    }
}

fun printMethods(clazz: Class<*>) {
    for (method in clazz.declaredMethods) {
        val modifiers = Modifier.toString(method.modifiers)
        val params = method.parameterTypes.joinToString { "${it.canonicalName}" }
        xlog("\t$modifiers ${method.returnType.canonicalName} ${method.name}($params);")
    }
}

fun printFields(clazz: Class<*>) {
    for (field in clazz.declaredFields) {
        val modifiers = Modifier.toString(field.modifiers)
        xlog("\t$modifiers ${field.type.canonicalName} ${field.name};")
    }
}