package gli_

import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import glm_.vec4.Vec4ub
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec

class copy_ : StringSpec() {

    init {

        "can copy level, all_levels" {

            fun make2d(format: Format, extent: Vec2i, clearColor: Vec4ub) = Texture2d(format, extent).apply { clear(clearColor) }

            // Can copy all levels from a source 2d texture to a destination texture of equivalent storage_linear shape

            val textureExpected = make2d(Format.RGBA8_UNORM_PACK8, Vec2i(8), Vec4ub(193, 127, 0, 255))

            // Scenario: Using gli.copy on a specialized texture 2d to copy all images at once
            run {
                val textureScenario = Texture2d(textureExpected.format, textureExpected.extent())
                textureExpected shouldNotBe textureScenario

                gli.copyLevel(textureExpected, 0, textureScenario, 0, textureScenario.levels())
                textureExpected shouldBe textureScenario
            }

            // Scenario: Using gli.copy on a specialized texture 2d to copy all images one by one
            run {
                val textureScenario = Texture2d(textureExpected.format, textureExpected.extent())
                textureExpected shouldNotBe textureScenario

                for (levelIndex in 0 until textureExpected.levels())
                    gli.copyLevel(textureExpected, levelIndex, textureScenario, levelIndex)
                textureExpected shouldBe textureScenario
            }

            // Scenario: Using gli.copy on a specialized texture 2d array
            run {
                val textureScenario = Texture2dArray(textureExpected.format, textureExpected.extent(), 1)
                textureExpected shouldNotBe Texture2d(textureScenario)

                gli.copyLevel(textureExpected, 0, textureScenario, 0, textureScenario.levels())
                textureExpected shouldBe Texture2d(textureScenario)
            }

            // Scenario: Using gli.copy on a generic texture 2d to copy all images at once
            run {
                val textureExtent = Vec3i(textureExpected.extent_(), 1)
                val textureScenario = Texture(textureExpected.target, textureExpected.format, textureExtent, 1, 1, gli.levels(textureExtent))
                textureExpected shouldNotBe textureScenario

                gli.copyLevel(textureExpected, 0, textureScenario, 0, textureExpected.levels())
                textureExpected shouldBe textureScenario
            }

            // Scenario: Using gli.copy on a generic texture 2d to copy all images one by one
            run {
                val textureExtent = Vec3i(textureExpected.extent_(), 1)
                val textureScenario = Texture(textureExpected.target, textureExpected.format, textureExtent, 1, 1, gli.levels(textureExtent))
                textureExpected shouldNotBe textureScenario

                for (levelIndex in 0 until textureExpected.levels())
                    gli.copyLevel(textureExpected, levelIndex, textureScenario, levelIndex)
                textureExpected shouldBe textureScenario
            }

            // Scenario: Using member gli.texture.copy on a specialized texture 2d
            run {
                val textureScenario = Texture2d(textureExpected.format, textureExpected.extent())
                textureExpected shouldNotBe textureScenario

                for (level in 0 until textureScenario.levels())
                    textureScenario.copy(textureExpected, 0, 0, level, 0, 0, level)
                textureExpected shouldBe textureScenario
            }

            // Scenario: Using member gli.texture.copy on a specialized texture 2d array
            run {
                val textureScenario = Texture2dArray(textureExpected.format, textureExpected.extent(), 1)
                textureExpected shouldNotBe Texture2d(textureScenario)

                for (level in 0 until textureScenario.levels())
                    textureScenario.copy(textureExpected, 0, 0, level, 0, 0, level)
                textureExpected shouldBe Texture2d(textureScenario)
            }

            // Scenario: Using member gli.texture.copy on a generic texture 2d
            run {
                val textureExtent = Vec3i(textureExpected.extent_(), 1)
                val textureScenario = Texture(textureExpected.target, textureExpected.format, textureExtent, 1, 1, gli.levels(textureExtent))
                textureExpected shouldNotBe textureScenario

                for (level in 0 until textureExpected.levels())
                    textureScenario.copy(textureExpected, 0, 0, level, 0, 0, level)
                textureExpected shouldBe textureScenario
            }
        }

        "can copy level, single level" {
            // Can copy a single level from a source texture to a destination texture of equivalent storage_linear shape

            val darkOrange = Vec4ub(193, 127, 0, 255)
            val lightOrange = Vec4ub(255, 191, 127, 255)

            val textureExpected = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(2), 1)
            textureExpected clear darkOrange
            for (layerIndex in 0 until textureExpected.layers())
                for (faceIndex in 0 until textureExpected.faces())
                    textureExpected.clear(layerIndex, faceIndex, 1, lightOrange)

            // Scenario: Using gli.copyLevel on a specialized texture cube array to copy all images one by one
            run {
                val textureScenario = TextureCubeArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                textureScenario clear darkOrange
                textureExpected shouldNotBe textureScenario

                gli.copyLevel(textureExpected, 1, textureScenario, 1)
                textureExpected shouldBe textureScenario
            }

            // Scenario: copying levels from storage_linear of different shape.
            run {
                val textureSource = TextureCubeArray(Format.RGBA8_UNORM_PACK8, textureExpected.extent_() ushr 1, textureExpected.layers(), 1)
                textureSource clear lightOrange

                val textureScenario = TextureCubeArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                textureScenario clear darkOrange
                textureExpected shouldNotBe textureScenario

                gli.copyLevel(textureSource, 0, textureScenario, 1)
                textureExpected shouldBe textureScenario
            }
        }

