package gli_

import glm_.glm
import glm_.vec1.*
import glm_.vec2.*
import glm_.vec3.*
import glm_.vec4.*
import io.kotest.matchers.shouldBe
import io.kotest.core.spec.style.StringSpec

/**
 * Created by GBarbieri on 04.04.2017.
 */

class coreTexture3d : StringSpec() {

    init {

        "alloc" {

            val sizes = intArrayOf(16, 32, 15, 17, 1)

            for (format in Format.FIRST .. Format.LAST)
                sizes.forEach {

                    val size = Vec3i(it)

                    val textureA = Texture3d(format, size, glm.levels(size))
                    val textureB = Texture3d(format, size)

                    textureA shouldBe textureB
                }
        }

        "clear" {

            val orange = Vec4ub(255, 127, 0, 255)

            val texture = Texture3d(Format.RGBA8_UINT_PACK8, Vec3i(16))

            texture.clear(orange)
        }

        "query" {

            val texture = Texture3d(Format.RGBA8_UINT_PACK8, Vec3i(2))

            texture.size shouldBe Vec4b.size * 9
            texture.format shouldBe Format.RGBA8_UINT_PACK8
            texture.levels() shouldBe 2
            texture.notEmpty() shouldBe true
            texture.extent() shouldBe Vec3i(2)
        }

        "access" {

            run {
                val orange = Vec4ub(255, 127, 0, 255)

                val image = Image(Format.RGBA8_UINT_PACK8, Vec3i(2))

                val texture = Texture3d(Format.RGBA8_UINT_PACK8, Vec3i(2), 1)

                image.size shouldBe texture.size
            }

            run {
                val texture = Texture3d(Format.RGBA8_UINT_PACK8, Vec3i(2), 2)
                texture.notEmpty() shouldBe true

                val image0 = texture[0]
                val image1 = texture[1]

                val size0 = image0.size
                val size1 = image1.size

                size0 shouldBe Vec4b.size * 8
                size1 shouldBe Vec4b.size * 1

                image0.data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)
                image1.data<Vec4b>()[0] = Vec4b(0, 127, 255, 255)

                val pointerA = image0.data<Vec4b>()[0]
                val pointerB = image1.data<Vec4b>()[0]

                val pointer0 = texture.data<Vec4b>()[0]
                val pointer1 = texture.data<Vec4b>()[8]

                pointerA shouldBe pointer0
                pointerB shouldBe pointer1

                val colorA = image0.data<Vec4b>()[0]
                val colorB = image1.data<Vec4b>()[0]

                val color0 = pointer0
                val color1 = pointer1

                colorA shouldBe color0
                colorB shouldBe color1

                color0 shouldBe Vec4b(255, 127, 0, 255)
                color1 shouldBe Vec4b(0, 127, 255, 255)
            }
        }

        "size" {

            class Test(val format: Format, val dimensions: Vec3i, val size: Int)

            val tests = arrayOf(
                    Test(Format.RGBA8_UINT_PACK8, Vec3i(4), 256),
                    Test(Format.R8_UINT_PACK8, Vec3i(4), 64),
                    Test(Format.RGBA_DXT1_UNORM_BLOCK8, Vec3i(4), 32),
                    Test(Format.RGBA_DXT1_UNORM_BLOCK8, Vec3i(2), 32),
                    Test(Format.RGBA_DXT1_UNORM_BLOCK8, Vec3i(1), 32),
                    Test(Format.RGBA_DXT5_UNORM_BLOCK16, Vec3i(4), 64))

            tests.forEach {
                val texture = Texture3d(it.format, Vec3i(4), 1)
                val image = texture[0]

                image.size shouldBe it.size
                texture.size shouldBe it.size
            }
        }

