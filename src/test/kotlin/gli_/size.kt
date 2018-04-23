package gli_

import glm_.glm
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import glm_.vec4.Vec4ub
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class size : StringSpec() {

    init {

        "can compute texture size" {

            // Scenario: Compute the size of a specialized 2d texture
            run {
                val texture = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(2), 1)

                texture.size shouldBe 2 * 2 * Vec4ub.size
                texture.size(0) shouldBe 2 * 2 * Vec4ub.size
                texture.size<Vec4ub>() shouldBe 2 * 2
                texture.size<Vec4ub>(0) shouldBe 2 * 2
            }

            // Scenario: Compute the size of a generic 2d texture
            run {
                val texture = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(2, 2, 1), 1, 1, 1)

                texture.size shouldBe 2 * 2 * Vec4ub.size
                texture.size(0) shouldBe 2 * 2 * Vec4ub.size
                texture.size<Vec4ub>() shouldBe 2 * 2
                texture.size<Vec4ub>(0) shouldBe 2 * 2
            }

            // Scenario: Compute the size of a specialized 2d texture with a mipmap chain
            run {
                val texture = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(2))

                texture.size shouldBe 5 * Vec4ub.size
                texture.size(0) shouldBe 4 * Vec4ub.size
                texture.size(1) shouldBe 1 * Vec4ub.size
                texture.size<Vec4ub>() shouldBe 5
                texture.size<Vec4ub>(0) shouldBe 4
                texture.size<Vec4ub>(1) shouldBe 1
            }

            // Scenario: Compute the size of a generic 2d texture with a mipmap chain
            run {
                val texture = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(2, 2, 1), 1, 1, 2)

                texture.size shouldBe 5 * Vec4ub.size
                texture.size(0) shouldBe 4 * Vec4ub.size
                texture.size(1) shouldBe 1 * Vec4ub.size
                texture.size<Vec4ub>() shouldBe 5
                texture.size<Vec4ub>(0) shouldBe 4
                texture.size<Vec4ub>(1) shouldBe 1
            }
        }//namespace can_compute_texture_size

        "can compute view size" {

            // Scenario: Compute the size of a specialized 2d texture with a mipmap chain
            run {
                val texture = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4))
                val view = Texture2d(gli.view(texture, 1, 2))

                view.size shouldBe 5 * Vec4ub.size
                view.size(0) shouldBe 4 * Vec4ub.size
                view.size(1) shouldBe 1 * Vec4ub.size
                view.size<Vec4ub>() shouldBe 5
                view.size<Vec4ub>(0) shouldBe 4
                view.size<Vec4ub>(1) shouldBe 1
            }

            // Scenario: Compute the size of a generic 2d texture with a mipmap chain
            run {
                val texture = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 1, 1, 3)
                val view = gli.view(Texture2d(texture), 1, 2)

                view.size shouldBe 5 * Vec4ub.size
                view.size(0) shouldBe 4 * Vec4ub.size
                view.size(1) shouldBe 1 * Vec4ub.size
                view.size<Vec4ub>() shouldBe 5
                view.size<Vec4ub>(0) shouldBe 4
                view.size<Vec4ub>(1) shouldBe 1
            }

            // Scenario: Compute the size of a specialized 2d array texture with a mipmap chain
            run {
                val texture = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                val view = Texture2dArray(gli.view(texture, 1, 1, 1, 2))

                view.size shouldBe 5 * Vec4ub.size
                view.size(0) shouldBe 4 * Vec4ub.size
                view.size(1) shouldBe 1 * Vec4ub.size
                view.size<Vec4ub>() shouldBe 5
                view.size<Vec4ub>(0) shouldBe 4
                view.size<Vec4ub>(1) shouldBe 1
            }
        }//namespace can_compute_view_size

        "can compute npot texture size" {

            run {
                val storage = Storage(Format.RGBA8_UNORM_PACK8, Vec3i(12, 12, 1), 1, 1, glm.levels(12))

                val levelSize0 = storage.levelSize(0)
                levelSize0 shouldBe 12 * 12 * Vec4ub.size

                val levelSize1 = storage.levelSize(1)
                levelSize1 shouldBe 6 * 6 * Vec4ub.size

                val levelSize2 = storage.levelSize(2)
                levelSize2 shouldBe 3 * 3 * Vec4ub.size

                val levelSize3 = storage.levelSize(3)
                levelSize3 shouldBe 1 * 1 * Vec4ub.size
            }

            run {
                val storage = Storage(Format.RGB_DXT1_UNORM_BLOCK8, Vec3i(12, 12, 1), 1, 1, glm.levels(12))
                val levelSize0 = storage.levelSize(0)
                val levelSizeA = 3 * 3 * Format.RGB_DXT1_UNORM_BLOCK8.blockSize
                levelSize0 shouldBe levelSizeA

                val levelSize1 = storage.levelSize(1)
                val levelSizeB = 2 * 2 * Format.RGB_DXT1_UNORM_BLOCK8.blockSize
                levelSize1 shouldBe levelSizeB

                val levelSize2 = storage.levelSize(2)
                val levelSizeC = 1 * 1 * Format.RGB_DXT1_UNORM_BLOCK8.blockSize
                levelSize2 shouldBe levelSizeC

                val levelSize3 = storage.levelSize(3)
                levelSize3 shouldBe levelSizeC
            }

            run {
                val blockCountA = glm.max(Vec3i(5, 5, 1) / Format.RGB_DXT1_UNORM_BLOCK8.blockExtend, 1)

                val blockCountB = glm.ceilMultiple(Vec3i(5, 5, 1), Format.RGB_DXT1_UNORM_BLOCK8.blockExtend) /
                        Format.RGB_DXT1_UNORM_BLOCK8.blockExtend
            }

            run {
                val texture = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(5))

                val sizeA1 = texture.size
                val sizeA2 = (5 * 5 + 2 * 2 + 1) * Vec4ub.size
                sizeA1 shouldBe sizeA2

                val sizeB1 = texture.size(0)
                val sizeB2 = 5 * 5 * Vec4ub.size
                sizeB1 shouldBe sizeB2

                val sizeC1 = texture.size(1)
                val sizeC2 = 2 * 2 * Vec4ub.size
                sizeC1 shouldBe sizeC2

                val sizeD1 = texture.size(2)
                val sizeD2 = 1 * 1 * Vec4ub.size
                sizeD1 shouldBe sizeD2
            }

            run {
                val texture = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(3))

                texture.size shouldBe (3 * 3 + 1) * Vec4ub.size
                texture.size(0) shouldBe 3 * 3 * Vec4ub.size
                texture.size(1) shouldBe 1 * 1 * Vec4ub.size
            }

            run {
                val texture = Texture2d(Format.RGB_DXT1_UNORM_BLOCK8, Vec2i(3), 1)

                texture.size shouldBe Format.RGB_DXT1_UNORM_BLOCK8.blockSize
            }

            run {
                val texture = Texture2d(Format.RGB_DXT1_UNORM_BLOCK8, Vec2i(9, 5), 1)

                val currentSize = texture.size
                val expectedSize = Format.RGB_DXT1_UNORM_BLOCK8.blockSize * 3 * 2

                currentSize shouldBe expectedSize
            }
        }
    }
}