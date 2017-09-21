package gli_

import glm_.BYTES
import glm_.vec1.Vec1b
import glm_.vec2.Vec2b
import glm_.vec3.Vec3
import glm_.vec3.Vec3b
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import java.nio.ByteBuffer

interface reinterpreter {
    operator fun get(index: Int): Any
    operator fun set(index: Int, value: Any): Any
}

object vec1bData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec1b(data, index * Vec1b.size)
    override fun set(index: Int, value: Any) = (value as Vec1b).to(data, index * Vec1b.size)
}

object vec2bData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec2b(data, index * Vec2b.size)
    override fun set(index: Int, value: Any) = (value as Vec2b).to(data, index * Vec2b.size)
}

object vec3bData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3b(data, index * Vec3b.size)
    override fun set(index: Int, value: Any) = (value as Vec3b).to(data, index * Vec3b.size)
}

object vec4bData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4b(data, index * Vec4b.size)
    override fun set(index: Int, value: Any) = (value as Vec4b).to(data, index * Vec4b.size)
}

object vec3Data : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3(data, index * Vec3.size)
    override fun set(index: Int, value: Any) = (value as Vec3).to(data, index * Vec3.size)
}

object vec4Data : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4(data, index * Vec4.size)
    override fun set(index: Int, value: Any) = (value as Vec4).to(data, index * Vec4.size)
}

object byteData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data[index]
    override fun set(index: Int, value: Any) = data.put(index, value as Byte)
}

object intData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data.getInt(index * Int.BYTES)
    override fun set(index: Int, value: Any) = data.putInt(index * Int.BYTES, value as Int)
}

object longData : reinterpreter {
    lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data.getLong(index * Long.BYTES)
    override fun set(index: Int, value: Any) = data.putLong(index * Long.BYTES, value as Long)
}