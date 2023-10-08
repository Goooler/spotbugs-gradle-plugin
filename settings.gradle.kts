pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    `gradle-enterprise`
}

dependencyResolutionManagement {
    repositories {
        // To download the Android Gradle Plugin
        google()
        // To download trove4j required by the Android Gradle Plugin
        mavenCentral()
    }
}

rootProject.name = "spotbugs-gradle-plugin"

val isCiBuild = providers.environmentVariable("CI").isPresent

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(isCiBuild)
    }
}
