package gli_

import gli_.tga.TgaImageReaderSpi
import io.kotlintest.specs.StringSpec
//import sun.awt.image.IntegerInterleavedRaster
import java.awt.image.DirectColorModel
import javax.imageio.ImageIO
import javax.imageio.spi.IIORegistry

class tga : StringSpec() {

    init {
//        "tga fail" {
//
//            val url = this.javaClass.classLoader.getResource("PlyonTexture.tga")
//            val image = ImageIO.read(url)
//
//            assert(image == null)
//        }

        "tga success" {

            val registry = IIORegistry.getDefaultInstance()
            registry.registerServiceProvider(TgaImageReaderSpi())

            val url = javaClass.classLoader.getResource("PlyonTexture.tga")
            val image = ImageIO.read(url)

            assert(image.type == 4)

            with(image.colorModel as DirectColorModel) {
                assert(redMask == 0xff)
                assert(greenMask == 0xff00)
                assert(blueMask == 0xff0000)
                assert(alphaMask == 0)
            }

//            with(image.raster as IntegerInterleavedRaster) {
            with(image.raster) {
                assert(width == 1024)
                assert(height == 1024)
                assert(numBands == 3)
                assert(sampleModelTranslateX == 0)
                assert(sampleModelTranslateY == 0)
//                assert(getDataOffset(0) == 0)
                assert(dataBuffer.offset == 0)
            }

            assert(image.propertyNames == null)

            assert(image.accelerationPriority == 0.5f)
        }
    }
}