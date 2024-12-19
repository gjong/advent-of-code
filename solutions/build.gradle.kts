plugins {
    id("java")
    id("application")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

application {
    mainClass = "com.github.gjong.advent.AdventOfCode"
}

dependencies {
    annotationProcessor(project(":annotation-scanner"))
    testAnnotationProcessor(project(":annotation-scanner"))

    implementation(aoc.slf4j)
    implementation(aoc.lang)
    implementation(project(":annotation-scanner"))

    implementation(aoc.logback)

    testImplementation(aoc.junit)
}