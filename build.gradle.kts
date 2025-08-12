import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.deblock"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)

    dependencies {
        implementation(rootProject.libs.kotlinx.coroutines)
        implementation(rootProject.libs.kotlin.logging)

        testImplementation(platform(rootProject.libs.testing.junit.bom))
        testImplementation(rootProject.libs.bundles.testing)

        testRuntimeOnly(rootProject.libs.testing.junit.platform.launcher)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}