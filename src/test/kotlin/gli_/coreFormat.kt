package gli_

import io.kotest.matchers.shouldBe
import io.kotest.core.spec.style.StringSpec


class coreFormat : StringSpec() {

    init {

        "valid" {

            for (format in Format.FIRST .. Format.LAST)
                format.isValid shouldBe true
            Format.UNDEFINED.isValid shouldBe false
        }

        "component" {

            for (formatIndex in Format.FIRST.i until Format.COUNT) {
                val components = Format.values().first { it.i == formatIndex }.componentCount
                (components in 1..4) shouldBe true
            }
        }

        "compressed" {

            Format.R8_SRGB_PACK8.isCompressed shouldBe false
            Format.RGB_DXT1_SRGB_BLOCK8.isCompressed shouldBe true
        }

        "block" {
            Format.RGBA8_UNORM_PACK8.blockSize shouldBe 4
            Format.RGB10A2_UNORM_PACK32.blockSize shouldBe 4
        }
    }
}
