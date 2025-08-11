plugins {
    `java-library`
}

dependencies {
    implementation(project(":backend-interview-domain"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
}
