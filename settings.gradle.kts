rootProject.name = "piradio"

dependencyResolutionManagement {
    versionCatalogs {
        create("kpiLibs") {
            from(files("libs.versions.toml"))
        }
    }
}
