package gli

import glm.f
import glm.vec._3.Vec3i
import glm.glm
import glm.i

/**
 * Created by elect on 02/04/17.
 */

infix fun Int.has(b: Int) = (this and b) != 0

fun levels(extent:Vec3i) = glm.log2(glm.compMax(extent).f).i