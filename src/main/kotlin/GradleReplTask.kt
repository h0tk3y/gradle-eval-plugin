package com.github.h0tk3y.gradle.eval

import groovy.lang.Binding
import groovy.lang.GroovyRuntimeException
import groovy.lang.GroovyShell
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

open class GradleReplTask : DefaultTask() {
    @TaskAction
    fun runRepl() {
        val shell = project.groovyShell()
        val terminal = TerminalBuilder.builder().streams(System.`in`, System.out).build()
        replLoop(terminal, shell)
    }

    private fun replLoop(terminal: Terminal, shell: GroovyShell) {
        val lineReader = LineReaderBuilder.builder().appName(appName).terminal(terminal).build()
        var binding = Binding()

        loop@while (true) {
            val command = try {
                lineReader.readLine(">>> ")
            } catch (e: UserInterruptException) {
                continue
            } catch (e: EndOfFileException) {
                return
            }

            when (command) {
                in helpCommands -> printHelp()
                in quitCommands -> return
                in resetCommands -> binding = Binding()
                else -> {
                    val (result, newBinding) = try {
                        shell.evalWithDelegate(project, command, binding)
                    } catch (e: GroovyRuntimeException) {
                        println(e.message)
                        continue@loop
                    }
                    println(result)
                    binding = newBinding
                }
            }
        }
    }

    companion object {
        private val appName = "com.github.h0tk3y.gradle.eval - Gradle REPL"
        private val helpCommands = setOf("help", ":help", "/help")
        private val quitCommands = setOf(":quit", "/quit", ":q", "/q")
        private val resetCommands = setOf(":reset", "/reset")
    }

    fun printHelp() {
        println("""
            === $appName
            ${helpCommands.joinToString()} - print this message;
            ${quitCommands.joinToString()} - exit REPL;
            ${resetCommands.joinToString()} - reset the script binding;

        """.trimIndent())
    }
}