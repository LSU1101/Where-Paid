import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.wherepaid.app.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

ksp {
    // DB 구조가 바뀌면 schemas/에 새 버전 JSON이 생긴다. 같이 커밋한다.
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    api(project(":core:model"))
    implementation(libs.kotlinx.coroutines.android)

    // Room은 이 모듈 밖으로 노출하지 않는다 (api 금지).
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)

    // Room 저장소 테스트는 실제 SQLite가 필요해 기기/에뮬레이터에서 돈다: ./gradlew :core:data:connectedDebugAndroidTest
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
