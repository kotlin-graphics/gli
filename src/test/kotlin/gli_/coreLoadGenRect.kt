package gli_

import glm_.vec3.Vec3i
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreLoadGenRect : StringSpec() {

    init {

        "load rect" {

            for (target in Target.RECT..Target.RECT_ARRAY)
                for (format in FORMAT_FIRST..FORMAT_LAST) {

                    if (format.isCompressed && target == Target._3D)
                        continue

                    val layers = if (target.isTargetArray) 2 else 1
                    val faces = if (target.isTargetCube) 6 else 1
                    val blockExtent = format.blockExtend

                    val texture = Texture(target, format, blockExtent * Vec3i(blockExtent.y, blockExtent.x, 1), layers, faces, 2)
                    texture.clear()

                    "gen_rect_test.kmg".let {
                        gli.save(texture, it)
                        val textureKMG = gli.load(it)
                        texture shouldBe textureKMG
                        Files.delete(pathOf(it))
                    }
                }
        }
    }
}