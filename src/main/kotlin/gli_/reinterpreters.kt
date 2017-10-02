package gli_

import glm_.BYTES
import glm_.set
import glm_.vec1.*
import glm_.vec2.*
import glm_.vec3.*
import glm_.vec4.*
import java.nio.ByteBuffer
import kotlin.reflect.KClass

interface reinterpreter<T> {
    var data: ByteBuffer
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T): ByteBuffer
}

object vec1bData : reinterpreter<Vec1b> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec1b(data, index * Vec1b.size)
    override fun set(index: Int, value: Vec1b) = value.to(data, index * Vec1b.size)
}

object vec2bData : reinterpreter<Vec2b> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec2b(data, index * Vec2b.size)
    override fun set(index: Int, value: Vec2b) = value.to(data, index * Vec2b.size)
}

object vec3bData : reinterpreter<Vec3b> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3b(data, index * Vec3b.size)
    override fun set(index: Int, value: Vec3b) = value.to(data, index * Vec3b.size)
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
    override fun set(index: Int, value: Byte) = data.put(index, value)
}

object intData : reinterpreter<Int> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data.getInt(index * Int.BYTES)
    override fun set(index: Int, value: Int) = data.putInt(index * Int.BYTES, value)
}

object longData : reinterpreter<Long> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = data.getLong(index * Long.BYTES)
    override fun set(index: Int, value: Long) = data.putLong(index * Long.BYTES, value)
}

object vec1ubData : reinterpreter<Vec1ub> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec1ub(data, index * Vec1ub.size)
    override fun set(index: Int, value: Vec1ub) = value.to(data, index * Vec1ub.size)
}

object vec2ubData : reinterpreter<Vec2ub> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec2ub(data, index * Vec2ub.size)
    override fun set(index: Int, value: Vec2ub) = value.to(data, index * Vec2ub.size)
}

object vec3ubData : reinterpreter<Vec3ub> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3ub(data, index * Vec3ub.size)
    override fun set(index: Int, value: Vec3ub) = value.to(data, index * Vec3ub.size)
}

object vec4ubData : reinterpreter<Vec4ub> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4ub(data, index * Vec4ub.size)
    override fun set(index: Int, value: Vec4ub) = value.to(data, index * Vec4ub.size)
}

object vec1usData : reinterpreter<Vec1us> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec1us(data, index * Vec1us.size)
    override fun set(index: Int, value: Vec1us) = value.to(data, index * Vec1us.size)
}

object vec2usData : reinterpreter<Vec2us> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec2us(data, index * Vec2us.size)
    override fun set(index: Int, value: Vec2us) = value.to(data, index * Vec2us.size)
}

object vec3usData : reinterpreter<Vec3us> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3us(data, index * Vec3us.size)
    override fun set(index: Int, value: Vec3us) = value.to(data, index * Vec3us.size)
}

object vec4usData : reinterpreter<Vec4us> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4us(data, index * Vec4us.size)
    override fun set(index: Int, value: Vec4us) = value.to(data, index * Vec4us.size)
}

object vec1uiData : reinterpreter<Vec1ui> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec1ui(data, index * Vec1ui.size)
    override fun set(index: Int, value: Vec1ui) = value.to(data, index * Vec1ui.size)
}

object vec2uiData : reinterpreter<Vec2ui> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec2ui(data, index * Vec2ui.size)
    override fun set(index: Int, value: Vec2ui) = value.to(data, index * Vec2ui.size)
}

object vec3uiData : reinterpreter<Vec3ui> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec3ui(data, index * Vec3ui.size)
    override fun set(index: Int, value: Vec3ui) = value.to(data, index * Vec3ui.size)
}

object vec4uiData : reinterpreter<Vec4ui> {
    override lateinit var data: ByteBuffer
    override operator fun get(index: Int) = Vec4ui(data, index * Vec4ui.size)
    override fun set(index: Int, value: Vec4ui) = value.to(data, index * Vec4ui.size)
}

@Suppress("UNCHECKED_CAST")
fun <T> getReinterpreter(clazz: KClass<*>) = when (clazz) {
    Vec1b::class -> vec1bData
    Vec2b::class -> vec2bData
    Vec3b::class -> vec3bData
    Vec4b::class -> vec4bData
    Vec1ub::class -> vec1ubData
    Vec2ub::class -> vec2ubData
    Vec3ub::class -> vec3ubData
    Vec4ub::class -> vec4ubData
    Vec1us::class -> vec1usData
    Vec2us::class -> vec2usData
    Vec3us::class -> vec3usData
    Vec4us::class -> vec4usData
    Vec1ui::class -> vec1uiData
    Vec2ui::class -> vec2uiData
    Vec3ui::class -> vec3uiData
    Vec4ui::class -> vec4uiData
    Vec1::class -> vec1Data
    Vec2::class -> vec2Data
    Vec3::class -> vec3Data
    Vec4::class -> vec4Data
    java.lang.Byte::class -> byteData
    java.lang.Integer::class -> intData
    java.lang.Long::class -> longData
    else -> throw Error()
} as reinterpreter<T>

