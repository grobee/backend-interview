plugins {
    `java-library`
}

dependencies {
    implementation(project(":backend-interview-domain"))
    implementation("org.springframework:spring-web:6.2.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
}
