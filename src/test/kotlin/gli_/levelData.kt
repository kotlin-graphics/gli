package gli

import gli_.buffer.intBufferBig
import gli_.gl
import gli_.gli
import io.kotlintest.specs.StringSpec
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL13.glCompressedTexSubImage2D
import org.lwjgl.opengl.GL33
import org.lwjgl.opengl.GL42.glTexStorage2D

/**
 * Created by GBarbieri on 21.04.2017.
 */

class levelData : StringSpec() {

    init {

//        "main" {
//
//            val TEXTURE_DIFFUSE = "kueken7_rgba8_srgb.dds"
//
//            val texture = Texture2d(gli.loadDds(javaClass.getResource("/$TEXTURE_DIFFUSE").toURI()))
//            gl.profile = gl.Profile.GL32
//
//            val format = gl.translate(texture.format, texture.swizzles)
//            for (level in 0 until texture.levels()) {
//
//                println("level: $level, internal format: ${format.internal}")
//                println("size: (${texture[level].extent().x}, ${texture[level].extent().y})")
//                println("format: (${format.external}, type: ${format.type})")
//                println("data: (${texture[level].data()})")
//                val r = texture[level].data()[0]
//                val g = texture[level].data()[1]
//                val b = texture[level].data()[2]
//                val a = texture[level].data()[3]
////                val r = texture.data()[0]
////                val g = texture.data()[1]
////                val b = texture.data()[2]
////                val a = texture.data()[3]
//                println("pixel 0: ($r, $g, $b, $a)")
//            }
//            texture.dispose()
//        }
    }
}