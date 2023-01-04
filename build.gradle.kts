import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.squareup.sqldelight")
}

group = "de.syex"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val commonMain by getting

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.squareup.sqldelight:sqlite-driver:${extra["sqldelight.version"] as String}")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "de.syex.dailyupdate.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DailyUpdate"
            packageVersion = "1.0.0"
            // required for SQLDelight and java.sql.DriverManager
            // see https://github.com/JetBrains/compose-jb/issues/381
            modules("java.sql")
        }
    }
}

sqldelight {
    database("DailyUpdateDatabase") {
        packageName = "de.syex.dailyupdate"
    }
}
