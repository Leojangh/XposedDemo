dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jcenter.bintray.com")
    }
}
pluginManagement {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "XposedDemo"
include(":app")
