pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Qurban-Ticketing-System"
include(":app")
include(":core:theme")
include(":core:common")
include(":feature:member:management:ui")
include(":feature:member:shared:domain")
include(":feature:member:shared:data")
include(":feature:member:shared:common")
include(":feature:member:ticketing:ui")
