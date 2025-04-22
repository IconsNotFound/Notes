/*
 * Notes - Fast &amp; Simple
 * Copyright (C) 2025 IconsNotFound
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // (kotlin parcelable) ---------------------------
    alias(libs.plugins.kotlin.parcelable)
    // -----------------------------------------------
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.iconsnotfound.notes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.iconsnotfound.notes"
        minSdk = 16
        targetSdk = 35
        versionCode = 29
        versionName = "1.2.9"

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
    viewBinding { enable = true }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // (Navigation) ----------------------------------
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    // -----------------------------------------------

    // (multidex) ------------------------------------
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.startup)
    // -----------------------------------------------

    // (ViewModel) -----------------------------------
    implementation(libs.androidx.activity.ktx)
    // -----------------------------------------------

    // (Paging3) -------------------------------------
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.common.ktx)
    // -----------------------------------------------

    // (Custom Libs) ---------------------------------
    implementation(project(":lib"))
    implementation(project(":db"))
    implementation(project(":ui"))
    implementation(project(":data"))
    implementation(project(":presentation"))
    // -----------------------------------------------
}