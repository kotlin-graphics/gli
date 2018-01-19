package gli_

import glm_.glm
import glm_.vec1.*
import glm_.vec2.*
import glm_.vec3.*
import glm_.vec4.*
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

/**
 * Created by GBarbieri on 19.05.2017.
 */

class coreTextureCubeArray : StringSpec() {

    init {

        "alloc" {

            val sizes = intArrayOf(16, 32, 15, 17, 1)

            for (format in FORMAT_FIRST..FORMAT_LAST)
                sizes.forEach {

                    val size = Vec2i(it)

                    val textureA = TextureCubeArray(format, size, 2, glm.levels(size))
                    val textureB = TextureCubeArray(format, size, 2)

                    textureA shouldBe textureB
                }
        }

        "query" {

            with(TextureCubeArray(Format.RGBA8_UINT_PACK8, Vec2i(2), 1)) {

                size shouldBe Vec4ub.size * 5 * 6
                format shouldBe Format.RGBA8_UINT_PACK8
                levels() shouldBe 2
                empty() shouldBe false
                extent().x shouldBe 2
                extent().y shouldBe 2
            }

            with(TextureCubeArray(Format.RGBA8_UINT_PACK8, Vec2i(2), 4)) {

                size shouldBe Vec4ub.size * 5 * 6 * 4
                format shouldBe Format.RGBA8_UINT_PACK8
                levels() shouldBe 2
                empty() shouldBe false
                extent().x shouldBe 2
                extent().y shouldBe 2
            }
        }

        "textureCube access" {

            run {

                val textureCubeArray = TextureCubeArray(Format.RGBA8_UINT_PACK8, Vec2i(2), 2, 1)
                textureCubeArray.notEmpty() shouldBe true

                val colors = arrayOf(Vec4ub(255, 0, 0, 255), Vec4ub(0, 0, 255, 255))

                val textureCube = textureCubeArray[1]
                val pointerA = textureCube.data<Vec4ub>()[0]
                val pointerB = textureCubeArray.data<Vec4ub>()[textureCube.size<Vec4ub>()]
                pointerA shouldBe pointerB
            }

            run {
                val textureCube = TextureCube(Format.RGBA8_UINT_PACK8, Vec2i(2), 1)
                assert(textureCube.notEmpty())

                val colors = listOf(
                        Vec4ub(255, 0, 0, 255),
                        Vec4ub(255, 255, 0, 255),
                        Vec4ub(0, 255, 0, 255),
                        Vec4ub(0, 255, 255, 255),
                        Vec4ub(0, 0, 255, 255),
                        Vec4ub(255, 255, 0, 255))

                colors.forEachIndexed { i, it ->
                    val texture2D = textureCube[i]
                    for (pixelIndex in 0 until 4)
                        texture2D.data<Vec4ub>()[pixelIndex] = it
                }

                for (texelIndex in 0 until textureCube.size / Vec4b.size)
                    textureCube.data<Vec4ub>()[texelIndex] shouldBe colors[texelIndex / 4]
            }

            run {

                val textureCube = TextureCube(Format.RGBA8_UINT_PACK8, Vec2i(2), 2)
                assert(textureCube.notEmpty())

                val textureA = textureCube[0]
                val textureB = textureCube[1]

                val size0 = textureA.size
                val size1 = textureB.size

                size0 shouldBe Vec4b.size * 5
                size1 shouldBe Vec4b.size * 5

                textureA.data<Vec4ub>()[0] = Vec4ub(255, 127, 0, 255)
                textureB.data<Vec4ub>()[0] = Vec4ub(0, 127, 255, 255)

                val pointerA = textureA.data<Vec4ub>()[0]
                val pointerB = textureB.data<Vec4ub>()[0]

                val pointer0 = textureCube.data<Vec4ub>()[0]
                val pointer1 = textureCube.data<Vec4ub>()[5]

                pointerA shouldBe pointer0
                pointerB shouldBe pointer1

                val colorA = textureA.data<Vec4ub>()[0]
                val colorB = textureB.data<Vec4ub>()[0]

                val color0 = pointer0
                val color1 = pointer1

                colorA shouldBe color0
                colorB shouldBe color1

                color0 shouldBe Vec4ub(255, 127, 0, 255)
                color1 shouldBe Vec4ub(0, 127, 255, 255)
            }

            run {
                val textureCube = TextureCube(Format.RGBA8_UINT_PACK8, Vec2i(2), 1)

                val sizeA = textureCube.size
                sizeA shouldBe Vec4b.size * 4 * 6

                val texture2D = textureCube[0]

                val size0 = texture2D.size
                size0 shouldBe Vec4ub.size * 4

                texture2D.data<Vec4ub>()[0] = Vec4ub(255, 127, 0, 255)

                val pointerA = texture2D.data<Vec4ub>()[0]
                val pointer0 = textureCube.data<Vec4ub>()[0]
                pointerA shouldBe pointer0

                val colorA = pointerA
                val color0 = pointer0

                colorA shouldBe color0

                colorA shouldBe Vec4ub(255, 127, 0, 255)
                color0 shouldBe Vec4ub(255, 127, 0, 255)
            }
        }

        "textureCube size" {

            class Test(val format: Format, val dimensions: Vec2i, val size: Int)

            val tests = arrayOf(
                    Test(Format.RGBA8_UINT_PACK8, Vec2i(4), 384 * 4),
                    Test(Format.R8_UINT_PACK8, Vec2i(4), 96 * 4),
                    Test(Format.RGBA_DXT1_UNORM_BLOCK8, Vec2i(4), 48 * 4),
                    Test(Format.RGBA_DXT1_UNORM_BLOCK8, Vec2i(2), 48 * 4),
                    Test(Format.RGBA_DXT1_UNORM_BLOCK8, Vec2i(1), 48 * 4),
                    Test(Format.RGBA_DXT5_UNORM_BLOCK16, Vec2i(4), 96 * 4))

            tests.forEach {
                val texture = TextureCubeArray(it.format, Vec2i(4), 4, 1)
                val size = texture.size
                size shouldBe it.size
            }
        }

        "clear" {

            val orange = Vec4ub(255, 127, 0, 255)

            val texture = TextureCubeArray(Format.RGBA8_UINT_PACK8, Vec2i(4), 4, 1)

            texture.clear(orange)
        }

        "load" {

            val color = arrayOf(arrayOf(
                    Vec4ub(255, 0, 0, 255),
                    Vec4ub(255, 127, 0, 255),
                    Vec4ub(255, 255, 0, 255),
                    Vec4ub(0, 255, 0, 255),
                    Vec4ub(0, 255, 255, 255),
                    Vec4ub(0, 0, 255, 255)
            ), arrayOf(
                    Vec4ub(255, 127, 127, 255),
                    Vec4ub(255, 191, 127, 255),
                    Vec4ub(255, 255, 127, 255),
                    Vec4ub(127, 255, 127, 255),
                    Vec4ub(127, 255, 255, 255),
                    Vec4ub(127, 127, 255, 255)))

            val texture = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(1), 2)

            for (layer in 0 until texture.layers())
                for (face in 0 until texture.faces())
                    texture[layer][face].clear(color[layer][face])

            val ktx = "cube_rgba8_unorm.ktx"
            val dds = "cube_rgba8_unorm.dds"
            gli.save(texture, ktx)
            gli.save(texture, dds)

            val textureKTX = gli.load(ktx)
            val textureDDS = gli.load(dds)

            textureKTX shouldBe texture
            textureDDS shouldBe texture
            Files.delete(pathOf(ktx))
            Files.delete(pathOf(dds))
        }

