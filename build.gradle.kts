@file:Suppress("UnstableApiUsage")

import com.moowork.gradle.node.yarn.YarnTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
  kotlin("jvm") version "1.3.61"
  id("com.github.node-gradle.node") version "2.2.0"
  id("io.vertx.vertx-plugin") version "1.0.1"
  id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
}

group = "org.example.vertx"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))
  // io.vertx:vertx-core : Vert.x plugin includes vertx-core dependency with the specified version
  implementation("io.vertx:vertx-web") // Vert.x plugin enforces its version
  implementation("io.vertx:vertx-lang-kotlin") // Vert.x plugin enforces its version
  implementation("io.vertx:vertx-lang-kotlin-coroutines") // Vert.x plugin enforces its version
  implementation("ch.qos.logback:logback-classic:+") // Logback version is not important in this demo
  implementation("org.slf4j:slf4j-api:+") // SLF4J version is not important in this demo
}

val frontend: String = "src/main/frontend/"
val frontendOut: String = "$frontend/build"

node {
  version = "12.13.1"
  npmVersion = "6.13.2"
  yarnVersion = "1.21.1"
  download = true
  nodeModulesDir = File(frontend)
}

vertx {
  mainVerticle = "org.example.vertx.MainVerticle"
  vertxVersion = "3.8.4"
}

tasks {
  withType(KotlinCompile::class).all {
    kotlinOptions {
      allWarningsAsErrors = true
      jvmTarget = "1.8"
    }
  }

  val buildFrontend by creating(YarnTask::class) {
    args = listOf("build")
    inputs.files(
      "$frontend/package.json", // React package configuration
      "$frontend/yarn.lock", // dependencies lockfile
      "$frontend/tsconfig.json" // TypeScript config
    )
    inputs.dir("$frontend/src") // React sources
    inputs.dir(
      fileTree("$frontend/node_modules") // Node modules ...
        .exclude(".cache") // ... ignoring cache
    )
    outputs.dir(frontendOut)
    dependsOn("yarn")
  }

  val copyToWebRoot by creating(Copy::class) {
    from(frontendOut)
    destinationDir = File("$buildDir/classes/kotlin/main/webroot")
    dependsOn(buildFrontend)
  }

  "processResources"(ProcessResources::class) {
    dependsOn(copyToWebRoot)
  }
}

ktlint {
  version.set("0.36.0")
  verbose.set(true)
  outputToConsole.set(true)
  coloredOutput.set(true)
  reporters {
    reporter(ReporterType.CHECKSTYLE)
    reporter(ReporterType.JSON)
    reporter(ReporterType.PLAIN)
  }
}
