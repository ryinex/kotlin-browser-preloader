import com.vanniktech.maven.publish.GradlePublishPlugin
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("com.vanniktech.maven.publish") version "0.31.0"
    alias(libs.plugins.ktlint)
}

file("../.env").readLines().forEach { line ->
    if (line.isNotBlank()) {
        val splits = line.split("=")
        val env = splits[0] to splits.subList(1, splits.size).joinToString("")
        System.setProperty(env.first, env.second.removeSurrounding("\"").removeSurrounding("'"))
    }
}

group = "com.ryinex.kotlin"
version = System.getProperty("VERSION_NAME") ?: ""

gradlePlugin {
    plugins {
        create("preloader") {
            id = "com.ryinex.kotlin.browser.preloader"
            implementationClass = "PreloaderPlugin"
        }
    }
}

mavenPublishing {
    configure(GradlePublishPlugin())

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(rootProject.group.toString(), "browser-preloader", rootProject.version.toString())

    pom {
        name = "Kotlin Browser Preloader"
        description = "Kotlin Multiplatform Browser Targets Distribution Preloader"
        inceptionYear = "2025"
        url = "https://github.com/ryinex/kotlin-browser-preloader"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "elkhoudiry"
                name = "Ahmed Elkhoudiry"
                url = "https://github.com/elkhoudiry/"
            }
        }
        scm {
            url = "https://github.com/ryinex/kotlin-browser-preloader"
            connection = "scm:git:git://github.com/ryinex/kotlin-browser-preloader.git"
            developerConnection = "scm:git:ssh://git@github.com/ryinex/kotlin-browser-preloader.git"
        }
    }
}