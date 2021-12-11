import java.io.FileOutputStream
import java.util.Properties

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.0"
    java
}

val supportedManifestVersion = "v4"
group = "io.github.awesomemoder316.modgetcreate"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("com.google.dagger:dagger-compiler:2.40.5")
    implementation("com.diogonunes:JColor:5.2.0")
    implementation("com.google.dagger:dagger:2.40.5")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.3")
}

tasks {

    compileJava {
        options.release.set(11)
    }

    prepareKotlinBuildScriptModel {
        dependsOn("createVersionFile")
    }

    register("createVersionFile") {
        doLast {
            val fileOutputStream = FileOutputStream(
                "${rootProject.projectDir}" +
                        "${File.separator}src" +
                        "${File.separator}main" +
                        "${File.separator}resources" +
                        "${File.separator}version.properties"
            )

            val versionProperties = Properties();
            versionProperties.setProperty("ModgetCreateVersion", "v${rootProject.version}")
            versionProperties.setProperty("ModgetManifestVersion", supportedManifestVersion)
            versionProperties.store(fileOutputStream,
                "A file that contains ModgetCE version, and the targetted manifest version")

            fileOutputStream.close()
        }
    }

    shadowJar {
        archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
    }

    jar {
        manifest {
            attributes["Main-Class"] = "${rootProject.group}.ModgetCreateCE"
        }
    }
}