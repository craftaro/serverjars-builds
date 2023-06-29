import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.craftaro.serverjars"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.craftaro.serverjars.builder.AppKt")
}

repositories {
    mavenCentral()
}

val asmVersion = "9.1"
val ktorVersion = "1.5.4"
val logbackVersion = "1.2.9"
val junitVersion = "5.6.0"
val assertjVersion = "3.19.0"

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("org.ow2.asm:asm-tree:$asmVersion")
    implementation("org.ow2.asm:asm-util:$asmVersion")
    implementation("org.ow2.asm:asm-commons:$asmVersion")

    implementation("commons-cli:commons-cli:1.4")
    implementation("commons-io:commons-io:2.10.0")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("aws.sdk.kotlin:s3:0.25.0-beta")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.assertj:assertj-core:${assertjVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes["Main-Class"] = "com.craftaro.serverjars.builder.AppKt"
        }

        mergeServiceFiles()
        exclude("**/*.kotlin_metadata")
        exclude("**/*.kotlin_builtins")

        archiveBaseName.set("ServerJarsBuilder")
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}