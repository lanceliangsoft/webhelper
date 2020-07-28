buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath(group = "com.github.webhelper", name = "webhelper-gradle-plugin", version = "0.0.1-SNAPSHOT")
    }
}

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("java")
    //id("io.freefair.lombok") version "3.8.4"
}
apply(plugin = "webhelper")

group = "com.github.webhelper.example"
version = "0.0.1-SNAPSHOT"

val springBootVersion = "2.3.1.RELEASE"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springBootVersion}")
    implementation("org.xerial:sqlite-jdbc:3.32.3")
    implementation("org.springdoc:springdoc-openapi-ui:1.4.3")
    implementation("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")
    testImplementation("junit:junit:4.12")
}

tasks.register<com.github.webhelper.gradle.WebpackTask>("webpack") {
    dependsOn("compileJava")
    message = "Hello"
}

tasks["processResources"].dependsOn("webpack")

