
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("aoc") {
            library("lang", "com.jongsoft.lang:language:1.1.6")
            library("slf4j", "org.slf4j:slf4j-api:2.0.13")
            library("logback", "ch.qos.logback:logback-classic:1.5.6")

            library("junit", "org.junit.jupiter:junit-jupiter:5.8.2")
        }
    }
}

include ("annotation-scanner")
include ("solutions")