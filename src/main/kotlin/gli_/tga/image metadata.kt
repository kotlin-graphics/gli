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

import org.w3c.dom.Node
import javax.imageio.ImageTypeSpecifier
import javax.imageio.metadata.IIOMetadata
import javax.imageio.metadata.IIOMetadataFormat
import javax.imageio.metadata.IIOMetadataFormatImpl
import javax.imageio.metadata.IIOMetadataNode

/**
 * <p>The image metadata for a TGA image type.  At this time there are no
 * elements in the format (i.e. {@link IIOMetadataFormat#canNodeAppear(String, javax.imageio.ImageTypeSpecifier)}
 * always returns <code>false</code>).</p>
 *
 * @author Rob Grzywinski <a href="mailto:rgrzywinski@realityinteractive.com">rgrzywinski@realityinteractive.com</a>
 * @version $Id: TGAImageMetadata.java,v 1.1 2005/04/12 11:23:53 ornedan Exp $
 * @since 1.0
 */
// NOTE:  this is currently unused
class TgaImageMetadata : IIOMetadata(
        TgaImageReaderSpi.SUPPORTS_STANDARD_IMAGE_METADATA_FORMAT,
        TgaImageReaderSpi.NATIVE_IMAGE_METADATA_FORMAT_NAME,
        TgaImageReaderSpi.NATIVE_IMAGE_METADATA_FORMAT_CLASSNAME,
        TgaImageReaderSpi.EXTRA_IMAGE_METADATA_FORMAT_NAMES,
        TgaImageReaderSpi.EXTRA_IMAGE_METADATA_FORMAT_CLASSNAMES) {

    /** Ensure that the specified format name is supported by this metadata.
     *  If the format is not supported {@link IllegalArgumentException} is thrown.
     * @param  formatName the name of the metadata format that is to be validated   */
    fun checkFormatName(formatName: String) {
        // if the format name is not known, throw an exception
        if (TgaImageReaderSpi.NATIVE_IMAGE_METADATA_FORMAT_NAME != formatName)
            throw IllegalArgumentException("Unknown image metadata format name \"$formatName\".") // FIXME:  localize
    }

    /** @see IIOMetadata#getAsTree(String) */
    override fun getAsTree(formatName: String): Node {
        // validate the format name (this will throw if invalid)
        checkFormatName(formatName)

        // create and return a root node, NOTE:  there are no children at this time
        return IIOMetadataNode(TgaImageReaderSpi.NATIVE_IMAGE_METADATA_FORMAT_NAME)
    }

    /** @see IIOMetadata#getMetadataFormat(String) */
    override fun getMetadataFormat(formatName: String): IIOMetadataFormat {
        // validate the format name (this will throw if invalid)
        checkFormatName(formatName)

        // return the metadata format
        return TgaImageMetadataFormat
    }

    /** This is read-only metadata.
     *  @see IIOMetadata#isReadOnly()     */
    override fun isReadOnly() = true // see javadoc

    /** @see IIOMetadata#mergeTree(String, Node) */
    override fun mergeTree(formatName: String, root: Node) =
    // validate the format name (this will throw if invalid)
            checkFormatName(formatName)
    // since there are no elements in the tree, there is nothing to merge

    /** NOTE:  nothing to do since there are no elements    */
    override fun reset() = Unit
}

/**
 * <p>The image metadata format for a TGA image type.  At this time there are
 * no elements in the format (i.e. {@link javax.imageio.metadata.IIOMetadataFormat#canNodeAppear(String, ImageTypeSpecifier)}
 * always returns <code>false</code>).</p>
 *
 * @author Rob Grzywinski <a href="mailto:rgrzywinski@realityinteractive.com">rgrzywinski@realityinteractive.com</a>
 * @version $Id: TGAImageMetadataFormat.java,v 1.1 2005/04/12 11:23:53 ornedan Exp $
 * @since 1.0
 */
// NOTE:  this is currently unused
object TgaImageMetadataFormat : IIOMetadataFormatImpl(
        // set the name of the root document node.  The child elements may repeat
        TgaImageReaderSpi.NATIVE_IMAGE_METADATA_FORMAT_NAME, CHILD_POLICY_REPEAT) {

    /** NOTE:  since there are no elements, none are allowed    */
    override fun canNodeAppear(elementName: String?, imageType: ImageTypeSpecifier?) = false
}