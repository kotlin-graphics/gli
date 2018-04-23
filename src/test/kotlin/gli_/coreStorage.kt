package gli_

import gli_.buffer.intBufferBig
import glm_.vec3.Vec3i
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 03.04.2017.
 */

class coreStorage : StringSpec() {

    init {

        "storage layer size" {

            val storage = Storage(
                    format = Format.RGBA8_UNORM_PACK8,
                    extent = Vec3i(2, 2, 1),
                    layers = 2, faces = 1, levels = 1)

            for (i in 0..3)
                storage.data<Vec4b>()[i] = Vec4b(255, 127, 0, 255)
            for (i in 0..3)
                storage.data<Vec4b>()[i + 4] = Vec4b(0, 127, 255, 255)

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
                    format = Format.RGBA8_UNORM_PACK8,
                    extent = Vec3i(2, 2, 1),
                    layers = 1, faces = 6, levels = 1)

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