        "can copy level, texture targets" {

            fun make1dArray(format: Format, extent: Vec1i, layers: Int, clearColor: Vec4ub) =
                    Texture1dArray(format, extent, layers).apply { clear(clearColor) }

            fun make2dArray(format: Format, extent: Vec2i, layers: Int, clearColor: Vec4ub) =
                    Texture2dArray(format, extent, layers).apply { clear(clearColor) }

            fun makeCubeArray(format: Format, extent: Vec2i, layers: Int, clearColor: Vec4ub) =
                    TextureCubeArray(format, extent, layers).apply { clear(clearColor) }

            /*  Can copy all levels from various source texture target to a destination texture target of equivalent
                storage_linear shape             */

            // Texture 1d array
            make1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(8), 4, Vec4ub(193, 127, 0, 255)).let { textureExpected ->

                // Scenario: Using gli.copyLevel on a specialized texture 1d array to copy all images at once
                run {
                    val textureScenario = Texture1dArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                    textureExpected shouldNotBe textureScenario

                    gli.copyLevel(textureExpected, 0, textureScenario, 0, textureScenario.levels())
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Using gli.copy on a specialized texture 1d array to copy all images one by one
                run {
                    val textureScenario = Texture1dArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                    textureExpected shouldNotBe textureScenario

                    for (levelIndex in 0 until textureExpected.levels())
                        gli.copyLevel(textureExpected, levelIndex, textureScenario, levelIndex)
                    textureExpected shouldBe textureScenario
                }
            }

            // Texture 2d array
            make2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(8), 4, Vec4ub(193, 127, 0, 255)).let { texture2DArrayExpected ->

                // Scenario: Using gli.copy on a specialized texture 2d array to copy all images at once
                run {
                    val textureScenario = Texture2dArray(texture2DArrayExpected.format, texture2DArrayExpected.extent_(), texture2DArrayExpected.layers())
                    texture2DArrayExpected shouldNotBe textureScenario

                    gli.copyLevel(texture2DArrayExpected, 0, textureScenario, 0, textureScenario.levels())
                    texture2DArrayExpected shouldBe textureScenario
                }

                // Scenario: Using gli.copy on a specialized texture 2d array to copy all images one by one
                run {
                    val textureScenario = Texture2dArray(texture2DArrayExpected.format, texture2DArrayExpected.extent_(), texture2DArrayExpected.layers())
                    texture2DArrayExpected shouldNotBe textureScenario

                    for (levelIndex in 0 until texture2DArrayExpected.levels())
                        gli.copyLevel(texture2DArrayExpected, levelIndex, textureScenario, levelIndex)
                    texture2DArrayExpected shouldBe textureScenario
                }
            }