        "load store" {

            run(Format.R32_SFLOAT_PACK32, arrayOf(
                    Vec1(+0.0f),
                    Vec1(+1.0f),
                    Vec1(-1.0f),
                    Vec1(+0.5f),
                    Vec1(-0.5f),
                    Vec1(+0.2f),
                    Vec1(-0.2f),
                    Vec1(+0.9f)))

            run(Format.RG32_SFLOAT_PACK32, arrayOf(
                    Vec2(-1.0f, -1.0f),
                    Vec2(-0.5f, -0.5f),
                    Vec2(+0.0f, +0.0f),
                    Vec2(+0.5f, +0.5f),
                    Vec2(+1.0f, +1.0f),
                    Vec2(-1.0f, +1.0f),
                    Vec2(-0.5f, +0.5f),
                    Vec2(+0.0f, +0.0f)))

            run(Format.RGB32_SFLOAT_PACK32, arrayOf(
                    Vec3(-1.0f, +0.0f, +1.0f),
                    Vec3(-0.5f, +0.0f, +0.5f),
                    Vec3(-0.2f, +0.0f, +0.2f),
                    Vec3(-0.0f, +0.0f, +0.0f),
                    Vec3(+0.1f, +0.2f, +0.3f),
                    Vec3(-0.1f, -0.2f, -0.3f),
                    Vec3(+0.7f, +0.8f, +0.9f),
                    Vec3(-0.7f, -0.8f, -0.9f)))

            run(Format.RGBA32_SFLOAT_PACK32, arrayOf(
                    Vec4(-1.0f, +0.0f, +1.0f, 1.0f),
                    Vec4(-0.5f, +0.0f, +0.5f, 1.0f),
                    Vec4(-0.2f, +0.0f, +0.2f, 1.0f),
                    Vec4(-0.0f, +0.0f, +0.0f, 1.0f),
                    Vec4(+0.1f, +0.2f, +0.3f, 1.0f),
                    Vec4(-0.1f, -0.2f, -0.3f, 1.0f),
                    Vec4(+0.7f, +0.8f, +0.9f, 1.0f),
                    Vec4(-0.7f, -0.8f, -0.9f, 1.0f)))

            arrayOf(
                    Vec1b(-128),
                    Vec1b(-127),
                    Vec1b(+127),
                    Vec1b(+64),
                    Vec1b(-64),
                    Vec1b(+1),
                    Vec1b(-1),
                    Vec1b(+0)).let {

                run(Format.R8_SINT_PACK8, it)
                run(Format.R8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec2b(-128, -96),
                    Vec2b(-64, 96),
                    Vec2b(-128, 64),
                    Vec2b(127, 32),
                    Vec2b(0, 126),
                    Vec2b(-48, 48),
                    Vec2b(-127, 127),
                    Vec2b(64, 0)).let {

                run(Format.RG8_UINT_PACK8, it)
                run(Format.RG8_UNORM_PACK8, it)
            }

            arrayOf(
                    Vec3b(-128, 0, 0),
                    Vec3b(-128, 127, 0),
                    Vec3b(-128, -96, 0),
                    Vec3b(127, -128, 0),
                    Vec3b(0, 127, 0),
                    Vec3b(0, 127, -127),
                    Vec3b(0, 64, -64),
                    Vec3b(-32, 32, 96)).let {

                run(Format.RGB8_SINT_PACK8, it)
                run(Format.RGB8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec4b(-127, 0, 0, 127),
                    Vec4b(-128, 96, 0, -128),
                    Vec4b(127, 64, 0, 1),
                    Vec4b(0, -64, 0, 2),
                    Vec4b(-95, 32, 0, 3),
                    Vec4b(95, -32, 127, 4),
                    Vec4b(-63, 16, -128, -1),
                    Vec4b(63, -16, -127, -2)).let {

                run(Format.RGBA8_SINT_PACK8, it)
                run(Format.RGBA8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec1ub(255),
                    Vec1ub(224),
                    Vec1ub(192),
                    Vec1ub(128),
                    Vec1ub(64),
                    Vec1ub(32),
                    Vec1ub(16),
                    Vec1ub(0)).let {

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
                    Vec2ub(0, 255),
                    Vec2ub(0, 0),
                    Vec2ub(255, 0)).let {

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
                    Vec3ub(0, 255, 255),
                    Vec3ub(0, 0, 255),
                    Vec3ub(255, 0, 255)).let {

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
                    Vec4ub(0, 255, 255, 255),
                    Vec4ub(0, 0, 255, 255),
                    Vec4ub(255, 0, 255, 255)).let {

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
                    Vec1us(32),
                    Vec1us(16),
                    Vec1us(0)).let {

                run(Format.R16_UINT_PACK16, it)
                run(Format.R16_UNORM_PACK16, it)
            }

            arrayOf(
                    Vec2us(255, 0),
                    Vec2us(255, 128),
                    Vec2us(255, 255),
                    Vec2us(128, 255),
                    Vec2us(0, 255),
                    Vec2us(0, 255),
                    Vec2us(0, 0),
                    Vec2us(255, 0)).let {

                run(Format.RG16_UINT_PACK16, it)
                run(Format.RG16_UNORM_PACK16, it)
            }

            arrayOf(
                    Vec3us(255, 0, 0),
                    Vec3us(255, 128, 0),
                    Vec3us(255, 255, 0),
                    Vec3us(128, 255, 0),
                    Vec3us(0, 255, 0),
                    Vec3us(0, 255, 255),
                    Vec3us(0, 0, 255),
                    Vec3us(255, 0, 255)).let {

                run(Format.RGB16_UINT_PACK16, it)
                run(Format.RGB16_UNORM_PACK16, it)
            }

            arrayOf(
                    Vec4us(255, 0, 0, 255),
                    Vec4us(255, 128, 0, 255),
                    Vec4us(255, 255, 0, 255),
                    Vec4us(128, 255, 0, 255),
                    Vec4us(0, 255, 0, 255),
                    Vec4us(0, 255, 255, 255),
                    Vec4us(0, 0, 255, 255),
                    Vec4us(255, 0, 255, 255)).let {

                run(Format.RGBA16_UINT_PACK16, it)
                run(Format.RGBA16_UNORM_PACK16, it)
            }

            run(Format.R32_UINT_PACK32, arrayOf(
                Vec1ui(65535),
                Vec1ui(32767),
                Vec1ui(192),
                Vec1ui(128),
                Vec1ui(64),
                Vec1ui(32),
                Vec1ui(16),
                Vec1ui(0)))

            run(Format.RG32_UINT_PACK32, arrayOf(
                Vec2ui(255, 0),
                Vec2ui(255, 128),
                Vec2ui(255, 255),
                Vec2ui(128, 255),
                Vec2ui(0, 255),
                Vec2ui(0, 255),
                Vec2ui(0, 0),
                Vec2ui(255, 0)))

            run(Format.RGB32_UINT_PACK32, arrayOf(
                Vec3ui(255, 0, 0),
                Vec3ui(255, 128, 0),
                Vec3ui(255, 255, 0),
                Vec3ui(128, 255, 0),
                Vec3ui(0, 255, 0),
                Vec3ui(0, 255, 255),
                Vec3ui(0, 0, 255),
                Vec3ui(255, 0, 255)))

            run(Format.RGBA32_UINT_PACK32, arrayOf(
                Vec4ui(255, 0, 0, 255),
                Vec4ui(255, 128, 0, 255),
                Vec4ui(255, 255, 0, 255),
                Vec4ui(128, 255, 0, 255),
                Vec4ui(0, 255, 0, 255),
                Vec4ui(0, 255, 255, 255),
                Vec4ui(0, 0, 255, 255),
                Vec4ui(255, 0, 255, 255)))
        }

        "clear2" {

            val black = Vec4ub(0, 0, 0, 255)
            val color = Vec4ub(255, 127, 0, 255)

            val texture = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(8), 5)
            texture clear black

            val texelA = texture.load<Vec4ub>(Vec3i(0), 0)
            val texelB = texture.load<Vec4ub>(Vec3i(0), 1)
            val texelC = texture.load<Vec4ub>(Vec3i(0), 2)

            texelA shouldBe black
            texelB shouldBe black
            texelC shouldBe black

            texture.clear(0, 0, 1, Vec4ub(255, 127, 0, 255))

            val coords = Vec3i(0)
            while (coords.z < texture.extent(1).z) {
                while (coords.y < texture.extent(1).y) {
                    while (coords.x < texture.extent(1).x) {
                        val texelD = texture.load<Vec4ub>(coords, 1)
                        texelD shouldBe color
                        coords.x++
                    }
                    coords.y++
                }
                coords.z++
            }

            val textureView = Texture3d(texture, 1, 1)

            val textureImage = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(4), 1)
            textureImage clear color

            textureView shouldBe textureImage
        }
    }

