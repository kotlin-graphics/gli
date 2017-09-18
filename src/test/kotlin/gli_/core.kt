package gli_

import glm_.vec2.Vec2i
import glm_.vec4.Vec4b
import io.kotlintest.matchers.gt
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class core : StringSpec() {

    init {

        "create texture storage" {

            val texture = Texture2d(Format.RGBA8_UINT_PACK8, Vec2i(256))
            texture.clear<Vec4b>(255, 127, 0, 255)
            val levels = texture.levels()

            levels should gt(1)

            assert(texture.notEmpty())

            val pointer = texture[0].data()!!

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

            val a = glmTemp.floorMultiple(3, 4)
            val b = glmTemp.floorMultiple(6, 4)
            val c = glmTemp.floorMultiple(8, 4)
            val d = glmTemp.floorMultiple(9, 4)

            a shouldBe 0
            b shouldBe 4
            c shouldBe 8
            d shouldBe 8
        }
    }
}