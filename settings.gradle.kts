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
    }
}

rootProject.name = "CardioApp"
include(":app")
include(":core:common")
include(":core:model")
include(":core:domain")
include(":core:database")
include(":core:data")
include(":core:ui")
include(":feature:today")
include(":feature:healthrecords")
include(":feature:vitamins")
include(":feature:history")
include(":feature:help")
include(":feature:profile")
include(":feature:onboarding")