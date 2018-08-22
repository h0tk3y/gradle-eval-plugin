package com.github.h0tk3y.gradle.eval

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class GradleEvalTask : DefaultTask() {
    @get:Input
    @set:Option(option = "command", description = "Groovy command to evaluate")
    lateinit var evalCommand: String

    @TaskAction
    fun eval() {
        val shell = project.groovyShell()
        println(shell.evalWithDelegate(project, evalCommand))
    }
}