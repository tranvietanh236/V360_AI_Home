plugins {
    id("com.android.library")
}

group = "com.github.philjay"

android {
    namespace = "com.github.mikephil.charting"
    compileSdk = 36

    defaultConfig {
        minSdk = 29
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.0.0")
    testImplementation("junit:junit:4.12")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

/** FIX 1: sourcesJar */
val sourcesJar by tasks.registering(Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.set("sources")
}

/** FIX 2: javadocJar */
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from("src/main/java")
}

/** FIX 3: publish artifacts */
artifacts {
    add("archives", sourcesJar)
    add("archives", javadocJar)
}
