package gli_

import glm_.f
import glm_.glm
import glm_.i
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i

interface levels {
    fun levels(extent: Vec3i) = glm.log2(glm.compMax(extent).f).i + 1
    fun levels(extent: Vec2i) = glm.log2(glm.compMax(extent).f).i + 1
    fun levels(extent: Vec1i) = glm.log2(glm.compMax(extent).f).i + 1
    fun levels(extent: Int) = glm.log2(extent.f).i + 1
}