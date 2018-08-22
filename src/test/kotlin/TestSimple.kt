package com.github.h0tk3y.gradle.eval

import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class TestSimple {
    @Rule @JvmField
    val tempDir = TemporaryFolder()

    lateinit var testProjectDir: File

    @Before
    fun setup() {
        testProjectDir = tempDir.newFolder("test")
    }

    @Test
    fun simpleEval() {
        val projectFiles = File("src/test/resources/simpleEval")
        projectFiles.copyRecursively(testProjectDir)

        val pluginClasspath = javaClass.classLoader.getResource("plugin-classpath.txt").readText().lines().map(::File)
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath(pluginClasspath)
            .withArguments("eval", "--command=project.sourceSets.each { println 'source set ' + it.name }")
            .build()

        Assert.assertTrue("source set main" in result.output)
        Assert.assertTrue("source set test" in result.output)
    }
}