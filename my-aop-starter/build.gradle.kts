plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "ru.matthew"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.starter.aop)
    implementation(libs.spring.boot.autoconfigure)
    implementation(libs.aspectjweaver)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from("src/main/resources") {
        include("META-INF/spring.factories")
    }
}

tasks.bootJar {
    enabled = false
}