import magik.createGithubPublication
import magik.github
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.lwjgl.lwjgl
import org.lwjgl.Lwjgl.Module.*

plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("org.lwjgl.plugin") version "0.0.34"
    id("elect86.magik") version "0.3.2"
    `maven-publish`
//    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    github("kotlin-graphics/mary")
}

dependencies {

    api("kotlin.graphics:glm:0.9.9.1-8")

    // https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-core
    listOf(/*"-batik",*/ "-bmp", "-core", "-icns", "-iff", "-jpeg", "-metadata", "-pcx", "-pdf", "-pict", "-pnm",
           "-psd", "-sgi", "-tga", "-thumbsdb", "-tiff"/*, "-reference", "-clippath", "-hdr"*/).forEach {
        implementation("com.twelvemonkeys.imageio:imageio$it:3.5")
    }
    // https://mvnrepository.com/artifact/org.apache.xmlgraphics/batik-transcoder
    //implementation "org.apache.xmlgraphics:batik-transcoder:1.12"

    lwjgl { implementation(jemalloc, opengl) }

    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
}

kotlin.jvmToolchain { languageVersion.set(JavaLanguageVersion.of(8)) }

tasks {
    withType<KotlinCompile<*>>().all {
        kotlinOptions { freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn") }
    }
    test { useJUnitPlatform() }
}

publishing {
    publications {
        createGithubPublication {
            from(components["java"])
            suppressAllPomMetadataWarnings()
        }
    }
    repositories { github { domain = "kotlin-graphics/mary" } }
}

java.withSourcesJar()