        "load store" {

            run(Format.R32_SFLOAT_PACK32, arrayOf(
                    Vec1(+0.0f),
                    Vec1(+1.0f),
                    Vec1(-1.0f),
                    Vec1(+0.5f),
                    Vec1(-0.5f),
                    Vec1(+0.2f)))

            run(Format.RG32_SFLOAT_PACK32, arrayOf(
                    Vec2(-1.0f, -1.0f),
                    Vec2(-0.5f, -0.5f),
                    Vec2(+0.0f, +0.0f),
                    Vec2(+0.5f, +0.5f),
                    Vec2(+1.0f, +1.0f),
                    Vec2(-1.0f, +1.0f)))

            run(Format.RGB32_SFLOAT_PACK32, arrayOf(
                    Vec3(-1.0f, +0.0f, +1.0f),
                    Vec3(-0.5f, +0.0f, +0.5f),
                    Vec3(-0.2f, +0.0f, +0.2f),
                    Vec3(-0.0f, +0.0f, +0.0f),
                    Vec3(+0.1f, +0.2f, +0.3f),
                    Vec3(-0.1f, -0.2f, -0.3f)))

            run(Format.RGBA32_SFLOAT_PACK32, arrayOf(
                    Vec4(-1.0f, +0.0f, +1.0f, 1.0f),
                    Vec4(-0.5f, +0.0f, +0.5f, 1.0f),
                    Vec4(-0.2f, +0.0f, +0.2f, 1.0f),
                    Vec4(-0.0f, +0.0f, +0.0f, 1.0f),
                    Vec4(+0.1f, +0.2f, +0.3f, 1.0f),
                    Vec4(-0.1f, -0.2f, -0.3f, 1.0f)))

            arrayOf(
                    Vec1b(-128),
                    Vec1b(-127),
                    Vec1b(+127),
                    Vec1b(+64),
                    Vec1b(-64),
                    Vec1b(+1)).let {

                run(Format.R8_SINT_PACK8, it)
                run(Format.R8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec2b(-128, -96),
                    Vec2b(-64, 96),
                    Vec2b(-128, 64),
                    Vec2b(127, 32),
                    Vec2b(0, 126),
                    Vec2b(-48, 48)).let {

                run(Format.RG8_UINT_PACK8, it)
                run(Format.RG8_UNORM_PACK8, it)
            }

            arrayOf(
                    Vec3b(-128, 0, 0),
                    Vec3b(-128, 127, 0),
                    Vec3b(-128, -96, 0),
                    Vec3b(127, -128, 0),
                    Vec3b(0, 127, 0),
                    Vec3b(0, 127, -127)).let {

                run(Format.RGB8_SINT_PACK8, it)
                run(Format.RGB8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec4b(-127, 0, 0, 127),
                    Vec4b(-128, 96, 0, -128),
                    Vec4b(127, 64, 0, 1),
                    Vec4b(0, -64, 0, 2),
                    Vec4b(-95, 32, 0, 3),
                    Vec4b(95, -32, 127, 4)).let {

                run(Format.RGBA8_SINT_PACK8, it)
                run(Format.RGBA8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec1ub(255),
                    Vec1ub(224),
                    Vec1ub(192),
                    Vec1ub(128),
                    Vec1ub(64),
                    Vec1ub(32)).let {

                run(Format.R8_UINT_PACK8, it)
                run(Format.R8_UNORM_PACK8, it)
                run(Format.R8_SRGB_PACK8, it)
            }

            arrayOf(
                    Vec2ub(255, 0),
                    Vec2ub(255, 128),
                    Vec2ub(255, 255),
                    Vec2ub(128, 255),
                    Vec2ub(0, 255),
                    Vec2ub(0, 255)).let {

                run(Format.RG8_UINT_PACK8, it)
                run(Format.RG8_UNORM_PACK8, it)
                run(Format.RG8_SRGB_PACK8, it)
            }

            arrayOf(
                    Vec3ub(255, 0, 0),
                    Vec3ub(255, 128, 0),
                    Vec3ub(255, 255, 0),
                    Vec3ub(128, 255, 0),
                    Vec3ub(0, 255, 0),
                    Vec3ub(0, 255, 255)).let {

                run(Format.RGB8_UINT_PACK8, it)
                run(Format.RGB8_UNORM_PACK8, it)
                run(Format.RGB8_SRGB_PACK8, it)
            }

            arrayOf(
                    Vec4ub(255, 0, 0, 255),
                    Vec4ub(255, 128, 0, 255),
                    Vec4ub(255, 255, 0, 255),
                    Vec4ub(128, 255, 0, 255),
                    Vec4ub(0, 255, 0, 255),
                    Vec4ub(0, 255, 255, 255)).let {

                run(Format.RGBA8_UINT_PACK8, it)
                run(Format.RGBA8_UNORM_PACK8, it)
                run(Format.RGBA8_SRGB_PACK8, it)
            }


            arrayOf(
                    Vec1us(65535),
                    Vec1us(32767),
                    Vec1us(192),
                    Vec1us(128),
                    Vec1us(64),
                    Vec1us(32)).let {

                run(Format.R16_UINT_PACK16, it)
                run(Format.R16_UNORM_PACK16, it)
            }

            arrayOf(
                    Vec2us(255, 0),
                    Vec2us(255, 128),
                    Vec2us(255, 255),
                    Vec2us(128, 255),
                    Vec2us(0, 255),
                    Vec2us(0, 255)).let {

                run(Format.RG16_UINT_PACK16, it)
                run(Format.RG16_UNORM_PACK16, it)
            }

            arrayOf(
                    Vec3us(255, 0, 0),
                    Vec3us(255, 128, 0),
                    Vec3us(255, 255, 0),
                    Vec3us(128, 255, 0),
                    Vec3us(0, 255, 0),
                    Vec3us(0, 255, 255)).let {

                run(Format.RGB16_UINT_PACK16, it)
                run(Format.RGB16_UNORM_PACK16, it)
            }

            arrayOf(
                    Vec4us(255, 0, 0, 255),
                    Vec4us(255, 128, 0, 255),
                    Vec4us(255, 255, 0, 255),
                    Vec4us(128, 255, 0, 255),
                    Vec4us(0, 255, 0, 255),
                    Vec4us(0, 255, 255, 255)).let {

                run(Format.RGBA16_UINT_PACK16, it)
                run(Format.RGBA16_UNORM_PACK16, it)
            }

            arrayOf(
                    Vec1ui(65535),
                    Vec1ui(32767),
                    Vec1ui(192),
                    Vec1ui(128),
                    Vec1ui(64),
                    Vec1ui(32)).let {

                run(Format.R32_UINT_PACK32, it)
            }

            arrayOf(
                    Vec2ui(255, 0),
                    Vec2ui(255, 128),
                    Vec2ui(255, 255),
                    Vec2ui(128, 255),
                    Vec2ui(0, 255),
                    Vec2ui(0, 255)).let {

                run(Format.RG32_UINT_PACK32, it)
            }

            arrayOf(
                    Vec3ui(255, 0, 0),
                    Vec3ui(255, 128, 0),
                    Vec3ui(255, 255, 0),
                    Vec3ui(128, 255, 0),
                    Vec3ui(0, 255, 0),
                    Vec3ui(0, 255, 255)).let {

                run(Format.RGB32_UINT_PACK32, it)
            }

            arrayOf(
                    Vec4ui(255, 0, 0, 255),
                    Vec4ui(255, 128, 0, 255),
                    Vec4ui(255, 255, 0, 255),
                    Vec4ui(128, 255, 0, 255),
                    Vec4ui(0, 255, 0, 255),
                    Vec4ui(0, 255, 255, 255)).let {

                run(Format.RGBA32_UINT_PACK32, it)
            }
        }

        "clear" {

            val black = Vec4ub(0, 0, 0, 255)
            val color = Vec4ub(255, 127, 0, 255)

            val texture = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1)
            texture clear black

            val texelA = texture.load<Vec4ub>(Vec2i(0), 0, 0, 0)
            val texelB = texture.load<Vec4ub>(Vec2i(0), 0, 0, 1)
            val texelC = texture.load<Vec4ub>(Vec2i(0), 0, 0, 2)

            texelA shouldBe black
            texelB shouldBe black
            texelC shouldBe black

            for (faceIndex in 0 until texture.faces())
                texture.clear(0, faceIndex, 1, color)

            val coords = Vec2i(0)
            while (coords.y < texture.extent(1).y) {
                while (coords.x < texture.extent(1).x) {
                    val texelD = texture.load<Vec4ub>(coords, 0, 0, 1)
                    texelD shouldBe color
                    coords.x++
                }
                coords.y++
            }

            val textureView = TextureCubeArray(texture, 0, 0, 0, 5, 1, 1)

            val textureImage = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(2), 1, 1)
            textureImage clear color

            textureView shouldBe textureImage

            val textureCopy = TextureCubeArray(gli.duplicate(textureView))
            textureView shouldBe textureCopy
        }
    }

