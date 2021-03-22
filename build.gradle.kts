import kx.LwjglModules.*
import kx.KxProject.*
import kx.kxImplementation
import kx.lwjglImplementation

plugins {
    val build = "0.7.0+63"
    id("kx.kotlin.11") version build
    id("kx.lwjgl") version build
    id("kx.dokka") version build
    id("kx.publish") version build
    java
}

version = "0.8.3.0-16+19"

repositories {
    maven("https://jitpack.io")
}

dependencies {

    kxImplementation(unsigned, kool, glm)

    // https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-core
    listOf(/*"-batik",*/ "-bmp", "-core", "-icns", "-iff", "-jpeg", "-metadata", "-pcx", "-pdf", "-pict", "-pnm",
            "-psd", "-sgi", "-tga", "-thumbsdb", "-tiff"/*, "-reference", "-clippath", "-hdr"*/).forEach {
        implementation("com.twelvemonkeys.imageio:imageio$it:3.5")
    }
    // https://mvnrepository.com/artifact/org.apache.xmlgraphics/batik-transcoder
    //implementation "org.apache.xmlgraphics:batik-transcoder:1.12"

    lwjglImplementation(jemalloc, opengl)
}