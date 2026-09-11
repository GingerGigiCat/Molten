plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
}

group = "com.hackclub.molten"
version = "1.0.0"
application {
    mainClass = "com.hackclub.molten.ApplicationKt"
}

dependencies {
    api(project(":core"))
    implementation(libs.logback)
    implementation(libs.dotenv.kotlin)
    implementation(libs.java.jwt)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.serverCors)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.clientCio)
    implementation(libs.ktor.clientContentNegotiation)
    implementation(libs.ktor.serializationKotlinxJson)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}
