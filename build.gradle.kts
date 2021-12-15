import java.io.FileOutputStream
import java.util.*

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.0"
    java
}

val supportedManifestVersion = "v4"
group = "io.github.awesomemoder316.modgetcreate"
version = "1.0.0"


allprojects {
    repositories {
        mavenCentral()
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
                "A file that contains ModgetCE's version, and the targeted manifest version"
            )

            fileOutputStream.close()
        }
    }
}
