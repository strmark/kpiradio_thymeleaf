import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    with(kpiLibs.versions) {
        id("org.springframework.boot") version springboot
        id("io.spring.dependency-management") version dependency
        id("com.github.node-gradle.node") version node
        id("com.github.ben-manes.versions") version manes
        id("org.sonarqube") version sonarqube
        id("org.owasp.dependencycheck") version owasp
        kotlin("jvm") version kotlinversion
        kotlin("plugin.spring") version kotlinversion
        kotlin("plugin.allopen") version kotlinversion
    }
}

allprojects {
    group = "nl.strmark"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_21

    repositories {
        mavenCentral()
    }

    dependencies {
        with(kpiLibs.versions) {
            implementation("io.github.oshai:kotlin-logging-jvm:${klogging.get()}")
            implementation("org.springframework.boot:spring-boot-starter-web:${springboot.get()}")
            implementation("org.springframework.boot:spring-boot-starter-validation:${springboot.get()}")
            implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinversion.get()}")
            implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlinversion.get()}")
            implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springboot.get()}")
            runtimeOnly("com.h2database:h2:${h2db.get()}")
            implementation("org.springframework.boot:spring-boot-starter-thymeleaf:${springboot.get()}")
            implementation("org.yaml:snakeyaml:${snakeyaml.get()}")
            implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:${thymeleaf.get()}")
            implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swagger.get()}")
            implementation("org.webjars:font-awesome:${font.get()}")
            developmentOnly("org.springframework.boot:spring-boot-devtools:${springboot.get()}")
            testImplementation("org.springframework.boot:spring-boot-starter-test:${springboot.get()}")
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
    version.set("20.15.0")
}

val npmRunBuild = tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmRunBuild") {
    description = "NPM build task."
    group = JavaBasePlugin.BUILD_TASK_NAME
    args.set(listOf("run", "build"))

    dependsOn(tasks.npmInstall)

    inputs.files(fileTree("node_modules"))
    inputs.files(fileTree("src/main/resources"))
    inputs.file("package.json")
    inputs.file("webpack.config.js")
    outputs.dir("build/resources/main/static")
}

tasks.processResources {
    dependsOn(npmRunBuild)
}

tasks.getByName<BootRun>("bootRun") {
    environment["SPRING_PROFILES_ACTIVE"] = environment["SPRING_PROFILES_ACTIVE"] ?: "local"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JVM_21
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Wrapper> {
    gradleVersion = "8.8"
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