    fun index3D(texture: Texture3d, level: Int, x: Int, y: Int, z: Int) = texture.extent(level).let { x + y * it.x + z * it.x * it.y }

    inline fun <reified T> run(format: Format, testSamples: Array<T>) {

        val extent = Vec3i(4)

        val textureA = Texture3d(format, extent)
        textureA.clear()
        testSamples.forEachIndexed { i, test ->
            textureA.data<T>(0, 0, 1)[i] = test
        }

        val textureB = Texture3d(format, extent)
        textureB.clear()
        for (z in 0..1)
            for (y in 0..1)
                for (x in 0..1)
                    textureB.store(T::class, Vec3i(x, y, z), 1, testSamples[index3D(textureB, 1, x, y, z)])

        val loadedSamplesA = Array<T?>(8) { null }
        for (z in 0..1)
            for (y in 0..1)
                for (x in 0..1)
                    loadedSamplesA[index3D(textureB, 1, x, y, z)] = textureA.load(T::class, Vec3i(x, y, z), 1)

        val loadedSamplesB = Array<T?>(8) { null }
        for (z in 0..1)
            for (y in 0..1)
                for (x in 0..1)
                    loadedSamplesB[index3D(textureB, 1, x, y, z)] = textureB.load(T::class, Vec3i(x, y, z), 1)

        for (i in 0..7)
            loadedSamplesA[i] shouldBe testSamples[i]

        for (i in 0..7)
            loadedSamplesB[i] shouldBe testSamples[i]

        textureA shouldBe textureB

        val textureC = Texture3d(textureA, 1, 1)
        val textureD = Texture3d(textureB, 1, 1)

        textureC shouldBe textureD
    }
}