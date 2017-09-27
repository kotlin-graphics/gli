package gli_

import glm_.vec1.Vec1
import glm_.vec1.Vec1b
import glm_.vec1.Vec1i
import glm_.vec2.Vec2
import glm_.vec2.Vec2b
import glm_.vec3.Vec3
import glm_.vec3.Vec3b
import glm_.vec3.Vec3i
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 04.04.2017.
 */

class coreTexture1d_ : StringSpec() {

    inline fun <reified T> run(format: Format, testSamples: Array<T>) {

        val dimensions = Vec1i(16)
        val texelCoord = Array(8, { Vec1i(it) })

        val textureA = Texture1d(format, dimensions)
        textureA.clear()
        testSamples.forEachIndexed { i, test ->
            textureA.data<T>(0, 0, 1)[i] = test
        }

        val textureB = Texture1d(format, dimensions)
        textureB.clear()
        testSamples.forEachIndexed { i, test ->
            textureB.store(texelCoord[i], 1, test)
        }

        val loadedSamplesA = Array(8, { textureA.load<T>(texelCoord[it], 1) })

        val loadedSamplesB = Array(8, { textureB.load<T>(texelCoord[it], 1) })

        for (i in 0..7)
            loadedSamplesA[i] shouldBe testSamples[i]

        for (i in 0..7)
            loadedSamplesB[i] shouldBe testSamples[i]

        textureA shouldBe textureB

        val textureC = Texture1d(textureA, 1, 1)
        val textureD = Texture1d(textureB, 1, 1)

        textureC shouldBe textureD
    }

    init {

        "load store" {
                       //
                       //
                       //
                       //
//                return Error
        }
    }
}