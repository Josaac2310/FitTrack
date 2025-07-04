// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0") // O la versión más reciente
    }
}