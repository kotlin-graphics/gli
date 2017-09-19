package gli_

import glm_.vec2.Vec2i
import io.kotlintest.specs.StringSpec

class coreFetch : StringSpec() {

    init {

        "fetch r8 unorm" {

            val textureA = Texture2d(Format.R8_UNORM_PACK8, Vec2i(2))
            run {
                textureA.store(Vec2i( 0), 0, Vec1b(1));
                textureA.store<glm::u8vec1>(gli::texture2d::extent_type(1, 0), 0, glm::u8vec1(2));
                textureA.store<glm::u8vec1>(gli::texture2d::extent_type(1, 1), 0, glm::u8vec1(3));
                textureA.store<glm::u8vec1>(gli::texture2d::extent_type(0, 1), 0, glm::u8vec1(4));
                textureA.store<glm::u8vec1>(gli::texture2d::extent_type(0, 0), 1, glm::u8vec1(5));
                gli::save_dds(TextureA, "r8_unorm_4pixels.dds");
            }
        }
    }
}
