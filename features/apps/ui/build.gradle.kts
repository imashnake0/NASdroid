plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.detekt)
}

android {
    namespace = "com.nasdroid.apps.ui"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    lint {
        sarifReport = true
        htmlReport = false
    }
}

kotlin {
    jvmToolchain(17)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

dependencies {
    implementation(projects.core.composeLogviewer)
    implementation(projects.core.composeUrllauncher)
    implementation(projects.core.composeMarkdown)
    implementation(projects.core.design)

    implementation(projects.features.apps.logic)

    implementation(libs.androidx.navigation)
    implementation(libs.bundles.compose)
    implementation(libs.compose.menuprovider)
    debugImplementation(libs.bundles.compose.tooling)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
}
