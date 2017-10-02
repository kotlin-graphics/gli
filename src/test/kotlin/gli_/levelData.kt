package gli

import gli_.gli
import io.kotlintest.specs.StringSpec
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL33

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

        fun createTexture(filename: String): Int {

            val texture = gli.load(filename)
            if (texture.empty())
                return 0

            gli.gl.profile = gli.gl.Profile.GL33
            val format = gli.gl.translate(texture.format, texture.swizzles)
            val target = gli.gl.translate(texture.target)
            assert(texture.format.isCompressed && target == gli.gl.Target._2D)

            val textureName = intBufferBig(1)
            GL11.glGenTextures(textureName)
            glBindTexture(target, textureName)
            glTexParameteri(target, GL12.GL_TEXTURE_BASE_LEVEL, 0)
            glTexParameteri(target, GL12.GL_TEXTURE_MAX_LEVEL, texture.levels() - 1)
            glTexParameteriv(target, GL33.GL_TEXTURE_SWIZZLE_RGBA, format.swizzles)
            glTexStorage2D(target, texture.levels(), format.internal, texture.extent())
            for (level in 0 until texture.levels()) {
                glCompressedTexSubImage2D(
                        target, level, 0, 0, texture.extent(level),
                        format.internal, texture.data(0, 0, level))
            }
            return textureName[0]
        }
    }
}