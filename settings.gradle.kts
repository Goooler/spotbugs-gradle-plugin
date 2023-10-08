plugins {
    id("com.gradle.enterprise") version("3.7.2")
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
