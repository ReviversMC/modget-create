dependencies {
    implementation(project(":core"))
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "${rootProject.group}.ModgetCreateCli"
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}Cli-${rootProject.version}.jar")
    }
}