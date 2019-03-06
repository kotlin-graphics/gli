![gli](/src/main/resources/logo-mini.png)

[![Build Status](https://travis-ci.org/kotlin-graphics/gli.svg?branch=master)](https://travis-ci.org/kotlin-graphics/gli) 
[![license](https://img.shields.io/badge/License-MIT-orange.svg)](https://github.com/kotlin-graphics/gli/blob/master/LICENSE) 
![](https://reposs.herokuapp.com/?path=kotlin-graphics/gli&color=yellow) 
[![Release](https://jitpack.io/v/kotlin-graphics/gli.svg)](https://jitpack.io/#kotlin-graphics/gli)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin) 
[![Slack Status](http://slack.kotlinlang.org/badge.svg)](http://slack.kotlinlang.org/)

This is the Kotlin port of the original [OpenGL Image](http://gli.g-truc.net/) (*GLI*), written by [g-truc](https://github.com/Groovounet) ([repository](https://github.com/g-truc/gli)), a header only C++ image library for graphics software.

*GLI* provides classes and functions to load image files (*[KTX](https://www.khronos.org/opengles/sdk/tools/KTX/)* and *[DDS](https://msdn.microsoft.com/en-us/library/windows/desktop/bb943990%28v=vs.85%29.aspx)*), facilitate graphics APIs texture creation, compare textures, access texture texels, sample textures, convert textures, generate mipmaps, etc.

This library works perfectly with *[OpenGL](https://www.opengl.org)* or *[Vulkan](https://www.khronos.org/vulkan)* but it also ensures interoperability with other third party libraries and SDK.
It is a good candidate for software rendering (raytracing / rasterization), image processing, image based software testing or any development context that requires a simple and convenient image library.

Don't hesitate to contribute to the project by submitting [issues](https://github.com/kotlin-graphics/gli/issues) or [pull requests](https://github.com/kotlin-graphics/gli/pulls) for bugs and features. Any feedback is welcome at [elect86@gmail.com](mailto://elect86@gmail.com).

Kotlin:
```kotlin
import gli_.gli

fun createTexture(filename: String): Int {

    val texture = gli.load(filename)
    if(texture.empty())
        return 0

    gli.gl.profile = gl.Profile.GL33
    val format = gli.gl.translate(texture.format, texture.swizzles)
    val target = gli.gl.translate(texture.target)
    assert(texture.format.isCompressed && target == gl.Target._2D)

    val textureName = intBufferBig(1)
    glGenTextures(textureName)
    glBindTexture(target.i, textureName[0])
    glTexParameteri(target.i, GL_TEXTURE_BASE_LEVEL, 0)
    glTexParameteri(target.i, GL_TEXTURE_MAX_LEVEL, texture.levels() - 1)
    val swizzles = intBufferBig(4)
    format.swizzles to swizzles
    glTexParameteriv(target.i, GL_TEXTURE_SWIZZLE_RGBA, swizzles)
    var extent = texture.extent()
    glTexStorage2D(target.i, texture.levels(), format.internal.i, extent.x, extent.y)
    for(level in 0 until texture.levels()) {
        extent = texture.extent(level)
        glCompressedTexSubImage2D(
                target.i, level, 0, 0, extent.x, extent.y,
                format.internal.i, texture.data(0, 0, level))
    }
    val texName = textureName[0]
    textureName.free()
    return texName
}
```

Kotlin with [gl-next](https://github.com/kotlin-graphics/gln):
```kotlin
    fun createTexture(filename: String): Int {

        val texture = gli.load(filename)
        if(texture.empty())
            return 0

        gli.gl.profile = gl.Profile.GL33
        val (target, format) = gli.gl.translate(texture)
        assert(texture.format.isCompressed && target == gl.Target._2D)

        return initTexture2d {
            levels = 0 until texture.levels()
            swizzles = format.swizzles
            storage(texture.levels(), format.internal, texture.extent())
            levels.forEach {
                compressedSubImage(it, texture.extent(it), format.internal, texture.data(0, 0, it))
            }
        }
    }
```

Java:
```java
public static int createTexture(String filename) {

    Texture texture = gli.load(filename);
    if (texture.empty())
        return 0;

    gli_.gli.gl.setProfile(gl.Profile.GL33);
    gl.Format format = gli_.gli.gl.translate(texture.getFormat(), texture.getSwizzles());
    gl.Target target = gli_.gli.gl.translate(texture.getTarget());
    assert (texture.getFormat().isCompressed() && target == gl.Target._2D);

    IntBuffer textureName = intBufferBig(1);
    glGenTextures(textureName);
    glBindTexture(target.getI(), textureName.get(0));
    glTexParameteri(target.getI(), GL12.GL_TEXTURE_BASE_LEVEL, 0);
    glTexParameteri(target.getI(), GL12.GL_TEXTURE_MAX_LEVEL, texture.levels() - 1);
    IntBuffer swizzles = intBufferBig(4);
    texture.getSwizzles().to(swizzles);
    glTexParameteriv(target.getI(), GL33.GL_TEXTURE_SWIZZLE_RGBA, swizzles);
    Vec3i extent = texture.extent(0);
    glTexStorage2D(target.getI(), texture.levels(), format.getInternal().getI(), extent.x, extent.y);
    for (int level = 0; level < texture.levels(); level++) {
        extent = texture.extent(level);
        glCompressedTexSubImage2D(
            target.getI(), level, 0, 0, extent.x, extent.y,
            format.getInternal().getI(), texture.data(0, 0, level));
    }
    int texName = textureName.get(0);
    MemoryUtil.memFree(textureName);
    return texName
}
```
