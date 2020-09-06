package gli_

import glm_.vec3.Vec3i
import io.kotest.matchers.shouldBe
import io.kotest.core.spec.style.StringSpec

class coreAddressing : StringSpec() {

    init {

        "layers" {

            class Test(val dimensions: Vec3i, val format: Format, val baseOffset: Int, val size: Int)

            val tests = arrayOf(
                    Test(Vec3i(4, 4, 1), Format.RGBA8_UINT_PACK8, 64, 128),
                    Test(Vec3i(4, 4, 1), Format.RGB16_SFLOAT_PACK16, 96, 192),
                    Test(Vec3i(4, 4, 1), Format.RGBA32_SFLOAT_PACK32, 256, 512),
                    Test(Vec3i(4, 4, 1), Format.RGBA_DXT1_UNORM_BLOCK8, 8, 16),
                    Test(Vec3i(8, 8, 1), Format.RGBA_DXT1_UNORM_BLOCK8, 32, 64),
                    Test(Vec3i(4, 4, 1), Format.R_ATI1N_SNORM_BLOCK8, 8, 16))

            for (test in tests) {

                val storage = Storage(
                        format = test.format,
                        extent = test.dimensions,
                        layers = 2,
                        faces = 1,
                        levels = 1)

                val baseOffset = storage.baseOffset(1, 0, 0)
                val size = storage.size()

                baseOffset shouldBe test.baseOffset
                size shouldBe test.size
            }
        }

        "faces" {

            class Test(val format: Format, val level: Int, val baseOffset: Int, val size: Int)

            val tests = arrayOf(
                    Test(Format.RGBA8_UINT_PACK8, 0, 0, 340),
                    Test(Format.RGBA8_UINT_PACK8, 1, 256, 340),
                    Test(Format.R8_UINT_PACK8, 1, 64, 85),
                    Test(Format.RGBA8_UINT_PACK8, 3, 336, 340),
                    Test(Format.RGBA32_SFLOAT_PACK32, 0, 0, 1360),
                    Test(Format.RGBA32_SFLOAT_PACK32, 1, 1024, 1360),
                    Test(Format.RGB_DXT1_UNORM_BLOCK8, 0, 0, 56),
                    Test(Format.RGB_DXT1_UNORM_BLOCK8, 1, 32, 56),
                    Test(Format.RGBA_DXT5_UNORM_BLOCK16, 1, 64, 112))

            for (test in tests) {

                val storage = Storage(
                        format = test.format,
                        extent = Vec3i(8, 8, 1),
                        layers = 1,
                        faces = 1,
                        levels = 4)

                val baseOffset = storage.baseOffset(0, 0, test.level)
                val size = storage.size()

                baseOffset shouldBe test.baseOffset
                size shouldBe test.size
            }
        }

        "levels" {

            class Test(val format: Format, val level: Int, val baseOffset: Int, val size: Int)

            val tests = arrayOf(
                    Test(Format.RGBA8_UINT_PACK8, 0, 0, 340),
                    Test(Format.RGBA8_UINT_PACK8, 1, 256, 340),
                    Test(Format.RGBA8_UINT_PACK8, 3, 336, 340),
                    Test(Format.RGBA32_SFLOAT_PACK32, 0, 0, 1360),
                    Test(Format.RGBA32_SFLOAT_PACK32, 1, 1024, 1360),
                    Test(Format.RGB_DXT1_UNORM_BLOCK8, 0, 0, 56),
                    Test(Format.RGBA_DXT1_UNORM_BLOCK8, 1, 32, 56))

            for (test in tests) {

                val storage = Storage(
                        format = test.format,
                        extent = Vec3i(8, 8, 1),
                        layers = 1,
                        faces = 1,
                        levels = 4)

                val baseOffset = storage.baseOffset(0, 0, test.level)
                val size = storage.size()

                baseOffset shouldBe test.baseOffset
                size shouldBe test.size
            }
        }
    }
}