package gli

import glm.f
import glm.vec._3.Vec3i
import glm.glm
import glm.i
import glm.vec._1.Vec1i
import glm.vec._2.Vec2i
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.net.URI
import java.nio.channels.FileChannel

/**
 * Created by elect on 02/04/17.
 */

infix fun Int.has(b: Int) = (this and b) != 0

fun levels(extent: Vec3i) = glm.log2(glm.compMax(extent).f).i + 1
fun levels(extent: Vec2i) = glm.log2(glm.compMax(extent).f).i + 1
fun levels(extent: Vec1i) = glm.log2(glm.compMax(extent).f).i + 1
