package gli_

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreLoadImage : StringSpec() {

    init {

        "load" {

            val textureTga = gli.load(uriOf("PlyonTexture.tga"))
//            textureTga.format shouldBe params.format
//
//            val kmg = params.filename + ".kmg"
//            gli.save(textureTga, kmg)
//            val textureSavedKMG = gli.load(kmg)
//            textureSavedKMG.format shouldBe params.format
//            textureSavedKMG shouldBe textureTga
//            Files.delete(pathOf(kmg))
//
//            gli.save(textureTga, ktx)
//            val textureSavedKTX = gli.load(ktx)
//            textureSavedKTX.format shouldBe params.format
//            textureSavedKTX shouldBe textureTga
//            Files.delete(pathOf(ktx))
        }
    }
}