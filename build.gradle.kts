import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    val kotlinVersion = "1.8.10"
    `version-catalog`
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.github.node-gradle.node") version "3.5.1"
    id("org.flywaydb.flyway") version "9.15.0"
    id("com.github.ben-manes.versions") version "0.45.0"
    id("org.sonarqube") version "4.0.0.2929"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
}

group = "nl.strmark"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    val versions = kpiLibs.versions
    implementation("org.springframework.boot:spring-boot-starter-web:${versions.springboot.get()}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${versions.springboot.get()}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${versions.kotlinversion.get()}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${versions.kotlinversion.get()}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${versions.springboot.get()}")
    runtimeOnly("com.h2database:h2:${versions.h2db.get()}")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:${versions.springboot.get()}")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:${versions.thymeleaf.get()}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions.swagger.get()}")
    implementation("org.webjars:font-awesome:${versions.font.get()}")
    implementation("io.github.microutils:kotlin-logging:${versions.klogging.get()}")
    developmentOnly("org.springframework.boot:spring-boot-devtools:${versions.springboot.get()}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${versions.springboot.get()}")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

node {
    download.set(true)
    version.set("18.13.0")
}

val npmRunBuild = tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmRunBuild") {
    args.set(listOf("run", "build"))

    dependsOn(tasks.npmInstall)

    inputs.files(fileTree("node_modules"))
    inputs.files(fileTree("src/main/resources"))
    inputs.file("package.json")
    inputs.file("webpack.config.js")
    outputs.dir("$buildDir/resources/main/static")
}

tasks.processResources {
    dependsOn(npmRunBuild)
}

tasks.getByName<BootRun>("bootRun") {
    environment["SPRING_PROFILES_ACTIVE"] = environment["SPRING_PROFILES_ACTIVE"] ?: "local"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Wrapper> {
    gradleVersion = "8.0.1"
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "strmark")
        property("sonar.projectKey", "strmark_kpiradio_thymeleaf")
    }
}
