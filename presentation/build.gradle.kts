
import java.util.regex.Pattern

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.dmonster.dcash"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dmonster.dcash"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val baseUrl: String = project.properties["baseUrl"] as String
        val baseUrlDebug: String = project.properties["baseUrlDebug"] as String

        getByName("debug") {
            isMinifyEnabled = false
            isJniDebuggable = true
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("String", "BASE_URL", baseUrlDebug)

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            buildConfigField("String", "BASE_URL", baseUrl)

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("version")

    productFlavors {
        create("Dcash") {
            dimension = "version"
            applicationId = "com.dmonster.dcash"
            buildConfigField("String", "STORE_TYPE", "\"playstore\"")
            buildConfigField("String", "SITE_CODE", "\"Dcash\"")
        }

        create("DcashOneStore") {
            dimension = "version"
            applicationId = "com.dmonster.onestore.dcash"
            buildConfigField("String", "STORE_TYPE", "\"onestore\"")
            buildConfigField("String", "SITE_CODE", "\"Dcash\"")
        }

        all {
            val sourceSet = sourceSets[this.name]
            sourceSet.setRoot("src/main/flavor/${sourceSet.name}")
        }
    }

    /*productFlavors.all {
        val props = org.jetbrains.kotlin.konan.properties.Properties()
        props.load(file("${projectDir}/src/main/flavor/${this.name}/setting.properties").inputStream())

        this.buildConfigField("Boolean", "SOCAL_LOGIN_KAKAO_ENABLE", (props.getProperty("SOCAL_LOGIN_KAKAO_ENABLE", "false") == "true").toString())
        this.buildConfigField("Boolean", "SOCAL_LOGIN_NAVER_ENABLE", (props.getProperty("SOCAL_LOGIN_NAVER_ENABLE", "false") == "true").toString())

        if (props.getProperty("SOCAL_LOGIN_KAKAO_ENABLE", "false") == "true") {
            this.buildConfigField("String", "KAKAO_API_KEY", "\"${props.getProperty("KAKAO_API_KEY")}\"")
            this.manifestPlaceholders["KAKAO_API_KEY"] = props.getProperty("KAKAO_API_KEY")
        } else {
            this.buildConfigField("String", "KAKAO_API_KEY", "null")
        }

        if (props.getProperty("SOCAL_LOGIN_NAVER_ENABLE", "false") == "true") {
            this.buildConfigField("String", "NAVER_CLIENT_ID", "\"${props.getProperty("NAVER_CLIENT_ID")}\"")
            this.buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${props.getProperty("NAVER_CLIENT_SECRET")}\"")
        } else {
            this.buildConfigField("String", "NAVER_CLIENT_ID", "null")
            this.buildConfigField("String", "NAVER_CLIENT_SECRET", "null")
        }

        if (props.getProperty("FCM_ENABLE", "false") == "true") {
            plugins.apply("com.google.gms.google-services")
        }
    }*/

    applicationVariants.all {
        /*val flavorName = this.productFlavors[0].name
        val variantName = this.buildType.name

        val props = org.jetbrains.kotlin.konan.properties.Properties()
        props.load(file("${projectDir}/src/main/flavor/${flavorName}/setting.properties").inputStream())
        val siteHost = props.getProperty("SITE_HOST")
        val redirectUrl = props.getProperty("REDIRECT_URL").replace("\${SITE_HOST}", siteHost)
        val apiHost = if (variantName == "debug" && props.containsKey("API_HOST_DEBUG")) {
            props.getProperty("API_HOST_DEBUG").replace("\${SITE_HOST}", siteHost)
        } else {
            props.getProperty("API_HOST").replace("\${SITE_HOST}", siteHost)
        }

        this.buildConfigField("String", "SITE_HOST", "\"${siteHost}\"")
        this.buildConfigField("String", "API_HOST", "\"${apiHost}\"")
        this.buildConfigField("String", "REDIRECT_URL", "\"${redirectUrl}\"")*/
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // Retrofit + Okhttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // androidX <
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.5")

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    // optional - helpers for implementing LifecycleOwner in a Service
    implementation("androidx.lifecycle:lifecycle-service:2.5.1")
    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // >

    // room
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    implementation("androidx.room:room-paging:2.5.0")
    kapt("androidx.room:room-compiler:2.5.0")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Paging 3.0
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")
    testImplementation("androidx.paging:paging-common:3.1.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Lottie
    implementation("com.airbnb.android:lottie:6.0.0")

    // Coil
    implementation("io.coil-kt:coil:2.3.0")

    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.0-rc01")

    implementation("com.tbuonomo:dotsindicator:5.0")
}

kapt {
    correctErrorTypes = true
}

fun getCurrentFlavor(task: Task): String {
    val patter: Pattern =if (task.name.startsWith("process") && task.name.endsWith("GoogleServices"))
        Pattern.compile("process(\\w+)(Release|Debug)GoogleServices")
    else if (task.name.contains("assemble"))
        Pattern.compile("assemble(\\w+)(Release|Debug)")
    else
        Pattern.compile("generate(\\w+)(Release|Debug)")

    val match = patter.matcher(task.name)

    return if (match.find())
        match.group(1).toLowerCase()
    else
        ""
}

fun getCurrentVariant(task: Task): String {
    val patter: Pattern =if (task.name.startsWith("process") && task.name.endsWith("GoogleServices"))
        Pattern.compile("process(\\w+)(Release|Debug)GoogleServices")
    else if (task.name.contains("assemble"))
        Pattern.compile("assemble(\\w+)(Release|Debug)")
    else
        Pattern.compile("generate(\\w+)(Release|Debug)")

    val match = patter.matcher(task.name)

    return if (match.find())
        match.group(2).toLowerCase()
    else
        ""
}

gradle.taskGraph.beforeTask {
    if (this.name.endsWith("GoogleServices")) {
        val flavorName = getCurrentFlavor(this)
        val variantName = getCurrentVariant(this)
        val flavorVariant = flavorName + variantName.substring(0, 1).toUpperCase() + variantName.substring(1)

        var googleServiceFile = file("${projectDir}/src/main/flavor/${flavorVariant}/google-services.json")

        if (!googleServiceFile.exists()) {
            googleServiceFile = file("${projectDir}/src/main/flavor/${flavorName}/google-services.json")
        }

        copy {
            print("Switches to $flavorVariant google-services.json")
            this.from(googleServiceFile.parent)
            this.include("google-services.json")
            this.into(".")
        }
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
}