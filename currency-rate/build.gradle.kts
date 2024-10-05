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
    implementation(libs.spring.boot)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation("org.springframework.boot:spring-boot-starter-cache:3.3.4")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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