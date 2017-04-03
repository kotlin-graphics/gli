package gli

import glm.vec._3.Vec3i
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 03.04.2017.
 */

class coreSwizzle : StringSpec() {

    init {

        "swizzle" {

            val texture = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(1), 1, 1, 1)
            val swizzles = texture.swizzles

        }
    }
}