    inline fun <reified T> run(format: Format, testSamples: Array<T>) {

        val dimensions = Vec2i(2)
        val layer = 1
        val level = 1

        val textureA = TextureCubeArray(format, dimensions, 3)
        textureA.clear()
        for (faceIndex in 0..5)
            textureA.data<T>(layer, faceIndex, level)[0] = testSamples[faceIndex]

        val textureB = TextureCubeArray(format, dimensions, 3)
        textureB.clear()
        for (faceIndex in 0..5)
            textureB.store(Vec2i(0), layer, faceIndex, level, testSamples[faceIndex])

        val loadedSamplesA = Array(6, { textureA.load<T>(Vec2i(0), layer, it, level) })

        val loadedSamplesB = Array(6, { textureB.load<T>(Vec2i(0), layer, it, level) })

        for (faceIndex in 0..5)
            loadedSamplesA[faceIndex] shouldBe testSamples[faceIndex]

        for (faceIndex in 0..5)
            loadedSamplesB[faceIndex] shouldBe testSamples[faceIndex]

        textureA shouldBe textureB

        val textureC = TextureCubeArray(textureA, layer, layer, 0, 5, level, level)
        val textureD = TextureCubeArray(textureB, layer, layer, 0, 5, level, level)

        textureC shouldBe textureD
    }
}