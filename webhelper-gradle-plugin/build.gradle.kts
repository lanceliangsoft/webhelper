plugins {
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "com.github.webhelper.gradle"
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
    implementation("com.github.webhelper:webhelper-core:${project.version}")
    testImplementation("junit:junit:4.12")
}

gradlePlugin {
    plugins {
        create("WebhelperPlugin") {
            id = "com.github.webhelper.gradle.webhelper"
            implementationClass = "com.github.webhelper.gradle.WebhelperPlugin"
        }
    }
}

tasks {
    publishToMavenLocal {
        dependsOn(check)
    }
    build {
        dependsOn(publish)
    }
    publish {
        dependsOn(publishToMavenLocal)
    }
}

//jar.baseName = "webhelper.gradle.plugin"
project.defaultTasks("build")