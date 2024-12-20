import org.gradle.internal.impldep.com.fasterxml.jackson.core.JsonPointer.compile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.playlistmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.playlistmaker"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
  //  configurations.all {
  //      exclude "group": 'org.jetbrains', module: 'annotations-java5'
  //  }

   // configurations { implementation.get().exclude(mapOf("group" to "org.jetbrains", "module" to "annotations-java5")) }

    // build.gradle.kts
   /* allprojects {
        project.configurations.all {
            resolutionStrategy {
                eachDependency {
                    if (requested.group == "com.intellij" && requested.name == "annotations") {
                        useTarget("org.jetbrains:annotations:23.0.0")
                    }
                }
            }
        }
    }*/
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
  //  implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.contentpager:contentpager:1.0.0")
    implementation("androidx.activity:activity:1.9.2")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    //implementation("androidx.room:room-compiler-processing-testing:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    kapt("com.github.bumptech.glide:compiler:4.14.2")//annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

    // подключаем библиотеку retrofit и конвертер converter_gson
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // подключаем библиотеку koin
    implementation ("io.insert-koin:koin-android:3.3.0")

    // ViewPager2
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //  Jetpack Navigation Component
    implementation ("androidx.fragment:fragment-ktx:1.5.6")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Room
    val room_version = "2.5.1"
    implementation("androidx.room:room-runtime:$room_version") // Библиотека "Room"
    kapt("androidx.room:room-compiler:$room_version") // Кодогенератор
    implementation("androidx.room:room-ktx:$room_version") // Дополнительно для Kotlin Coroutines, Kotlin Flows

}