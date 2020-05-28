plugins {
    `java-library`
    `maven-publish`
    signing

    id("org.ajoberstar.grgit") version "4.0.2"

    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("io.freefair.lombok") version "5.0.1"
}

group = "systems.fehn"
version = grgit.describe(mapOf("tags" to true))
val isRelease = !version.toString().substringAfterLast('-').startsWith('g')

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter")
    compileOnly("org.springframework.boot:spring-boot-starter-web")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")

    api("org.hashids:hashids:1.0.3")

    compileOnly("com.fasterxml.jackson.core:jackson-databind")
}

java.withSourcesJar()
java.withJavadocJar()

tasks.jar {
    enabled = true
}

tasks.register("version") {
    doLast {
        println(version)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            version = if (isRelease) project.version.toString() else "${project.version}-SNAPSHOT"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("Hashids Spring Boot Starter")
                description.set("Use Hashids with Spring Boot")
                url.set("https://github.com/fehnomenal/hashids-spring-boot-starter")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://www.opensource.org/licenses/mit-license.php")
                    }
                }
                developers {
                    developer {
                        id.set("afehn")
                        name.set("Andreas Fehn")
                        email.set("fehnomenal@fehn.systems")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/fehnomenal/hashids-spring-boot-starter.git")
                    developerConnection.set("scm:git:ssh://github.com:fehnomenal/hashids-spring-boot-starter.git")
                    url.set("https://github.com/fehnomenal/hashids-spring-boot-starter")
                }
            }
        }
    }
    repositories {
        mavenLocal()
        maven {
            url = if (isRelease) {
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            } else {
                uri("https://oss.sonatype.org/content/repositories/snapshots")
            }

            credentials {
                username = project.property("ossrh.username").toString()
                password = project.property("ossrh.password").toString()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
