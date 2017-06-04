package gli

import glm_.vec3.Vec3i
import glm_.vec4.Vec4b
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 03.04.2017.
 */

class coreStorage : StringSpec() {

    init {

        "storage layer size" {

            val storage = Storage(
                    Format.RGBA8_UNORM_PACK8,
                    Vec3i(2, 2, 1),
                    2, 1, 1)

            with(storage) {
                blockSize shouldBe Vec4b.size
                levelSize(0) shouldBe Vec4b.size * 2 * 2
                faceSize(0, levels - 1) == Vec4b.size * 2 * 2
                layerSize(0, faces - 1, 0, levels - 1) shouldBe Vec4b.size * 2 * 2
                size() shouldBe Vec4b.size * 2 * 2 * 2
            }
        }

        "storage face size" {

            val storage = Storage(
                    Format.RGBA8_UNORM_PACK8,
                    Vec3i(2, 2, 1),
                    1, 6, 1)

            with(storage) {

                blockSize shouldBe Vec4b.size

                levelSize(0) shouldBe Vec4b.size * 2 * 2

                faceSize(0, levels - 1) shouldBe Vec4b.size * 2 * 2

                layerSize(0, faces - 1, 0, levels - 1) shouldBe Vec4b.size * 2 * 2 * 6

                size() shouldBe Vec4b.size * 2 * 2 * 6
            }
        }
    }
}