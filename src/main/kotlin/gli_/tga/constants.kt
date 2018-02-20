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

/**
 * <p>Various header and such constants for the TGA image format.</p>
 *
 * @author Rob Grzywinski <a href="mailto:rgrzywinski@realityinteractive.com">rgrzywinski@realityinteractive.com</a>
 * @version $Id: TGAConstants.java,v 1.1 2005/04/12 11:23:53 ornedan Exp $
 * @since 1.0
 */
enum class ImageType(val i: Int) {
    /** An image type indicating no image data. */
    NO_IMAGE(0),
    /** An image type indicating an uncompressed color mapped (indexed) image.  */
    COLOR_MAP(1),
    /** An image type indicating an uncompressed true-color image.  */
    TRUE_COLOR(2),
    /** An image type indicating a black and white (monochrome) image.  */
    MONO(3),
    /** An image type indicating an RLE (run-length encoded) color-mapped (indexed) image.  */
    RLE_COLOR_MAP(9),
    /** An image type indicating an RLE (run-length encoded) true-color image.  */
    RLE_TRUE_COLOR(10),
    /** An image type indicating an RLE (run-length encoded) black and white (monochrome) image.    */
    RLE_MONO(11);

    companion object {
        infix fun of(i: Int) = values()[i]
    }
}

enum class ImageDescriptorBit(val i: Int) {
    /** The bit of the image descriptor field (5.5) indicating that the first pixel should be at the left or the right. */
    LEFT_RIGHT_BIT(0x10),
    /** The bit of the image descriptor field (5.5) indicating that the first pixel should be at the bottom or the top. */
    BOTTOM_TOP_BIT(0x20);

    companion object {
        infix fun of(i: Int) = values()[i]
    }
}

infix fun Int.has(b: ImageDescriptorBit) = and(b.i) != 0
infix fun Int.hasnt(b: ImageDescriptorBit) = and(b.i) == 0