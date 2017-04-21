package gli

import io.kotlintest.specs.StringSpec
import org.lwjgl.opengl.GL11

/**
 * Created by GBarbieri on 21.04.2017.
 */

class levelData : StringSpec() {

    init {

        "main" {

            val TEXTURE_DIFFUSE = "kueken7_rgba8_srgb.dds"

            val texture = Texture2d(gli.loadDDS(javaClass.getResource("/$TEXTURE_DIFFUSE").toURI()))
            gl.profile = gl.Profile.GL32

            val format = gl.translate(texture.format, texture.swizzles)
            for (level in 0 until texture.levels()) {

                println("level: $level, internal format: ${format.internal}")
                println("size: (${texture[level].extent().x}, ${texture[level].extent().y})")
                println("format: (${format.external}, type: ${format.type})")
                println("data: (${texture[level].data()})")
            }
            texture.dispose()
        }
    }
}