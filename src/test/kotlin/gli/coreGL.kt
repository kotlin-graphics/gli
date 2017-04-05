package gli

import glm.vec._2.Vec2i
import io.kotlintest.specs.StringSpec
import io.kotlintest.matchers.shouldBe

/**
 * Created by GBarbieri on 03.04.2017.
 */

class coreGL :StringSpec() {

    init {

        "ktx" {

            val gl = gli.GL(GL.Profile.KTX)

            val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA))
            val formatA = gl.translate(textureA.format, textureA.swizzles)

            formatA.internal shouldBe GL.InternalFormat.RGB8_UNORM
            formatA.external shouldBe GL.ExternalFormat.RGB
            formatA.type shouldBe GL.TypeFormat.U8

            // Swizzle is not supported by KTX, so we always return the constant default swizzle
            formatA.swizzle shouldBe GL.Swizzles(GL.Swizzle.RED, GL.Swizzle.GREEN, GL.Swizzle.BLUE, GL.Swizzle.ALPHA)

            val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA))
            val formatB = gl.translate(textureB.format, textureB.swizzles)

            formatB.internal shouldBe GL.InternalFormat.RGB8_UNORM
            formatB.external shouldBe GL.ExternalFormat.BGR
            formatB.type shouldBe GL.TypeFormat.U8

            // Swizzle is not supported by KTX, so we always return the constant default swizzle
            formatA.swizzle shouldBe GL.Swizzles(GL.Swizzle.RED, GL.Swizzle.GREEN, GL.Swizzle.BLUE, GL.Swizzle.ALPHA)
        }
    }
}