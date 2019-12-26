plugins {
    kotlin("jvm") version "1.3.61"
    id("io.vertx.vertx-plugin") version "1.0.1"
}

group = "org.example.vertx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    // io.vertx:vertx-core : Vert.x plugin includes vertx-core dependency with the specified version
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
