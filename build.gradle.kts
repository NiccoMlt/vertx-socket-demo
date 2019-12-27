import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
  kotlin("jvm") version "1.3.61"
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

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
}

vertx {
  mainVerticle = "org.example.vertx.MainVerticle"
  vertxVersion = "3.8.4"
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
