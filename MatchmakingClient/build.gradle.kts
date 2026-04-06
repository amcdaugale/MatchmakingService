plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5" // Use your preferred version
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

group = "com.mcdaale.capstone.client"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework:spring-websocket:7.0.6")
    implementation("org.springframework:spring-webmvc:7.0.6")
    implementation("org.springframework:spring-messaging:7.0.6")
    implementation("jakarta.websocket:jakarta.websocket-api:2.1.0")
    implementation("jakarta.platform:jakarta.jakartaee-api:10.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.mcdaale.capstone.client.MatchmakingClient"
    }
}

tasks.test {
    useJUnitPlatform()
}