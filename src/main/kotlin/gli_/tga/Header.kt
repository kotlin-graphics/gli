/*  This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Copyright (c) 2003 Reality Interactive, Inc.
 */

package gli_.tga

import glm_.bool
import glm_.i
import glm_.vec2.Vec2i
import javax.imageio.stream.ImageInputStream

/**
 * <p>The header to a TGA image file.</p>
 *
 * <p>See <a href="http://organicbit.com/closecombat/formats/tga.html">format</a>,
 * <a href="http://www.opennet.ru/docs/formats/targa.pdf">format</a> or
 * <a href="http://netghost.narod.ru/gff/graphics/summary/tga.htm">format</a>
 * for header information.</p>
 *
 * @author Rob Grzywinski <a href="mailto:rgrzywinski@realityinteractive.com">rgrzywinski@realityinteractive.com</a>
 * @version $Id: TGAHeader.java,v 1.1 2005/04/12 11:23:53 ornedan Exp $
 * @since 1.0
 */
class Header
/**
 * <p>Reads and populates the header from the specifed {@link ImageInputStream}.
 * Any existing values will be over written.</p>
 *
 * <p>The <code>ImageInputStream</code> will be changed as a result of this operation (the offset will be moved).</p>
 *
 * @param  inputStream the <code>ImageInputStream</code> from which the header data is read
 * @throws IOException if there was an I/O error while reading the header data
 */
constructor(inputStream: ImageInputStream) {

    /** The length of the TGA identifier.  This is a <code>byte</code> in length.   */
    val idLength = inputStream.readUnsignedByte()
    /** The image identifier with length <code>idLength</code>. */
    val id = ByteArray(idLength).also { inputStream.read(it) }
    /** Does this TGA have an associated color map?
     *  <code>1</code> indicates that there is a color map.
     *  <code>0</code> indicates no color map.</p>  */
    var hasColorMap = inputStream.readUnsignedByte().bool
    /** The type of image.  See the image type constants in {@link ImageType} for allowed values.    */
    val imageType = ImageType of inputStream.readUnsignedByte()
    /** An image is compressed if its image type is {@link ImageType.RLE_COLOR_MAP}, {@link ImageType.RLE_TRUE_COLOR},
     *  or {@link ImageType.RLE_MONO}.  */
    val isCompressed = imageType == ImageType.RLE_COLOR_MAP || imageType == ImageType.RLE_TRUE_COLOR || imageType == ImageType.RLE_MONO
    /** The index of the first color map entry. */
    val firstColorMapEntryIndex = inputStream.readUnsignedShort()
    /** The total number of color map entries.  */
    val numberColorMapEntries = inputStream.readUnsignedShort()
    /** The number of bits per color map entry. */
    val bitsPerColorMapEntry = inputStream.readByte().i
    /** The coordinate for the lower-left corner of the image.   */
    val origin = Vec2i(inputStream.readUnsignedShort(), inputStream.readUnsignedShort())
    /** The size of the image in pixels.    */
    val size = Vec2i(inputStream.readUnsignedShort(), inputStream.readUnsignedShort())
    /** The number of bits per pixel.   */
    val bitsPerPixel = inputStream.readByte().i
    /** The number of attribute bits per pixel. */
    val imageDescriptor = inputStream.readByte().i
    /** The computed size of a color map entry in <code>byte</code>s.   */
    val colorMapEntrySize = when (bitsPerColorMapEntry) {
        15, 16 -> 2
        24, 32 -> 3
        else -> 1
    }
    /** The computed size of the color map field in <code>byte</code>s. This is determined from the
     *  <code>numberColorMapEntries</code> and <code>colorMapEntrySize</code>.  */
    val colorMapSize = colorMapEntrySize * numberColorMapEntries
    /** The horizontal ordering of the pixels as determined from the image descriptor. By default the order is left to
     *  right.  NOTE:  true -> left-to-right; false -> right-to-left    */
    val leftToRight = imageDescriptor hasnt ImageDescriptorBit.LEFT_RIGHT_BIT
    /** The horizontal ordering of the pixels as determined from the image descriptor. By default the order is left to
     *  right. NOTE:  true -> bottom-to-top; false -> top-to-bottom */
    val bottomToTop = imageDescriptor hasnt ImageDescriptorBit.BOTTOM_TOP_BIT
    /** The offset to the color map data.  This value is not defined if there is no color map (see <code>hasColorMap</code>).   */
    val colorMapDataOffset = inputStream.streamPosition.i
    /** The offset to the pixel data.   */
    val pixelDataOffset = if (hasColorMap) colorMapDataOffset + colorMapSize else inputStream.streamPosition.i


    /** Retrieves ttotal number of color map entries.   */
    val colorMapLength get() = numberColorMapEntries

    /** Retrieves a string that represents the image type.  */
    val imageTypeString
        get() = when (imageType) {
            ImageType.NO_IMAGE -> "NO IMAGE"
            ImageType.COLOR_MAP -> "COLOR MAP"
            ImageType.TRUE_COLOR -> "TRUE COLOR"
            ImageType.MONO -> "MONOCHROME"
            ImageType.RLE_COLOR_MAP -> "RLE COMPRESSED COLOR MAP"
            ImageType.RLE_TRUE_COLOR -> "RLE COMPRESSED TRUE COLOR"
            ImageType.RLE_MONO -> "RLE COMPRESSED MONOCHROME"
        }

    /** Retrieves the number of samples per pixel.  */
    val samplesPerPixel get() = if (bitsPerPixel == 32) 4 else 3 // FIXME:  this is overly simplistic but it is accurate

    /** Retrieves a string useful for debugging.    */
    val debugString get() = "TGAHeader[type=$imageTypeString, " +
            if (hasColorMap) "firstColorMapEntryIndex=$firstColorMapEntryIndex, numberColorMapEntries=$numberColorMapEntries, " +
                    "bitsPerColorMapEntry=$bitsPerColorMapEntry, totalColorMapEntrySize=$colorMapSize, "
            else "" +
                    "isCompressed=$isCompressed, origin=$origin, size=$size, bitsPerPixel=$bitsPerPixel, samplesPerPixel=$samplesPerPixel]"
}