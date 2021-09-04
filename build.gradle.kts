plugins {
    id("ca.cutterslade.analyze") version "1.7.1"
    id("com.asarkar.gradle.build-time-tracker") version "3.0.1"
    id("com.github.ben-manes.versions") version "0.39.0"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "ca.cutterslade.analyze")
    apply(plugin = "com.github.ben-manes.versions")

    group = "com.github.ngyewch.fjage-extras"
    version = "0.3.0"
    val isReleaseVersion = !(project.version as String).endsWith("SNAPSHOT")

    configure<PublishingExtension> {
        repositories {
            maven {
                if (isReleaseVersion) {
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                } else {
                    setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                }
                credentials {
                    val ossrhUsername: String? by project
                    val ossrhPassword: String? by project
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    tasks.withType<Javadoc> {
        configure(options, closureOf<StandardJavadocDocletOptions> {
            addStringOption("Xdoclint:none", "-quiet")
            links = listOf(
                "https://docs.oracle.com/javase/8/docs/api/",
                "https://org-arl.github.io/fjage/javadoc/",
            )
        })
    }
}

allprojects {
    repositories {
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
