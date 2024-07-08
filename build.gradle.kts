plugins {
//    id("org.jetbrains.kotlin.jvm") version "1.9.23"
//    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
//    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
//    id("com.github.johnrengelman.shadow") version "8.1.1"
//    id("io.micronaut.application") version "4.4.0"
//    id("io.micronaut.aot") version "4.4.0"

    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
//    id("com.github.johnrengelman.shadow") version "8.1.1"
//    id("io.micronaut.application") version "4.4.0"
    id("io.micronaut.minimal.application") version "4.4.0"
    id("io.micronaut.aot") version "4.4.0"
}

version = "1.0"
group = "cz.cablo.knuspr"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
//    ksp("io.micronaut.data:micronaut-data-processor")
//    ksp("io.micronaut:micronaut-http-validation")
//    ksp("io.micronaut.serde:micronaut-serde-processor")
//    implementation("io.micronaut.data:micronaut-data-jdbc")
//    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
//    implementation("io.micronaut.serde:micronaut-serde-jackson")
//    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
//    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
//    compileOnly("io.micronaut:micronaut-http-client")
//    runtimeOnly("ch.qos.logback:logback-classic")
//    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
//    runtimeOnly("org.postgresql:postgresql")

//    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")


//    implementation("io.micronaut:micronaut-inject")
//
//    testImplementation("io.micronaut.test:micronaut-test-junit5")
//    testImplementation("org.jetbrains.kotlin:kotlin-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
//    testImplementation("org.junit.jupiter:junit-jupiter-api")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")


//    testImplementation("io.micronaut:micronaut-http-client")
//    testImplementation("io.micronaut.test:micronaut-test-junit5")
//    testImplementation("org.jetbrains.kotlin:kotlin-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
//    testImplementation("org.junit.jupiter:junit-jupiter-api")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // for transactional
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-validation:3.10.4")
    annotationProcessor("io.micronaut.data:micronaut-data-processor")

    ksp("io.micronaut.data:micronaut-data-processor")
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.data:micronaut-data-jdbc:4.8.3")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.yaml:snakeyaml")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")


    // tests
//    testImplementation("io.micronaut.test:micronaut-test-junit5")
//    testImplementation("org.junit.jupiter:junit-jupiter-api")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
//    testImplementation("io.micronaut:micronaut-http-client")
}


application {
    mainClass = "com.example.ApplicationKt"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
}


//graalvmNative.toolchainDetection = false
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("cz.cablo.knuspr.*")
    }
    aot {
    // Please review carefully the optimizations enabled below
    // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    }
}

//tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
//    jdkVersion = "21"
//}


