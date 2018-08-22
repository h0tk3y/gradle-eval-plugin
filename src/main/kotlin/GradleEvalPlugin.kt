package com.github.h0tk3y.gradle.eval

import groovy.lang.Binding
import groovy.lang.GroovyShell
import groovy.util.DelegatingScript
import org.codehaus.groovy.control.CompilerConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project

open class GradleEvalPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        tasks.create("eval", GradleEvalTask::class.java).apply {
            group = "help"
        }

        tasks.create("projectRepl", GradleReplTask::class.java).apply {
            group = "help"
        }
    }
}

fun Project.groovyShell(): GroovyShell {
    val groovyCompilerConfiguration = CompilerConfiguration().apply {
        scriptBaseClass = DelegatingScript::class.java.name
    }
    return GroovyShell(project.buildscript.classLoader, Binding(), groovyCompilerConfiguration)
}

