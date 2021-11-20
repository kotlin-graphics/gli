import kx.*
import org.lwjgl.Lwjgl
import org.lwjgl.Lwjgl.Module.*

plugins {
    fun kx(vararg p: Pair<String, String>) = p.forEach { id("io.github.kotlin-graphics.${it.first}") version it.second }
    kx("align" to "0.0.7",
       "base" to "0.0.10",
       "publish" to "0.0.6",
       "utils" to "0.0.5")
    id("org.lwjgl.plugin") version "0.0.20"
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