package gli_

import glm_.vec2.Vec2i
import glm_.vec4.Vec4ub
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec

class copy : StringSpec() {

    init {

        "can copy level, all_levels" {

            fun make2d(format: Format, extent: Vec2i, clearColor: Vec4ub) = Texture2d(format, extent).apply { clear(clearColor) }

            // Can copy all levels from a source 2d texture to a destination texture of equivalent storage_linear shape

            val textureExpected = make2d(Format.RGBA8_UNORM_PACK8, Vec2i(8), Vec4ub(193, 127, 0, 255))

            // Scenario: Using gli::copy on a specialized texture 2d to copy all images at once
            run {
                val textureScenario = Texture2d(textureExpected.format, textureExpected.extent())
                textureExpected shouldNotBe textureScenario

                gli.copyLevel(textureExpected, 0, textureScenario, 0, textureScenario.levels())
                textureExpected shouldBe  textureScenario
            }

            // Scenario: Using gli::copy on a specialized texture 2d to copy all images one by one
//            {
//                gli::texture2d TextureScenario (textureExpected.format(), TextureExpected.extent())
//                textureExpected != TextureScenario ? 0 : 1
//
//                for (gli:: size_t LevelIndex = 0, LevelCount = TextureExpected.levels(); LevelIndex < LevelCount; ++LevelIndex)
//                gli::copy_level(TextureExpected, LevelIndex, TextureScenario, LevelIndex)
//                textureExpected == TextureScenario ? 0 : 1
//            }
//
//            // Scenario: Using gli::copy on a specialized texture 2d array
//            {
//                gli::texture2d_array TextureScenario (textureExpected.format(), TextureExpected.extent(), 1)
//                textureExpected != gli::texture2d(TextureScenario) ? 0 : 1
//
//                gli::copy_level(TextureExpected, 0, TextureScenario, 0, TextureScenario.levels())
//                textureExpected == gli::texture2d(TextureScenario) ? 0 : 1
//            }
//
//            // Scenario: Using gli::copy on a generic texture 2d to copy all images at once
//            {
//                gli::texture::extent_type const TextureExtent(textureExpected.extent(), 1)
//                gli::texture TextureScenario (textureExpected.target(), TextureExpected.format(), TextureExtent, 1, 1, gli::levels(TextureExtent))
//                textureExpected != TextureScenario ? 0 : 1
//
//                gli::copy_level(TextureExpected, 0, TextureScenario, 0, TextureExpected.levels())
//                textureExpected == TextureScenario ? 0 : 1
//            }
//
//            // Scenario: Using gli::copy on a generic texture 2d to copy all images one by one
//            {
//                gli::texture::extent_type const TextureExtent(textureExpected.extent(), 1)
//                gli::texture TextureScenario (textureExpected.target(), TextureExpected.format(), TextureExtent, 1, 1, gli::levels(TextureExtent))
//                textureExpected != TextureScenario ? 0 : 1
//
//                for (gli:: size_t LevelIndex = 0, LevelCount = TextureExpected.levels(); LevelIndex < LevelCount; ++LevelIndex)
//                gli::copy_level(TextureExpected, LevelIndex, TextureScenario, LevelIndex)
//                textureExpected == TextureScenario ? 0 : 1
//            }
//
//            // Scenario: Using member gli::texture::copy on a specialized texture 2d
//            {
//                gli::texture2d TextureScenario (textureExpected.format(), TextureExpected.extent())
//                textureExpected != TextureScenario ? 0 : 1
//
//                for (gli:: size_t Level = 0; Level < TextureScenario.levels(); ++Level)
//                TextureScenario.copy(textureExpected, 0, 0, Level, 0, 0, Level)
//                textureExpected == TextureScenario ? 0 : 1
//            }
//
//            // Scenario: Using member gli::texture::copy on a specialized texture 2d array
//            {
//                gli::texture2d_array TextureScenario (textureExpected.format(), TextureExpected.extent(), 1)
//                textureExpected != gli::texture2d(TextureScenario) ? 0 : 1
//
//                for (gli:: size_t Level = 0; Level < TextureScenario.levels(); ++Level)
//                TextureScenario.copy(textureExpected, 0, 0, Level, 0, 0, Level)
//                textureExpected == gli::texture2d(TextureScenario) ? 0 : 1
//            }
//
//            // Scenario: Using member gli::texture::copy on a generic texture 2d
//            {
//                gli::texture::extent_type const TextureExtent(textureExpected.extent(), 1)
//                gli::texture TextureScenario (textureExpected.target(), TextureExpected.format(), TextureExtent, 1, 1, gli::levels(TextureExtent))
//                textureExpected != TextureScenario ? 0 : 1
//
//                for (gli:: size_t Level = 0; Level < textureExpected.levels(); ++Level)
//                TextureScenario.copy(textureExpected, 0, 0, Level, 0, 0, Level)
//                textureExpected == TextureScenario ? 0 : 1
//            }
        }
    }
}