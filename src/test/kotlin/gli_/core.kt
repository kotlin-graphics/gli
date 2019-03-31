package gli_

import glm_.glm
import glm_.vec2.Vec2i
import glm_.vec4.Vec4b
import io.kotlintest.matchers.numerics.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kool.*

class core : StringSpec() {

    init {

        "create texture storage" {

            val texture = Texture2d(Format.RGBA8_UINT_PACK8, Vec2i(256))
            texture.clear(Vec4b(255, 127, 0, 255))
            val levels = texture.levels()

            assert(levels > 1)

            assert(texture.notEmpty())

            val texelA = texture[0].data<Vec4b>()[0]
            val texelB = texture[0].data<Vec4b>()[1]
            val texelC = texture[0].data<Vec4b>()[2]
            val texelD = texture[0].data<Vec4b>()[3]

            val texel = Vec4b(255, 127, 0, 255)
            assert(texelA == texel)
            assert(texelB == texel)
            assert(texelC == texel)
            assert(texelD == texel)
        }

        "reset memset zero" { } // TODO
        "reset loop zero" { } // TODO

        "floorMultiple" {

            val a = glm.floorMultiple(3, 4)
            val b = glm.floorMultiple(6, 4)
            val c = glm.floorMultiple(8, 4)
            val d = glm.floorMultiple(9, 4)

            a shouldBe 0
            b shouldBe 4
            c shouldBe 8
            d shouldBe 8
        }

        "flipY" {
            val original = ByteArray(16) { it.toByte() }
            val expected = byteArrayOf(
                    12, 13, 14, 15,
                    8, 9, 10, 11,
                    4, 5, 6, 7,
                    0, 1, 2, 3)

            val result = flipY(original, 4, 4)
            result.pos shouldBe 0
            result.rem shouldBeGreaterThanOrEqual 16
            for(i in 0 until 16) {
                result[i] shouldBe expected[i]
            }
        }
    }
}