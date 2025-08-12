plugins {
    `java-library`
}

dependencies {
    implementation(project(":backend-interview-domain"))
    implementation(rootProject.libs.jackson.databind)
}
