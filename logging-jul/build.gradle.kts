java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

dependencies {
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String

            from(components["java"])

            pom {
                name.set(project.name)
                description.set("JUL helper.")
                url.set("https://github.com/ngyewch/fjage-extras")
                licenses {
                    license {
                        name.set("BSD-3-Clause License")
                        url.set("https://github.com/ngyewch/fjage-extras/blob/main/LICENSE")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:ngyewch/fjage-extras.git")
                    developerConnection.set("scm:git:git@github.com:ngyewch/fjage-extras.git")
                    url.set("https://github.com/ngyewch/fjage-extras")
                }
                developers {
                    developer {
                        id.set("ngyewch")
                        name.set("Nick Ng")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
