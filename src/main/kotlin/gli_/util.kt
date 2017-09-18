package gli_

import glm_.glm
import glm_.f
import glm_.i
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i

/**
 * Created by elect on 02/04/17.
 */

infix fun Int.has(b: Int) = (this and b) != 0
infix fun Int.hasnt(b: Int) = (this and b) == 0


// util function
inline fun wasInit(f: () -> Unit): Boolean {
    try {
        f()
    }
    catch(e: UninitializedPropertyAccessException) {
        return false
    }
    return true
}