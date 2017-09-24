package gli_

import glm_.BYTES
import glm_.vec1.Vec1
import glm_.vec1.Vec1b
import glm_.vec2.Vec2
import glm_.vec2.Vec2b
import glm_.vec3.Vec3
import glm_.vec3.Vec3b
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import java.nio.ByteBuffer

interface reinterpreter<T> {
    var data: ByteBuffer
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T): Any
}

object vec1bData : reinterpreter<Vec1b> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec1b(data, index * Vec1b.size)
    override fun set(index: Int, value: Vec1b) = value.to(data, index * Vec1b.size)
}

object vec2bData : reinterpreter<Vec2b> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec2b(data, index * Vec2b.size)
    override fun set(index: Int, value: Vec2b) = (value as Vec2b).to(data, index * Vec2b.size)
}

object vec3bData : reinterpreter<Vec3b> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3b(data, index * Vec3b.size)
    override fun set(index: Int, value: Vec3b) = (value as Vec3b).to(data, index * Vec3b.size)
}

object vec4bData : reinterpreter<Vec4b> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4b(data, index * Vec4b.size)
    override fun set(index: Int, value: Vec4b) = value.to(data, index * Vec4b.size)
}

object vec1Data : reinterpreter<Vec1> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec1(data, index * Vec1.size, false)
    override fun set(index: Int, value: Vec1) = value.to(data, index * Vec1.size)
}

object vec2Data : reinterpreter<Vec2> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec2(data, index * Vec2.size)
    override fun set(index: Int, value: Vec2) = value.to(data, index * Vec2.size)
}

object vec3Data : reinterpreter<Vec3> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3(data, index * Vec3.size)
    override fun set(index: Int, value: Vec3) = value.to(data, index * Vec3.size)
}

object vec4Data : reinterpreter<Vec4> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4(data, index * Vec4.size)
    override fun set(index: Int, value: Vec4) = value.to(data, index * Vec4.size)
}

object byteData : reinterpreter<Byte> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data[index]
    override fun set(index: Int, value: Byte) = data.put(index, value as Byte)
}

object intData : reinterpreter<Int> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data.getInt(index * Int.BYTES)
    override fun set(index: Int, value: Int) = data.putInt(index * Int.BYTES, value as Int)
}

object longData : reinterpreter<Long> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data.getLong(index * Long.BYTES)
    override fun set(index: Int, value: Long) = data.putLong(index * Long.BYTES, value as Long)
}