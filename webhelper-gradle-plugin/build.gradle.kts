plugins {
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "com.github.webhelper"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases-local/")
    }
    mavenLocal()
}

dependencies {
    api(gradleApi())
    testImplementation("junit:junit:4.12")
}

/*
gradlePlugin {
    plugins {
        create("WebhelperPlugin") {
            id = "webhelper"
            implementationClass = "com.github.webhelper.gradle.WebhelperPlugin"
        }
    }
}*/

tasks {
    publishToMavenLocal {
        dependsOn(check)
    }
    build {
        dependsOn(publishToMavenLocal)
    }
}

//jar.baseName = "webhelper.gradle.plugin"
