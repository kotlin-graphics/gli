plugins {
    `java-platform`
    `maven-publish`
}

repositories {
    maven("https://jitpack.io")
}

val kx = "com.github.kotlin-graphics"
val glmVersion: String by project

javaPlatform.allowDependencies()

dependencies {
    api(platform("$kx.glm:bom:$glmVersion"))
    constraints.api("$kx:glm:$glmVersion")
}

publishing.publications.create<MavenPublication>("mavenJava") {
    from(components["javaPlatform"])
}
