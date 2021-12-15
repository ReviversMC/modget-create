plugins {
    id("com.github.johnrengelman.shadow")
    java
}

dependencies {
    annotationProcessor("com.google.dagger:dagger-compiler:2.40.5")
    implementation(project(":core"))
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

    jar {
        manifest {
            attributes["Main-Class"] = "${rootProject.group}.ModgetCreate"
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}Cli-${rootProject.version}.jar")

        relocate("com.diogonunes:JColor", "${rootProject.group}.dependencies.jcolor")
        relocate("com.google.dagger", "${rootProject.group}.dependencies.dagger")
        relocate("com.squareup.moshi", "${rootProject.group}.dependencies.moshi")
        relocate("com.squareup.okhttp3", "${rootProject.group}.dependencies.okhttp3")

    }
}