# gradle-eval-plugin
A tiny plugin for Gradle projects debugging via command line

[![Build Status](https://travis-ci.com/h0tk3y/gradle-eval-plugin.svg?branch=master)](https://travis-ci.com/h0tk3y/gradle-eval-plugin)

## Usage

This plugin allows inspecting a Gradle build directly from the command line, without editing the build scripts when that is not really necessary.
for example:

```bash
./gradlew eval --command='configurations.compileClasspath.files'
```

The plugin can be applied normally to a single project as follows:

```groovy
plugins {
    id 'com.github.h0tk3y.gradle.eval' version '0.0.1'
}
```

However, it may be more practical to apply it with a [Gradle init script](https://docs.gradle.org/current/userguide/init_scripts.html):

```groovy
initscript {
    repositories {
        jcenter()
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath 'com.github.h0tk3y.gradle.eval:gradle-eval:0.0.1'
    }
}

allprojects {
    apply plugin: com.github.h0tk3y.gradle.eval.GradleEvalPlugin
}
```

### Tasks:  `eval` and `projectRepl`

The plugin adds two tasks:

* `eval` for running a single command with no state saved
    * the command is passed via the command line option `--command='...'`
    
* `projectRepl` for running a REPL in the context of the project
    * Note: Gradle's rich console features disrupt the jline functionality. It is recommended to run `projectRepl` with the 
      `--console=plain` command line flag to minimize harm from the rich console.