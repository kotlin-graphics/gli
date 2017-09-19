package gli_

import glm_.vec4.Vec4b
import java.nio.ByteBuffer


object vec4bData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4b(data, index * Vec4b.size)
    override fun set(index: Int, value: Any) = (value as Vec4b).to(data, index * Vec4b.size)
}

object byteData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4b(data, index * Vec4b.size)
    override fun set(index: Int, value: Any) = (value as Vec4b).to(data, index * Vec4b.size)
}

interface reinterpreter {
    operator fun get(index: Int): Any
    operator fun set(index: Int, value: Any): Any
}