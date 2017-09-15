package gli

import glm_.vec4.Vec4b
import java.nio.ByteBuffer


object Vec4bData {
    lateinit var data: ByteBuffer
    operator fun get(index: Int) = Vec4b(data, index * Vec4b.size)
}