fun getSize(clazz: KClass<*>) = when (clazz) {
    java.lang.Byte::class -> Byte.BYTES
    java.lang.Integer::class -> Int.BYTES
    java.lang.Short::class -> Short.BYTES
    java.lang.Long::class -> Long.BYTES
    Vec1b::class -> Vec1b.size
    Vec2b::class -> Vec2b.size
    Vec3b::class -> Vec3b.size
    Vec4b::class -> Vec4b.size
    Vec1::class -> Vec1.size
    Vec2::class -> Vec2.size
    Vec3::class -> Vec3.size
    Vec3::class -> Vec3.size
    Vec4::class -> Vec4.size
    Vec1ub::class -> Vec1ub.size
    Vec2ub::class -> Vec2ub.size
    Vec3ub::class -> Vec3ub.size
    Vec4ub::class -> Vec4ub.size
    Vec1us::class -> Vec1us.size
    Vec2us::class -> Vec2us.size
    Vec3us::class -> Vec3us.size
    Vec4us::class -> Vec4us.size
    Vec1ui::class -> Vec1ui.size
    Vec2ui::class -> Vec2ui.size
    Vec3ui::class -> Vec3ui.size
    Vec4ui::class -> Vec4ui.size
    else -> println(clazz).run { throw Error() }
}

fun _clear(data: ByteBuffer, texel: Any) = when (texel) {
    is Byte -> for (i in 0 until data.capacity()) data[i] = texel
    is Short -> for (i in 0 until data.capacity() step Short.BYTES) data.putShort(i, texel)
    is Int -> for (i in 0 until data.capacity() step Int.BYTES) data.putInt(i, texel)
    is Long -> for (i in 0 until data.capacity() step Long.BYTES) data.putLong(i, texel)
    is Vec1b -> for (i in 0 until data.capacity()) data[i] = texel.x
    is Vec2b -> for (i in 0 until data.capacity() step Vec2b.size) {
        data[i] = texel.x
        data[i + Byte.BYTES] = texel.y
    }
    is Vec3b -> for (i in 0 until data.capacity() step Vec3b.size) {
        data[i] = texel.x
        data[i + Byte.BYTES] = texel.y
        data[i + Byte.BYTES * 2] = texel.z
    }
    is Vec4b -> for (i in 0 until data.capacity() step Vec4b.size) {
        data[i] = texel.x
        data[i + Byte.BYTES] = texel.y
        data[i + Byte.BYTES * 2] = texel.z
        data[i + Byte.BYTES * 3] = texel.w
    }
    is Vec1ub -> for (i in 0 until data.capacity()) data[i] = texel.x.v
    is Vec2ub -> for (i in 0 until data.capacity() step Vec2ub.size) {
        data[i] = texel.x.v
        data[i + Byte.BYTES] = texel.y.v
    }
    is Vec3ub -> for (i in 0 until data.capacity() step Vec3ub.size) {
        data[i] = texel.x.v
        data[i + Byte.BYTES] = texel.y.v
        data[i + Byte.BYTES * 2] = texel.z.v
    }
    is Vec4ub -> for (i in 0 until data.capacity() step Vec4ub.size) {
        data[i] = texel.x.v
        data[i + Byte.BYTES] = texel.y.v
        data[i + Byte.BYTES * 2] = texel.z.v
        data[i + Byte.BYTES * 3] = texel.w.v
    }
    is Vec1 -> for (i in 0 until data.capacity() step Vec1.size) data.putFloat(i, texel.x)
    is Vec2 -> for (i in 0 until data.capacity() step Vec2.size) {
        data.putFloat(i, texel.x)
        data.putFloat(i + Float.BYTES, texel.y)
    }
    is Vec3 -> for (i in 0 until data.capacity() step Vec3.size) {
        data.putFloat(i, texel.x)
        data.putFloat(i + Float.BYTES, texel.y)
        data.putFloat(i + Float.BYTES * 2, texel.z)
    }
    is Vec4 -> for (i in 0 until data.capacity() step Vec4.size) {
        data.putFloat(i, texel.x)
        data.putFloat(i + Float.BYTES, texel.y)
        data.putFloat(i + Float.BYTES * 2, texel.z)
        data.putFloat(i + Float.BYTES * 3, texel.w)
    }
    else -> throw Error()
}