import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    kpiLibs.versions.let { versions ->
        id("org.springframework.boot") version versions.springboot.get()
        id("io.spring.dependency-management") version versions.dependency.get()
        id("com.github.node-gradle.node") version versions.node.get()
        id("com.github.ben-manes.versions") version versions.manes.get()
        id("org.sonarqube") version versions.sonarqube.get()
        id("org.owasp.dependencycheck") version versions.owasp.get()
        kotlin("jvm") version versions.kotlinversion.get()
        kotlin("plugin.spring") version versions.kotlinversion.get()
        kotlin("plugin.allopen") version versions.kotlinversion.get()
    }
}

allprojects {
    group = "nl.strmark"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_17

    repositories {
        mavenCentral()
    }

    dependencies {
        kpiLibs.versions.let { versions ->
            implementation("org.springframework.boot:spring-boot-starter-web:${versions.springboot.get()}")
            implementation("org.springframework.boot:spring-boot-starter-validation:${versions.springboot.get()}")
            implementation("org.jetbrains.kotlin:kotlin-reflect:${versions.kotlinversion.get()}")
            implementation("org.jetbrains.kotlin:kotlin-stdlib:${versions.kotlinversion.get()}")
            implementation("org.springframework.boot:spring-boot-starter-data-jpa:${versions.springboot.get()}")
            runtimeOnly("com.h2database:h2:${versions.h2db.get()}")
            implementation("org.springframework.boot:spring-boot-starter-thymeleaf:${versions.springboot.get()}")
            implementation("org.yaml:snakeyaml:${versions.snakeyaml.get()}")
            implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:${versions.thymeleaf.get()}")
            implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions.swagger.get()}")
            implementation("org.webjars:font-awesome:${versions.font.get()}")
            implementation("io.github.microutils:kotlin-logging:${versions.klogging.get()}")
            developmentOnly("org.springframework.boot:spring-boot-devtools:${versions.springboot.get()}")
            testImplementation("org.springframework.boot:spring-boot-starter-test:${versions.springboot.get()}")
        }
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

node {
    download.set(true)
    version.set("20.3.1")
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
    gradleVersion = "8.2"
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "strmark")
        property("sonar.projectKey", "strmark_kpiradio_thymeleaf")
    }
}

dependencyCheck {
    analyzers.assemblyEnabled = false
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
