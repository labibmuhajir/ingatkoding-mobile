import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.10"
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.mockmp)
    alias(libs.plugins.skie)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.coroutines.extensions)
        }
        androidMain.dependencies{
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }


        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.junit)
            implementation(libs.junit)
            implementation(libs.ktor.client.mock)
        }
        androidNativeTest.dependencies {

        }
        iosTest.dependencies {

        }
    }
}

android {
    namespace = "com.ingatkoding.blog"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
dependencies {
    testImplementation("junit:junit:4.12")
}

sqldelight {
    databases {
        create(name = "IngatkodingBlog") {
            packageName.set("ingatkoding.blog.db")
        }
    }
}

buildkonfig {
    packageName = "com.ingatkoding.blog"

    // default config is required
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "http://api.ingatkoding.com/api/")
    }
}


mockmp {
    usesHelper = true
}