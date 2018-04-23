package gli_

import glm_.b
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import glm_.vec3.Vec3i
import glm_.vec4.Vec4ub
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

class testCopySub : StringSpec() {

    init {

        "sub copy" {

            val source = Texture2d(Format.R8_UNORM_PACK8, Vec2i(4), 1)
            source clear 0.b
            source.store(Vec2i(1, 1), 0, 1.b)
            source.store(Vec2i(2, 1), 0, 2.b)
            source.store(Vec2i(2, 2), 0, 3.b)
            source.store(Vec2i(1, 2), 0, 4.b)

            val destination = Texture2d(source.format, source.extent_(), source.levels())
            destination clear 255.b

            destination.copy(source, 0, 0, 0, Vec3i(1, 1, 0), 0, 0, 0,
                    Vec3i(1, 1, 0), Vec3i(2, 2, 1))
            for (indexY in 1..2)
                for (indexX in 1..2)
                    source.load<Byte>(Vec2i(indexX, indexY), 0) shouldBe destination.load<Byte>(Vec2i(indexX, indexY), 0)
        }

        "sub copy2" {

            val source = Texture2d(Format.R8_UNORM_PACK8, Vec2i(5, 4), 1)
            source clear 0.b
            source.store(Vec2i(1, 1), 0, 1.b)
            source.store(Vec2i(2, 1), 0, 2.b)
            source.store(Vec2i(2, 2), 0, 3.b)
            source.store(Vec2i(1, 2), 0, 4.b)

            val destination = Texture2d(source.format, source.extent_(), source.levels())
            destination clear 255.b

            destination.copy(source, 0, 0, 0, Vec3i(1, 1, 0), 0, 0, 0,
                    Vec3i(1, 1, 0), Vec3i(2, 2, 1))
            for (indexY in 0 until source.extent().y)
                for (indexX in 0 until source.extent().x) {
                    val texelCoord = Vec2i(indexX, indexY)
                    val texelSrc = source.load<Byte>(texelCoord, 0)
                    val texelDst = destination.load<Byte>(texelCoord, 0)
                    (texelSrc == texelDst || (texelSrc == 0.b && texelDst == 255.b)) shouldBe true
                }
        }

        "sub copy rgb32f" {

            val source = Texture2d(Format.RGB32_SFLOAT_PACK32, Vec2i(4, 2), 1)
            for (texelIndex in 0 until source.size<Vec3>())
                source.data<Vec3>()[texelIndex] = Vec3(texelIndex + 1)

            val destination = Texture2d(source.format, source.extent_(), source.levels())
            destination clear Vec3(255)

            destination.copy(source, 0, 0, 0, Vec3i(1, 1, 0), 0, 0, 0,
                    Vec3i(1, 1, 0), Vec3i(2, 1, 1))
            for (indexY in 0 until source.extent().y)
                for (indexX in 0 until source.extent().x) {
                    val texelCoord = Vec2i(indexX, indexY)
                    val texelSrc = source.load<Vec3>(texelCoord, 0)
                    val texelDst = destination.load<Vec3>(texelCoord, 0)
                    (texelSrc == texelDst || (/*texelSrc == gli::u8vec4(0) &&*/ texelDst == Vec3(255))) shouldBe true
                }
        }

        "sub copy rgba8" {

            val source = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4, 2), 1)
            for (texelIndex in 0 until source.size<Vec4ub>())
                source.data<Vec4ub>()[texelIndex] = Vec4ub(texelIndex + 1)

            val destination = Texture2d(source.format, source.extent_(), source.levels())
            destination clear Vec4ub(255)

            destination.copy(source, 0, 0, 0, Vec3i(1, 1, 0), 0, 0, 0,
                    Vec3i(1, 1, 0), Vec3i(2, 1, 1))
            for (indexY in 0 until source.extent().y)
                for (indexX in 0 until source.extent().x) {
                    val texelCoord = Vec2i(indexX, indexY)
                    val texelSrc = source.load<Vec4ub>(texelCoord, 0)
                    val texelDst = destination.load<Vec4ub>(texelCoord, 0)
                    (texelSrc == texelDst || (/*texelSrc == gli::u8vec4(0) &&*/ texelDst == Vec4ub(255))) shouldBe true
                }
        }

        "sub clear rgba8" {

            val clear = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4, 2), 1)
            clear clear Vec4ub(0)

            val source = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4, 2), 1)
            source clear Vec4ub(0)
            source.clear(0, 0, 0, Vec3i(1, 1, 0), Vec3i(2, 1, 1), Vec4ub(255))

            source shouldNotBe clear

            val destination = Texture2d(source.format, source.extent_(), source.levels())
            destination clear Vec4ub(0)
            destination.copy(source, 0, 0, 0, Vec3i(1, 1, 0), 0, 0, 0,
                    Vec3i(1, 1, 0), Vec3i(2, 1, 1))

            destination shouldNotBe clear

            source shouldBe destination
        }
    }
}