package gli

import glm.b
import glm.glm
import glm.set
import glm.vec2.Vec2i
import glm.vec4.Vec4b
import glm.vec4.Vec4ub
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 19.05.2017.
 */

class coreTextureCube : StringSpec() {

    init {

        "alloc" {

            val formats = listOf(Format.RGBA8_UNORM_PACK8, Format.RGB8_UNORM_PACK8, Format.R8_SNORM_PACK8,
                    Format.RGB_DXT1_UNORM_BLOCK8, Format.RGBA_BP_UNORM_BLOCK16, Format.RGBA32_SFLOAT_PACK32)

            val sizes = listOf(16, 32, 15, 17, 1)

            formats.forEach { fmt ->

                sizes.forEach {

                    val size = Vec2i(it)

                    val textureA = TextureCube(fmt, size, gli.levels(size))
                    val textureB = TextureCube(fmt, size)

                    textureA shouldBe textureB
                }
            }
        }

        "query" {

            with(TextureCube(Format.RGBA8_UINT_PACK8, Vec2i(2), 2)) {

                size() shouldBe Vec4ub.size * 5 * 6
                format shouldBe Format.RGBA8_UINT_PACK8
                levels() shouldBe 2
                empty() shouldBe false
                extent().x shouldBe 2
                extent().y shouldBe 2
            }
        }

        "texture2D access" {

            run {

                val texture2dA = Texture2d(Format.RGBA8_UINT_PACK8, Vec2i(2), 1)
                for (i in 0 until texture2dA.size())
                    texture2dA.data()[i] = i.b

                val texture2dB = Texture2d(Format.RGBA8_UINT_PACK8, Vec2i(2), 1)
                for (i in 0 until texture2dB.size())
                    texture2dB.data()[i] = (i + 100).b

                val textureCube = TextureCube(Format.RGBA8_UINT_PACK8, Vec2i(2), 2)

                // Todo
                // gli::copy(TextureCube, 0, Texture2DA);
                // gli::copy(TextureCube, 1, Texture2DB);

                // Error += TextureCube[0] == Texture2DA ? 0 : 1;
                // Error += TextureCube[1] == Texture2DB ? 0 : 1;
            }

            run {
                val textureCube = TextureCube(Format.RGBA8_UINT_PACK8, Vec2i(2), 1)
                assert(textureCube.notEmpty())

                val colors = listOf(
                        Vec4b(255, 0, 0, 255),
                        Vec4b(255, 255, 0, 255),
                        Vec4b(0, 255, 0, 255),
                        Vec4b(0, 255, 255, 255),
                        Vec4b(0, 0, 255, 255),
                        Vec4b(255, 255, 0, 255))

                colors.forEachIndexed { i, it ->
                    val texture2D = textureCube[i]
                    for (pixelIndex in 0 until 4)
                        texture2D.pData(pixelIndex, it)
                }

                for (texelIndex in 0 until textureCube.size() / Vec4b.size)
                    Vec4b(textureCube.data(), texelIndex * Vec4b.size) shouldBe colors[texelIndex / 4]
            }

            run {

                val textureCube = TextureCube(Format.RGBA8_UINT_PACK8, Vec2i(2), 2)
                assert(textureCube.notEmpty())

                val textureA = textureCube[0]
                val textureB = textureCube[1]

                val size0 = textureA.size()
                val size1 = textureB.size()

                println(size0)
            }
        }
    }
}