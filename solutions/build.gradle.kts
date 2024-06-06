plugins {
    id("java")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    annotationProcessor(project(":annotation-scanner"))

    implementation(aoc.slf4j)
    implementation(aoc.lang)
    implementation(project(":annotation-scanner"))

    runtimeOnly(aoc.logback)

    testImplementation(aoc.junit)
}