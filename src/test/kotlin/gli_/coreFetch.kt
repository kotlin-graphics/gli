package gli_

import glm_.b
import glm_.vec2.Vec2i
import io.kotlintest.specs.StringSpec

class coreFetch : StringSpec() {

    init {

        "fetch r8 unorm" {

            val textureA = Texture2d(Format.R8_UNORM_PACK8, Vec2i(2))
            run {
                textureA.store(Vec2i(0, 0), 0, 1.b)
                textureA.store(Vec2i(1, 0), 0, 2.b)
                textureA.store(Vec2i(1, 1), 0, 3)
                textureA.store(Vec2i(0, 1), 0, 4)
                textureA.store(Vec2i(0, 0), 1, 5)
                gli.saveDds(TextureA, "r8_unorm_4pixels.dds")
            }
        }
    }
}
