//package gli
//
//import glm_.set
//import glm_.vec3.Vec3i
//import glm_.vec4.Vec4b
//import io.kotlintest.matchers.shouldBe
//import io.kotlintest.specs.StringSpec
//
///**
// * Created by elect on 08/04/17.
// */
//
//class coreImage : StringSpec() {
//
//    init {
//
//        "image ctor" {
//
//            val imageA = Image(Format.RGBA8_UINT_PACK8, Vec3i(4, 4, 1))
//            val imageB = Image(Format.RGBA8_UINT_PACK8, Vec3i(4, 4, 1))
//            val imageC = imageA
//            val imageD = Image(imageA, Format.RGBA8_UINT_PACK8)
//            val imageE = Image(imageD, Format.RGBA8_UINT_PACK8)
//
//            imageA shouldBe imageB
//            imageC shouldBe imageB
//            imageA shouldBe imageE
//        }
//
//        "image data" {
//
//            val imageA = Image()
//            imageA.empty() shouldBe true
//
//            val imageB = Image(Format.RGBA8_UNORM_PACK8, Vec3i(1))
//            imageB.size shouldBe Vec4b.size
//
//            Vec4b(255, 127, 0, 255) to imageB.data()
//            imageB.notEmpty() shouldBe true
//        }
//
//        "image query" {
//
//            val image = Image(Format.RGBA8_UINT_PACK8, Vec3i(1))
//
//            image.size shouldBe Vec4b.size
//            image.notEmpty() shouldBe true
//            image.extent() shouldBe Vec3i(1)
//        }
//
//        "fetch" {
//
//            val image = Image(Format.RGBA8_UINT_PACK8, Vec3i(4, 2, 1))
//            image.data()[0] = Vec4b(255, 0, 0, 255)
//            image.data()[1] = Vec4b(255, 128, 0, 255)
//            image.data()[2] = Vec4b(255, 255, 0, 255)
//            image.data()[3] = Vec4b(128, 255, 0, 255)
//            image.data()[4] = Vec4b(0, 255, 0, 255)
//            image.data()[5] = Vec4b(0, 255, 255, 255)
//            image.data()[6] = Vec4b(0, 0, 255, 255)
//            image.data()[7] = Vec4b(255, 0, 255, 255)
//
//            val data0 = image.load(Vec3i(0, 0, 0), Vec4b())
//            val data1 = image.load(Vec3i(1, 0, 0), Vec4b())
//            val data2 = image.load(Vec3i(2, 0, 0), Vec4b())
//            val data3 = image.load(Vec3i(3, 0, 0), Vec4b())
//            val data4 = image.load(Vec3i(0, 1, 0), Vec4b())
//            val data5 = image.load(Vec3i(1, 1, 0), Vec4b())
//            val data6 = image.load(Vec3i(2, 1, 0), Vec4b())
//            val data7 = image.load(Vec3i(3, 1, 0), Vec4b())
//
//            data0 shouldBe Vec4b(255, 0, 0, 255)
//            data1 shouldBe Vec4b(255, 128, 0, 255)
//            data2 shouldBe Vec4b(255, 255, 0, 255)
//            data3 shouldBe Vec4b(128, 255, 0, 255)
//            data4 shouldBe Vec4b(0, 255, 0, 255)
//            data5 shouldBe Vec4b(0, 255, 255, 255)
//            data6 shouldBe Vec4b(0, 0, 255, 255)
//            data7 shouldBe Vec4b(255, 0, 255, 255)
//        }
//    }
//}