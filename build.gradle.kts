/*
 * Copyright 2021-2022 Creek Contributors (https://github.com/creek-service)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    java
    jacoco
    `maven-publish`
    id("com.github.spotbugs") version "5.0.9"                   // https://mvnrepository.com/artifact/com.github.spotbugs/spotbugs-gradle-plugin
    id("com.diffplug.spotless") version "6.8.0"                 // https://mvnrepository.com/artifact/com.diffplug.spotless/spotless-plugin-gradle
    id("pl.allegro.tech.build.axion-release") version "1.13.14"  // https://mvnrepository.com/artifact/pl.allegro.tech.build.axion-release/pl.allegro.tech.build.axion-release.gradle.plugin?repo=gradle-plugins
    id("com.github.kt3k.coveralls") version "2.12.0"            // https://plugins.gradle.org/plugin/com.github.kt3k.coveralls
    id("org.javamodularity.moduleplugin") version "1.8.11"      // https://plugins.gradle.org/plugin/org.javamodularity.moduleplugin
}

project.version = scmVersion.version

apply(plugin = "idea")
apply(plugin = "java")
apply(plugin = "jacoco")
apply(plugin = "checkstyle")
apply(plugin = "com.diffplug.spotless")
apply(plugin = "com.github.spotbugs")
apply(plugin = "maven-publish")

group = "org.creekservice"

java {
    withSourcesJar()

    modularity.inferModulePath.set(false)
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://maven.pkg.github.com/creek-service/*")
        credentials {
            username = "Creek-Bot-Token"
            password = "\u0067hp_LtyvXrQZen3WlKenUhv21Mg6NG38jn0AO2YH"
        }
    }
}

extra.apply {
    set("creekTestVersion", "0.2.0-SNAPSHOT")
    set("spotBugsVersion", "4.4.2")         // https://mvnrepository.com/artifact/com.github.spotbugs/spotbugs-annotations

    set("guavaVersion", "31.1-jre")       // https://mvnrepository.com/artifact/com.google.guava/guava
    set("log4jVersion", "2.18.0")           // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core

    set("junitVersion", "5.8.2")            // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    set("junitPioneerVersion", "1.7.1")     // https://mvnrepository.com/artifact/org.junit-pioneer/junit-pioneer
    set("mockitoVersion", "4.6.1")          // https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
    set("hamcrestVersion", "2.2")           // https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core
}

val creekTestVersion : String by extra
val guavaVersion : String by extra
val log4jVersion : String by extra
val junitVersion: String by extra
val junitPioneerVersion: String by extra
val mockitoVersion: String by extra
val hamcrestVersion : String by extra

dependencies {
    testImplementation("org.creekservice:creek-test-util:$creekTestVersion")
    testImplementation("org.creekservice:creek-test-hamcrest:$creekTestVersion")
    testImplementation("org.creekservice:creek-test-conformity:$creekTestVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.junit-pioneer:junit-pioneer:$junitPioneerVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.hamcrest:hamcrest-core:$hamcrestVersion")
    testImplementation("com.google.guava:guava-testlib:$guavaVersion")
    testImplementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-slf4j18-impl:$log4jVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.compileJava {
    options.compilerArgs.add("-Xlint:all,-serial,-requires-automatic,-requires-transitive-automatic")
    options.compilerArgs.add("-Werror")
}

tasks.test {
    useJUnitPlatform()
    setForkEvery(1)
    maxParallelForks = 4
    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

spotless {
    java {
        googleJavaFormat("1.12.0").aosp()
        indentWithSpaces()
        importOrder()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

spotbugs {
    tasks.spotbugsMain {
        reports.create("html") {
            enabled = true
            setStylesheet("fancy-hist.xsl")
        }
    }
    tasks.spotbugsTest {
        reports.create("html") {
            enabled = true
            setStylesheet("fancy-hist.xsl")
        }
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }

    dependsOn(tasks.test)
}

tasks.register("format") {
    dependsOn("spotlessCheck", "spotlessApply")
}

tasks.register("static") {
    dependsOn("checkstyleMain", "checkstyleTest", "spotbugsMain", "spotbugsTest")
}

tasks.register("coverage") {
    group = "coverage"
    dependsOn("jacocoTestReport")
}

tasks.coveralls {
    group = "coverage"
    description = "Uploads the aggregated coverage report to Coveralls"

    dependsOn("coverage")
    onlyIf{System.getenv("CI") != null}
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/creek-service/${rootProject.name}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                url.set("https://github.com/creek-service/${rootProject.name}.git")
            }
        }
    }
}

defaultTasks("format", "static", "check")
