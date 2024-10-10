plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("jacoco")
}

group = "ru.matthew"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":my-aop-starter"))
    implementation(libs.spring.boot)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation(libs.mockito)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.engine)
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-13")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")
    testImplementation("org.testcontainers:testcontainers:1.20.1")
    testImplementation("org.testcontainers:junit-jupiter:1.20.1")
    testImplementation("org.wiremock:wiremock:3.9.1")
}

tasks.test {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }

    afterEvaluate {
        classDirectories.setFrom(files(
            fileTree("${layout.buildDirectory}/classes/java/main").exclude("/dto/")
        ))
    }
}