plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"

    jacoco
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
    }
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow") {
        exclude(group = "io.undertow", module = "undertow-websockets-jsr")
    }

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.3")

    implementation("org.slf4j:slf4j-api") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-core")
    }
    implementation("ch.qos.logback:logback-classic")
    implementation("org.apache.logging.log4j:log4j-to-slf4j")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.10")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileKotlin {
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    }

    test {
        useJUnitPlatform()
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
        }
    }
}

