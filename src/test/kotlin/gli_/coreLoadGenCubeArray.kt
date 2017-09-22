package gli_

import glm_.vec3.Vec3i
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreLoadGenCubeArray : StringSpec() {

    init {

        "load cube array" {

            for (format in FORMAT_FIRST..FORMAT_LAST) {

                val target = Target.CUBE_ARRAY

                val layers = if (target.isTargetArray) 2 else 1
                val faces = if (target.isTargetCube) 6 else 1
                val blockExtent = format.blockExtend

                val texture = Texture(target, format, blockExtent * Vec3i(blockExtent.y, blockExtent.x, 1), layers, faces, 2)
                texture.clear()

                "test_cube_array.dds".let {
                    gli.save(texture, it)
                    val textureDDS = gli.load(it)
                    texture shouldBe textureDDS
                    Files.delete(pathOf(it))
                }

                "test_cube_array.ktx".let {
                    gli.save(texture, it)
                    val textureKTX = gli.load(it)
                    texture shouldBe textureKTX
                    Files.delete(pathOf(it))
                }

                "test_cube_array.kmg".let {
                    gli.save(texture, it)
                    val textureKMG = gli.load(it)
                    texture shouldBe textureKMG
                    Files.delete(pathOf(it))
                }
            }
        }
    }
}