plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.chatapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chatapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    //启用 ViewBinding 功能
    buildFeatures {
        viewBinding=true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //用于支持不同的屏幕尺寸
    implementation("com.intuit.sdp:sdp-android:1.0.6") // 用于根据屏幕大小调整尺寸
    implementation("com.intuit.ssp:ssp-android:1.0.6") // 用于根据屏幕大小调整文本大小

    //用于旋转或圆角图像
    implementation("com.makeramen:roundedimageview:2.3.0") // 提供圆角、边框等图片处理功能
}