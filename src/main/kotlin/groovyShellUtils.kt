package com.github.h0tk3y.gradle.eval

import groovy.lang.Binding
import groovy.lang.GroovyShell
import groovy.util.DelegatingScript

data class ResultAndBinding<T>(
    val result: T,
    val binding: Binding
)

fun GroovyShell.evalWithDelegate(delegate: Any, command: String, binding: Binding = Binding()): ResultAndBinding<Any?> {
    val script = parse(command) as DelegatingScript
    script.delegate = delegate
    script.binding = binding
    val result = script.run()
    return ResultAndBinding(result, script.binding)
}