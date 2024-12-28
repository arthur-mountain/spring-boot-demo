plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.arthurmountain"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring boot web starter
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Doc (OpenAPI/Swagger)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // DB：Spring Data JDBC + PostgreSQL 驅動
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    runtimeOnly("org.postgresql:postgresql")

    // Redis
    // implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // implementation("io.micrometer:micrometer-registry-prometheus") // 需要時再開
    // Spring Boot Actuator (健康檢查、指標)
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // 開發/測試
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
