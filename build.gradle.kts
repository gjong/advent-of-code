plugins {
    id("groovy")
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}