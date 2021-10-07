import kx.KxProject.glm
import kx.KxProject.kool
import kx.KxProject.unsigned
import kx.Lwjgl
import kx.Lwjgl.Modules.jemalloc
import kx.Lwjgl.Modules.opengl
import kx.implementation

plugins {
    val build = "0.7.3+43"
    id("kx.kotlin") version build
//    id("kx.dokka") version build
    id("kx.publish") version build
    id("kx.dynamic-align") version build
    id("kx.util") version build
}

dependencies {

    implementation(unsigned, kool, glm)

    // https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-core
    listOf(/*"-batik",*/ "-bmp", "-core", "-icns", "-iff", "-jpeg", "-metadata", "-pcx", "-pdf", "-pict", "-pnm",
            "-psd", "-sgi", "-tga", "-thumbsdb", "-tiff"/*, "-reference", "-clippath", "-hdr"*/).forEach {
        implementation("com.twelvemonkeys.imageio:imageio$it:3.5")
    }
    // https://mvnrepository.com/artifact/org.apache.xmlgraphics/batik-transcoder
    //implementation "org.apache.xmlgraphics:batik-transcoder:1.12"

    Lwjgl { implementation(jemalloc, opengl) }
}