plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = Java.sourceCompatibility
    targetCompatibility = Java.targetCompatibility
}

dependencies {
    // DI
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}
