package gli_

import glm_.vec3.Vec3i
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreLoadGen2d : StringSpec() {

    init {

        "load 2d" {

            for (format in FORMAT_FIRST..FORMAT_LAST) {

                val target = Target._2D

                val layers = if (target.isTargetArray) 2 else 1
                val faces = if (target.isTargetCube) 6 else 1
                val blockExtent = format.blockExtend

                val texture = Texture(target, format, blockExtent * Vec3i(blockExtent.y, blockExtent.x, 1), layers, faces, 2)
                texture.clear()

                "test2d.dds".let {
                    gli.save(texture, it)
                    val textureDDS = gli.load(it)
                    texture shouldBe textureDDS
                    Files.delete(pathOf(it))
                }

                "test2d.ktx".let {
                    gli.save(texture, it)
                    val textureKTX = gli.load(it)
                    texture shouldBe textureKTX
                    Files.delete(pathOf(it))
                }

                "test2d.kmg".let {
                    gli.save(texture, it)
                    val textureKMG = gli.load(it)
                    texture shouldBe textureKMG
                    Files.delete(pathOf(it))
                }
            }
        }
    }
}