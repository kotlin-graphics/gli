package gli

import glm_.vec4.Vec4b
import java.nio.ByteBuffer

object DataPointer {

    lateinit var data: ByteBuffer
    var offset = 0

    operator fun set(texelOffset: Int, texel: Vec4b) = texel.to(data, offset + texelOffset * Vec4b.size)
}