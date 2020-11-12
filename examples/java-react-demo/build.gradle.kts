plugins {
    id("java")
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("com.github.webhelper.gradle.webhelper") version "0.0.1-SNAPSHOT"
}

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

webpack {
    entry = "./src/main/web/index.tsx"
    appType = "react"
    webappDirectory = "src/main/web"
    distDirectory = "src/main/resources/static/assets"
    outputFilename = "bundle.js"
}

