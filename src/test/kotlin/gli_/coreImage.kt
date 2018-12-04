package gli_

import kool.set
import glm_.vec3.Vec3i
import glm_.vec4.Vec4b
import glm_.vec4.Vec4ub
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by elect on 08/04/17.
 */

class coreImage : StringSpec() {

    init {

        "image ctor" {

            val imageA = Image(Format.RGBA8_UINT_PACK8, Vec3i(4, 4, 1))
            val imageB = Image(Format.RGBA8_UINT_PACK8, Vec3i(4, 4, 1))
            val imageC = imageA
            val imageD = Image(imageA, Format.RGBA8_UINT_PACK8)
            val imageE = Image(imageD, Format.RGBA8_UINT_PACK8)

            imageA shouldBe imageB
            imageC shouldBe imageB
            imageA shouldBe imageE
        }

        "image data" {

            val imageA = Image()
            imageA.empty() shouldBe true

            val imageB = Image(Format.RGBA8_UNORM_PACK8, Vec3i(1))
            imageB.size shouldBe Vec4b.size

            Vec4b(255, 127, 0, 255) to imageB.data()
            imageB.notEmpty() shouldBe true
        }

        "image query" {

            val image = Image(Format.RGBA8_UINT_PACK8, Vec3i(1))

            image.size shouldBe Vec4b.size
            image.notEmpty() shouldBe true
            image.extent() shouldBe Vec3i(1)
        }

        "fetch" {

            val image = Image(Format.RGBA8_UINT_PACK8, Vec3i(4, 2, 1))
            image.data<Vec4ub>()[0] = Vec4ub(255, 0, 0, 255)
            image.data<Vec4ub>()[1] = Vec4ub(255, 128, 0, 255)
            image.data<Vec4ub>()[2] = Vec4ub(255, 255, 0, 255)
            image.data<Vec4ub>()[3] = Vec4ub(128, 255, 0, 255)
            image.data<Vec4ub>()[4] = Vec4ub(0, 255, 0, 255)
            image.data<Vec4ub>()[5] = Vec4ub(0, 255, 255, 255)
            image.data<Vec4ub>()[6] = Vec4ub(0, 0, 255, 255)
            image.data<Vec4ub>()[7] = Vec4ub(255, 0, 255, 255)

            val data0 = image.load<Vec4ub>(Vec3i(0, 0, 0))
            val data1 = image.load<Vec4ub>(Vec3i(1, 0, 0))
            val data2 = image.load<Vec4ub>(Vec3i(2, 0, 0))
            val data3 = image.load<Vec4ub>(Vec3i(3, 0, 0))
            val data4 = image.load<Vec4ub>(Vec3i(0, 1, 0))
            val data5 = image.load<Vec4ub>(Vec3i(1, 1, 0))
            val data6 = image.load<Vec4ub>(Vec3i(2, 1, 0))
            val data7 = image.load<Vec4ub>(Vec3i(3, 1, 0))

            data0 shouldBe Vec4ub(255, 0, 0, 255)
            data1 shouldBe Vec4ub(255, 128, 0, 255)
            data2 shouldBe Vec4ub(255, 255, 0, 255)
            data3 shouldBe Vec4ub(128, 255, 0, 255)
            data4 shouldBe Vec4ub(0, 255, 0, 255)
            data5 shouldBe Vec4ub(0, 255, 255, 255)
            data6 shouldBe Vec4ub(0, 0, 255, 255)
            data7 shouldBe Vec4ub(255, 0, 255, 255)
        }
    }
}