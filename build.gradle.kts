plugins {
    `java-library`

    id("org.ajoberstar.grgit") version "4.0.2"

    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("io.freefair.lombok") version "5.0.1"
}

group = "systems.fehn"
version = grgit.describe(mapOf("tags" to true))

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")

    api("org.hashids:hashids:1.0.3")

    compileOnly("com.fasterxml.jackson.core:jackson-databind")
}