            // Texture cube array
            makeCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(8), 4, Vec4ub(193, 127, 0, 255)).let { textureCubeMapExpected ->

                // Scenario: Using gli.copy on a specialized texture cube array to copy all images at once
                run {
                    val textureScenario = TextureCubeArray(textureCubeMapExpected.format, textureCubeMapExpected.extent_(), textureCubeMapExpected.layers())
                    textureCubeMapExpected shouldNotBe textureScenario

                    gli.copyLevel(textureCubeMapExpected, 0, textureScenario, 0, textureScenario.levels())
                    textureCubeMapExpected shouldBe textureScenario
                }

                // Scenario: Using gli.copy on a specialized texture cube array to copy all images one by one
                run {
                    val textureScenario = TextureCubeArray(textureCubeMapExpected.format, textureCubeMapExpected.extent_(), textureCubeMapExpected.layers())
                    textureCubeMapExpected shouldNotBe textureScenario

                    for (levelIndex in 0 until textureCubeMapExpected.levels())
                        gli.copyLevel(textureCubeMapExpected, levelIndex, textureScenario, levelIndex)
                    textureCubeMapExpected shouldBe textureScenario
                }
            }
        }

        "can copy face, all faces" {

            fun makeCubeArray(format: Format, extent: Vec2i, layers: Int, clearColor: Vec4ub) =
                    TextureCubeArray(format, extent, layers).apply { clear(clearColor) }

            val textureExpected = makeCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(8), 4, Vec4ub(193, 127, 0, 255))

            // Scenario: Using gli.copyFace on a specialized texture cube array to copy all images at once
            run {
                val textureScenario = TextureCubeArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                textureExpected shouldNotBe textureScenario

                gli.copyFace(textureExpected, 0, textureScenario, 0, textureScenario.faces())
                textureExpected shouldBe textureScenario
            }

            // Scenario: Using gli.copyFace on a specialized texture cube array to copy all images one by one
            run {
                val textureScenario = TextureCubeArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                textureExpected shouldNotBe textureScenario

                for (faceIndex in 0 until textureExpected.faces())
                    gli.copyFace(textureExpected, faceIndex, textureScenario, faceIndex)
                textureExpected shouldBe textureScenario
            }
        }

        "can copy layer_all_layers" {

            fun makeCubeArray(format: Format, extent: Vec2i, layers: Int, clearColor: Vec4ub) =
                    TextureCubeArray(format, extent, layers).apply { clear(clearColor) }

            val textureCubeMapExpected = makeCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(8), 4, Vec4ub(193, 127, 0, 255))

            // Scenario: Using gli.copyLayer on a specialized texture cube array to copy all images at once
            run {
                val textureScenario = TextureCubeArray(textureCubeMapExpected.format, textureCubeMapExpected.extent_(), textureCubeMapExpected.layers())
                textureCubeMapExpected shouldNotBe textureScenario

                gli.copyLayer(textureCubeMapExpected, 0, textureScenario, 0, textureScenario.layers())
                textureCubeMapExpected shouldBe textureScenario
            }

            // Scenario: Using gli.copyLayer on a specialized texture cube array to copy all images one by one
            run {
                val textureScenario = TextureCubeArray(textureCubeMapExpected.format, textureCubeMapExpected.extent_(), textureCubeMapExpected.layers())
                textureCubeMapExpected shouldNotBe textureScenario

                for (layerIndex in 0 until textureCubeMapExpected.levels())
                    gli.copyLayer(textureCubeMapExpected, layerIndex, textureScenario, layerIndex)
                textureCubeMapExpected shouldBe textureScenario
            }
        }

        "can copy, full copy" {

            Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(8)).let { textureExpected ->

                textureExpected clear Vec4ub(193, 127, 0, 255)

                // Scenario: Full copy of an specialized texture 1d texture
                run {
                    val textureScenario = Texture1d(textureExpected.format, textureExpected.extent_())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Full copy of a generic texture 1d texture
                run {
                    val textureScenario = Texture(Target._1D, textureExpected.format, Vec3i(textureExpected.extent().x, 1, 1),
                            textureExpected.layers(), textureExpected.faces(), textureExpected.levels())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }
            }

            Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(8), 4).let { textureExpected ->

                textureExpected clear Vec4ub(193, 127, 0, 255)

                // Scenario: Full copy of an specialized texture 1d array texture
                run {
                    val textureScenario = Texture1dArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Full copy of a generic texture 1d array texture
                run {
                    val textureScenario = Texture(Target._1D_ARRAY, textureExpected.format, Vec3i(textureExpected.extent().x, 1, 1),
                            textureExpected.layers(), textureExpected.faces(), textureExpected.levels())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }
            }

            Texture2d (Format.RGBA8_UNORM_PACK8, Vec2i(8)).let { textureExpected ->

                textureExpected clear Vec4ub(193, 127, 0, 255)

                // Scenario: Full copy of an specialized texture 2d texture
                run {
                    val textureScenario =Texture2d(textureExpected.format, textureExpected.extent_())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Full copy of a generic texture 2d texture
                run {
                    val textureScenario = Texture(Target._2D, textureExpected.format, Vec3i(textureExpected.extent_(), 1),
                            textureExpected.layers(), textureExpected.faces(), textureExpected.levels())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }
            }

            Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(8), 4).let { textureExpected ->

                textureExpected clear Vec4ub(193, 127, 0, 255)

                // Scenario: Full copy of an specialized texture 2d array texture
                run {
                    val textureScenario =Texture2dArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Full copy of a generic texture 2d array texture
                run {
                    val textureScenario =Texture(Target._2D_ARRAY, textureExpected.format, Vec3i(textureExpected.extent_(), 1),
                            textureExpected.layers(), textureExpected.faces(), textureExpected.levels())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }
            }

            Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(8)).let { textureExpected ->

                textureExpected clear Vec4ub(193, 127, 0, 255)

                // Scenario: Full copy of an specialized texture 3d texture
                run {
                    val textureScenario =Texture3d(textureExpected.format, textureExpected.extent())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Full copy of a generic texture 3d texture
                run {
                    val textureScenario = Texture(Target._3D, textureExpected.format, textureExpected.extent(),
                            textureExpected.layers(), textureExpected.faces(), textureExpected.levels())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }
            }

            TextureCube (Format.RGBA8_UNORM_PACK8, Vec2i(8)).let { textureExpected ->

                textureExpected clear Vec4ub(193, 127, 0, 255)

                // Scenario: Full copy of an specialized texture cube texture
                run {
                    val textureScenario =TextureCube(textureExpected.format, textureExpected.extent_())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Full copy of a generic texture cube texture
                run {
                    val textureScenario =Texture(Target.CUBE, textureExpected.format, Vec3i(textureExpected.extent_(), 1),
                            textureExpected.layers(), textureExpected.faces(), textureExpected.levels())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }
            }

            TextureCubeArray (Format.RGBA8_UNORM_PACK8, Vec2i(8), 4).let { textureExpected ->

                textureExpected clear Vec4ub(193, 127, 0, 255)

                // Scenario: Full copy of an specialized texture cube array texture
                run {
                    val textureScenario =TextureCubeArray(textureExpected.format, textureExpected.extent_(), textureExpected.layers())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }

                // Scenario: Full copy of a generic texture cube array texture
                run {
                    val textureScenario =Texture(Target.CUBE_ARRAY, textureExpected.format, Vec3i(textureExpected.extent_(), 1),
                            textureExpected.layers(), textureExpected.faces(), textureExpected.levels())
                    textureExpected shouldNotBe textureScenario

                    gli.copy(textureExpected, textureScenario)
                    textureExpected shouldBe textureScenario
                }
            }
        }
    }
}