plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("com.google.cloud.tools.jib") version "3.4.4"
    id("org.unbroken-dome.test-sets") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ee.karlaru"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

jib {
    from {
        image = "amazoncorretto:21.0.6-alpine"
    }
    to {
        image = "docker.io/karlaru/filters"
        tags = setOf(version.toString(), "latest")
    }
}

repositories {
    mavenCentral()
}

testSets {
    create("integrationTest")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("org.hibernate:hibernate-validator:8.0.2.Final")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("io.github.classgraph:classgraph:4.8.179")
    implementation("org.postgresql:postgresql")
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("com.h2database:h2:2.3.232")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    "integrationTestImplementation"("org.springframework.boot:spring-boot-starter-test")
    "integrationTestImplementation"("org.springframework.security:spring-security-test")
    "integrationTestImplementation"("org.testcontainers:testcontainers:1.20.5")
    "integrationTestImplementation"("org.testcontainers:postgresql:1.20.5")
    "integrationTestImplementation"("org.testcontainers:junit-jupiter:1.20.5")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<Test>("integrationTest") {
    useJUnitPlatform()
}