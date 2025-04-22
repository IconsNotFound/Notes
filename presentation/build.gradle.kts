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
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)

    // (Room) ----------------------------------------
    alias(libs.plugins.google.devtools.ksp)
    // -----------------------------------------------
}

android {
    namespace = "com.iconsnotfound.presentation"
    compileSdk = 35

    defaultConfig {
        minSdk = 16
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

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

    // (Kotlin Components) ---------------------------
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)
    // -----------------------------------------------

    // (Lifecycle Components) ------------------------
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.common.java8)
    // -----------------------------------------------

    // (Room Components) -----------------------------
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    androidTestImplementation (libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)
    // -----------------------------------------------

    // (Custom Libs) ---------------------------------
    implementation(project(":db"))
    implementation(project(":data"))
    // -----------------------------------------------
}