plugins {
    "java-gradle-plugin"
    "maven-publish"
}

group = "com.github.webhelper"
version = "0.0.1-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    gradleApi()
}

gradlePlugin {
    plugins {
        create("WebhelperPlugin") {
            id = "webhelper"
            implementationClass = "com.github.webhelper.gradle"
        }
    }
}