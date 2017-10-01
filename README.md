![gli](/src/main/resources/logo-mini.png)

This is the Kotlin port of the original [OpenGL Image](http://gli.g-truc.net/) (*GLI*), written by [g-truc](https://github.com/Groovounet) ([repository](https://github.com/g-truc/gli)), a header only C++ image library for graphics software.

*GLI* provides classes and functions to load image files (*[KTX](https://www.khronos.org/opengles/sdk/tools/KTX/)* and *[DDS](https://msdn.microsoft.com/en-us/library/windows/desktop/bb943990%28v=vs.85%29.aspx)*), facilitate graphics APIs texture creation, compare textures, access texture texels, sample textures, convert textures, generate mipmaps, etc.

This library works perfectly with *[OpenGL](https://www.opengl.org)* or *[Vulkan](https://www.khronos.org/vulkan)* but it also ensures interoperability with other third party libraries and SDK.
It is a good candidate for software rendering (raytracing / rasterization), image processing, image based software testing or any development context that requires a simple and convenient image library.

[comment]: <> (For more information about *GLI*, please have a look at the [manual](manual.md\) and the [API reference documentation](http://gli.g-truc.net/0.8.0/api/index.html\).)
[comment]: <> (The source code and the documentation are licensed under the [Happy Bunny License (Modified MIT\) or the MIT License](manual.md#section0\).)

Don't hesitate to contribute to the project by submitting [issues](https://github.com/kotlin-graphics/gli/issues) or [pull requests](https://github.com/kotlin-graphics/gli/pulls) for bugs and features. Any feedback is welcome at [elect86@gmail.com](mailto://elect86@gmail.com).


```kotlin
import gli_.gli

fun createTexture(filename: String) {

	val texture = gli.load(filename)
	if(texture.empty())
		return 0

	gl.profile = gl.Profile.GL33
	val format = gl.translate(texture.format, texture.swizzles)
	val target = GL.translate(Texture.target());
	assert(gli::is_compressed(Texture.format()) && Target == gli::TARGET_2D);

	GLuint TextureName = 0;
	glGenTextures(1, &TextureName);
	glBindTexture(Target, TextureName);
	glTexParameteri(Target, GL_TEXTURE_BASE_LEVEL, 0);
	glTexParameteri(Target, GL_TEXTURE_MAX_LEVEL, static_cast<GLint>(Texture.levels() - 1));
	glTexParameteriv(Target, GL_TEXTURE_SWIZZLE_RGBA, &Format.Swizzles[0]);
	glTexStorage2D(Target, static_cast<GLint>(Texture.levels()), Format.Internal, Extent.x, Extent.y);
	for(std::size_t Level = 0; Level < Texture.levels(); ++Level)
	{
		glm::tvec3<GLsizei> Extent(Texture.extent(Level));
		glCompressedTexSubImage2D(
			Target, static_cast<GLint>(Level), 0, 0, Extent.x, Extent.y,
			Format.Internal, static_cast<GLsizei>(Texture.size(Level)), Texture.data(0, 0, Level));
	}

	return TextureName;
}
```
