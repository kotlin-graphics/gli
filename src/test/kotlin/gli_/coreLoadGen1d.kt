package gli_

import glm_.vec3.Vec3i
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreLoadGen1d : StringSpec() {

    init {

        "load 1d" {

            for (format in FORMAT_FIRST .. FORMAT_LAST) {

                val target = Target._1D

                if ((format.isCompressed && (target.isTarget1d || target == Target._3D)) || target.isTargetRect)
                    continue

                val layers = if (target.isTargetArray) 2 else 1
                val faces = if (target.isTargetCube) 6 else 1
                val blockExtent = format.blockExtend

                val texture = Texture(target, format, blockExtent * Vec3i(blockExtent.y, blockExtent.x, 1), layers, faces, 2)
                texture.clear()

                if(format == Format.L8_UNORM_PACK8)
                    println()

                var filename = "test1d.dds"
                gli.save(texture, filename)
                val textureDDS = gli.load(filename)
                texture shouldBe textureDDS
                Files.delete(pathOf(filename))

                filename = "test1d.ktx"
                gli.save(texture, filename)
                val textureKTX = gli.load(filename)
                texture shouldBe textureKTX
                Files.delete(pathOf(filename))

                filename = "test1d.kmg"
                gli.save(texture, filename)
                val textureKMG = gli.load(filename)
                texture shouldBe textureKMG
                Files.delete(pathOf(filename))
            }
        }
    }
}