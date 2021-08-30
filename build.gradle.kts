plugins {
    id("ca.cutterslade.analyze") version "1.7.1"
    id("com.asarkar.gradle.build-time-tracker") version "3.0.1"
    id("com.github.ben-manes.versions") version "0.39.0"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    group = "com.github.ngyewch.fjage-extras"
    version = "0.2.1"
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/ngyewch/fjage-extras")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
            }
        }
    }
}

allprojects {
    repositories {
        maven { url = rootProject.file("repo").toURI() }
        mavenCentral()
    }

    tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
        gradleReleaseChannel = "current"

        fun isNonStable(version: String): Boolean {
            val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeyword || regex.matches(version)
            return isStable.not()
        }

        rejectVersionIf {
            isNonStable(candidate.version) && !isNonStable(currentVersion)
        }
    }
}
