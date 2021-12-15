import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

import java.io.FileOutputStream
import java.util.*

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.1"
    java
}

val supportedManifestVersion = "v4"
group = "io.github.awesomemoder316.modgetcreate"
version = "1.0.0"


allprojects {
    apply {
        plugin("com.github.johnrengelman.shadow")
        plugin("java")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        annotationProcessor("com.google.dagger:dagger-compiler:2.40.5")
        implementation("com.diogonunes:JColor:5.2.0")
        implementation("com.google.dagger:dagger:2.40.5")
        implementation("com.squareup.moshi:moshi:1.13.0")
        implementation("com.squareup.okhttp3:okhttp:4.9.3")
        implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.3")
    }

    tasks {

        compileJava {
            options.release.set(11)
        }

        register<ConfigureShadowRelocation>("relocateShadowJar") {
            target = shadowJar.get()
            prefix = "${rootProject.group}.dependencies"
        }

        shadowJar {
            archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
            dependsOn("relocateShadowJar")
        }
    }
}

tasks {

    prepareKotlinBuildScriptModel {
        dependsOn("createVersionFile")
    }

    register("createVersionFile") {
        doLast {
            val fileOutputStream = FileOutputStream(
                "${rootProject.projectDir}" +
                        "${File.separator}core" +
                        "${File.separator}src" +
                        "${File.separator}main" +
                        "${File.separator}resources" +
                        "${File.separator}version.properties"
            )

            val versionProperties = Properties()
            versionProperties.setProperty("ModgetCreateVersion", "v${rootProject.version}")
            versionProperties.setProperty("ModgetManifestVersion", supportedManifestVersion)
            versionProperties.store(
                fileOutputStream,
                "A file that contains ModgetCE's version, and the targeted manifest version."
            )

            fileOutputStream.close()
        }
    }

}
