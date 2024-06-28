plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "utalca.gestor_qr"
    compileSdk = 34

    packagingOptions {
        resources.excludes += "META-INF/DEPENDENCIES"
        resources.excludes += "META-INF/LICENSE"
        resources.excludes += "META-INF/LICENSE.txt"
        resources.excludes += "META-INF/license.txt"
        resources.excludes += "META-INF/NOTICE"
        resources.excludes += "META-INF/NOTICE.txt"
        resources.excludes += "META-INF/notice.txt"
    }

    defaultConfig {
        applicationId = "utalca.gestor_qr"
        minSdk = 21
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3.android)
    implementation ("com.google.android.gms:play-services-auth:20.0.0")
    implementation ("com.google.api-client:google-api-client:1.33.0")
    implementation ("com.google.api-client:google-api-client-android:1.33.0")
    implementation ("com.google.api-client:google-api-client-gson:1.33.0")
    implementation ("com.google.api-client:google-api-client-jackson2:1.33.0")
    implementation ("com.google.apis:google-api-services-drive:v3-rev20211018-1.32.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.android.material:material:1.5.0-alpha05")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation( "com.google.api-client:google-api-client:1.31.5")
    implementation ("com.google.apis:google-api-services-drive:v3-rev136-1.25.0")
    implementation ("com.google.android.gms:play-services-auth:19.2.0")
    implementation ("com.google.http-client:google-http-client-gson:1.39.2")
    implementation("com.journeyapps:zxing-android-embedded:4.1.0") {
        isTransitive = false
    }
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.google.zxing:core:3.3.0")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation(libs.play.services.location)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.play.services.drive)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}