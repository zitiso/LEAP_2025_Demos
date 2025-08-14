plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.implicitintentdemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.implicitintentdemo"
        minSdk = 26      // If you use adaptive icons without fallbacks, set this to 26+
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }

    buildFeatures { compose = true }

    // Kotlin < 2.0 ⇒ specify Compose compiler explicitly
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    kotlin { jvmToolchain(17) }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // Compose BOM controls androidx.compose* versions
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI + Material3
    implementation(libs.bundles.compose.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.text)

    // Activity host for Compose
    implementation(libs.androidx.activity.compose)

    // Helpful AndroidX bits
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)

    // Tooling (preview, layout inspector)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}
