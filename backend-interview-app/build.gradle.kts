import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    `java-library`
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.framework.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(project(":backend-interview-domain"))
    implementation(project(":backend-interview-integration"))

    implementation(rootProject.libs.kotlinx.coroutines.reactor)
    implementation(rootProject.libs.bundles.spring)
    implementation(rootProject.libs.jackson.kotlin)

    testImplementation(rootProject.libs.testing.wiremock)
    testImplementation(rootProject.libs.spring.boot.starter.test) {
        exclude(
            group = rootProject.libs.testing.junit.vintage.get().group,
            module = rootProject.libs.testing.junit.vintage.get().name
        )
    }
    testImplementation(rootProject.libs.testing.junit.json.assertj)
}
