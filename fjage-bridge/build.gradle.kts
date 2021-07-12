plugins {
    `java-library`
    id("ca.cutterslade.analyze")
    id("com.github.ben-manes.versions")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api("com.github.org-arl:fjage:1.9.0")
}
