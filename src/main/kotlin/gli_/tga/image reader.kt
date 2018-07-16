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

import gli_.hasnt
import glm_.L
import glm_.i
import unsigned.toUInt
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.DataBuffer
import java.awt.image.DataBufferInt
import java.io.IOException
import java.nio.ByteOrder
import java.util.*
import javax.imageio.ImageReadParam
import javax.imageio.ImageReader
import javax.imageio.ImageTypeSpecifier
import javax.imageio.metadata.IIOMetadata
import javax.imageio.spi.ImageReaderSpi
import javax.imageio.stream.ImageInputStream


class TgaImageReaderSpi : ImageReaderSpi(
        VENDOR_NAME, VERSION, FORMAT_NAMES, SUFFIXES, MIME_TYPES,
        READER_CLASSNAME,
        arrayOf(ImageInputStream::class.java),
        WRITER_SPI_CLASSNAMES,
        SUPPORTS_STANDARD_STREAM_METADATA_FORMAT,
        NATIVE_STREAM_METADATA_FORMAT_NAME,
        NATIVE_STREAM_METADATA_FORMAT_CLASSNAME,
        EXTRA_STREAM_METADATA_FORMAT_NAMES,
        EXTRA_STREAM_METADATA_FORMAT_CLASSNAMES,
        SUPPORTS_STANDARD_IMAGE_METADATA_FORMAT,
        NATIVE_IMAGE_METADATA_FORMAT_NAME,
        NATIVE_IMAGE_METADATA_FORMAT_CLASSNAME,
        EXTRA_IMAGE_METADATA_FORMAT_NAMES,
        EXTRA_IMAGE_METADATA_FORMAT_CLASSNAMES
) {

    // NOTE:  these should be package (default protected) as it is used frequently elsewhere

    companion object {

        /** The vendor name:  Reality Interactive, Inc. */
        val VENDOR_NAME = "Reality Interactive, Inc."
        /** The plugin version number.  */
        val VERSION = "1.00"
        /** The class name for the TGA image reader.    */
        val READER_CLASSNAME = "gli_.tga.TGAImageReader"
        /** The format names.   */
        val FORMAT_NAMES = arrayOf("tga", "targa")
        /** The canonical suffix names. */
        val SUFFIXES = arrayOf("tga", "targa")
        /** The supported mime types.   */
        val MIME_TYPES = arrayOf("application/tga", "application/x-tga", "application/x-targa", "image/tga", "image/x-tga", "image/targa", "image/x-targa")
        /** This is a read-only TGA plugin. */
        val WRITER_SPI_CLASSNAMES = arrayOf<String>()
        /** The standard stream metadata format is not supported.   */
        val SUPPORTS_STANDARD_STREAM_METADATA_FORMAT = false
        /** There is no "native" stream metadata formats supported by the TGA plugin.   */
        val NATIVE_STREAM_METADATA_FORMAT_NAME = null
        /** There is no "native" stream metadata formats supported by the TGA plugin.   */
        val NATIVE_STREAM_METADATA_FORMAT_CLASSNAME = null
        /** There are no stream metadata formats other than the standard. */
        val EXTRA_STREAM_METADATA_FORMAT_NAMES = null
        /** There are no stream metadata formats other than the standard.   */
        val EXTRA_STREAM_METADATA_FORMAT_CLASSNAMES = null
        /** The standard image metadata format is not supported.    */
        val SUPPORTS_STANDARD_IMAGE_METADATA_FORMAT = false
        /** There are no "native" image metadata formats supported by the TGA plugin.   */
        val NATIVE_IMAGE_METADATA_FORMAT_NAME = null
        /** There are no "native" image metadata formats supported by the TGA plugin.   */
        val NATIVE_IMAGE_METADATA_FORMAT_CLASSNAME = null
        /** There are no image metadata formats supported other than the standard.  */
        val EXTRA_IMAGE_METADATA_FORMAT_NAMES = null
        /** There are no image metadata formats supported other than the standard.  */
        val EXTRA_IMAGE_METADATA_FORMAT_CLASSNAMES = null

    }

    /** @see ImageReaderSpi#canDecodeInput(Object)  */
    override fun canDecodeInput(source: Any?): Boolean {
        // NOTE:  the input source must be left in the same state as it started at (mark() and reset() should be used on ImageInputStream)

        // ensure that the input type is a ImageInputStream as that is all that is supported
        if (source !is ImageInputStream) return false

        try {
            // set a mark at the current position so that the stream can be reset
            source.mark()

            /*  there's no ideidentifiable header on a TGA file so a punt must occur. This will attempt to read
                the image type and if it is not known or allowed then false is returned.
                NOTE:  1.0.0 only supports un/compressed true color */
            source.readUnsignedByte() // idLength

            val colourMapType = source.readUnsignedByte()
            if (colourMapType != 0 && colourMapType != 1) return false

            val imageType = ImageType of source.readUnsignedByte()

            source.skipBytes(4)
            val colourMapBits = source.readUnsignedByte() // Offset 7

            // Defined as being 15, 16, 24 or 32 but I saw 0 in reality.
            if (colourMapBits != 0 && colourMapBits != 15 && colourMapBits != 16 && colourMapBits != 24 && colourMapBits != 32) return false

            source.skipBytes(8)
            val bits = source.readUnsignedByte() // Offset 16
            if (bits != 8 && bits != 16 && bits != 24 && bits != 32) return false

            /* else -- it's *possible* (though not known) that this is a TGA */

            return true
        } finally {
            source.reset()  // reset so that the ImageInputStream is put back where it was
        }
    }

    /** @see ImageReaderSpi#createReaderInstance(Object)    */
    override fun createReaderInstance(extension: Any?) = TGAImageReader(this)

    /** @see javax.imageio.spi.IIOServiceProvider#getDescription(Locale)    */
    override fun getDescription(locale: Locale?) = "TGA" // FIXME:  localize
}

// TODO:  incorporate the x and y origins
class TGAImageReader(originatingProvider: ImageReaderSpi) : ImageReader(originatingProvider) {

    /** The {@link ImageInputStream} from which the TGA is read.  This may be <code>null</code> if
     *  {@link ImageReader#setInput(Object)} (or the other forms of <code>setInput()</code>) has not been called.
     *  The stream will be set litle-endian when it is set. */
    var inputStream: ImageInputStream? = null

    /** The {@link TGAHeader}.  If <code>null</code> then the header has not been read since <code>inputStream</code>
     *  was last set.  This is created lazily.  */
    private var header: Header? = null

    /** Store the input if it is an {@link ImageInputStream}.
     *  Otherwise {@link IllegalArgumentException} is thrown.  The stream is set to little-endian byte ordering.
     * @see ImageReader#setInput(Object, boolean, boolean)     */
    // NOTE:  can't read the header in here as there would be no place for exceptions to go.  It must be read lazily.
    override fun setInput(input: Any?, seekForwardOnly: Boolean, ignoreMetadata: Boolean) {
        // delegate to the partent
        super.setInput(input, seekForwardOnly, ignoreMetadata)

        // if the input is null clear the inputStream and header
        if (input == null) {
            inputStream = null
            header = null
        } /* else -- the input is non-null */

        // only ImageInputStream are allowed.  If other throw IllegalArgumentException
        if (input is ImageInputStream) {
            // set the inputStream
            inputStream = input
            // put the ImageInputStream into little-endian ("Intel byte ordering") byte ordering
            input.byteOrder = ByteOrder.LITTLE_ENDIAN
        } else throw IllegalArgumentException("Only ImageInputStreams are accepted.")  // FIXME:  localize
    }

    /**
     * Create and read the {@link TGAHeader} only if there is not one already.</p>
     *
     * @return the <code>TGAHeader</code> (for convenience)
     * @throws IOException if there is an I/O error while reading the header
     */
    @Synchronized
    fun getHeader(): Header {
        // if there is already a header (non-null) then there is nothing to be done
        if (header != null) return header!!

        // ensure that there is an ImageInputStream from which the header is read
        if (inputStream == null)
            throw IllegalStateException("There is no ImageInputStream from which the header can be read.") // FIXME:  localize
        /* else -- there is an input stream */

        header = Header(inputStream!!)

        return header!!
    }

    /** Only a single image can be read by this reader.  Validate the specified image index and if not <code>0</code>
     *  then {@link IndexOutOfBoundsException} is thrown.
     *
     * @param  imageIndex the index of the image to validate
     * @throws IndexOutOfBoundsException if the <code>imageIndex</code> is not <code>0</code>
     */
    fun checkImageIndex(imageIndex: Int) {
        // if the imageIndex is not 0 then throw an exception
        if (imageIndex != 0)
            throw IndexOutOfBoundsException("Image index out of bounds ($imageIndex != 0).") // FIXME:  localize
    }

    // =========================================================================
    // Required ImageReader methods

    /** @see ImageReader#getImageTypes(int) */
    override fun getImageTypes(imageIndex: Int): Iterator<ImageTypeSpecifier> {
        // validate the imageIndex (this will throw if invalid)
        checkImageIndex(imageIndex)

        // read / get the header
        val header = getHeader()

        // get the ImageTypeSpecifier for the image type
        // FIXME:  finish
        val imageTypeSpecifier = when (header.imageType) {
            ImageType.COLOR_MAP, ImageType.RLE_COLOR_MAP, ImageType.TRUE_COLOR, ImageType.RLE_TRUE_COLOR -> {
                // determine if there is an alpha mask based on the number of samples per pixel
                val alphaMask =
                        if (header.samplesPerPixel == 4) 0xFF000000.i
                        else 0 // no alpha channel (less than 32 bits or 4 samples)

                // packed RGB(A) pixel data (more specifically (A)BGR)
                // TODO:  split on 16, 24, and 32 bit images otherwise there will be wasted space
                val rgb = ColorSpace.getInstance(ColorSpace.CS_sRGB)
                ImageTypeSpecifier.createPacked(rgb, 0x000000FF, 0x0000FF00, 0x00FF0000,
                        alphaMask, DataBuffer.TYPE_INT, false /*not pre-multiplied by an alpha*/)
            }
            ImageType.MONO, ImageType.RLE_MONO -> throw IllegalArgumentException("Monochrome image type not supported.")
            ImageType.NO_IMAGE -> throw IllegalArgumentException("The image type is not known.") // FIXME:  localize
        }

        // create a list and add the ImageTypeSpecifier to it and return
        return arrayListOf(imageTypeSpecifier).iterator()
    }

    /** Only a single image is supported.
     * @see ImageReader#getNumImages(boolean) */
    override fun getNumImages(allowSearch: Boolean) = 1 // see javadoc, NOTE:  1 is returned regardless if a search is allowed or not

    /** There is no stream metadata (i.e.  <code>null</code> is returned).
     * @see ImageReader#getStreamMetadata()     */
    override fun getStreamMetadata(): IIOMetadata? = null    // see javadoc

    /** There is no image metadata (i.e.  <code>null</code> is returned).
     * @see ImageReader#getImageMetadata(int)   */
    override fun getImageMetadata(imageIndex: Int): IIOMetadata? = null  // see javadoc

    /** @see ImageReader#getHeight(int) */
    override fun getHeight(imageIndex: Int): Int {
        // validate the imageIndex (this will throw if invalid)
        checkImageIndex(imageIndex)
        // get the header and return the height
        return getHeader().size.y
    }

    /** @see ImageReader#getWidth(int)  */
    override fun getWidth(imageIndex: Int): Int {
        // validate the imageIndex (this will throw if invalid)
        checkImageIndex(imageIndex)
        // get the header and return the width
        return getHeader().size.x
    }

    /** @see ImageReader#read(int, ImageReadParam) */
    override fun read(imageIndex: Int, param: ImageReadParam?): BufferedImage {
        // ensure that the image is of a supported type
        // NOTE:  this will implicitly ensure that the imageIndex is valid
        val imageTypes = getImageTypes(imageIndex)
        if (!imageTypes.hasNext()) throw IOException("Unsupported Image Type")

        // read and get the header
        val header = getHeader()

        // ensure that the ImageReadParam hasn't been set to other than the defaults (this will throw if not acceptible)
        checkImageReadParam(param, header)

        // get the height and width from the header for convenience
        val size = header.size

        // read the color map data.  If the image does not contain a color map then null will be returned.
        val colorMap = readColorMap(header)

        val input = inputStream!!
        // seek to the pixel data offset
        // TODO:  read the color map
        input.seek(header.pixelDataOffset.L)

        // get the destination image and WritableRaster for the image type and size
        val image = getDestination(param, imageTypes, size.x, size.y)
        val imageRaster = image.raster

        // get and validate the number of image bands
        val numberOfImageBands = image.sampleModel.numBands
        checkReadParamBandSettings(param, header.samplesPerPixel, numberOfImageBands)

        // get the destination bands
        val destinationBands = param?.destinationBands

        // create the destination WritableRaster
        val raster = imageRaster.createWritableChild(0, 0, size.x, size.y, 0, 0, destinationBands)

        // set up to read the data
        val intData = (raster.dataBuffer as DataBufferInt).data // CHECK:  is this valid / acceptible?
        var index: Int // the index in the intData array
        var runLength = 0 // the number of pixels in a run length
        var readPixel = true // if true then a raw pixel is read.  Used by the RLE.
        var isRaw = false // if true then the next pixels should be read.  Used by the RLE.
        var pixel = 0 // the current pixel data

        /*  TODO:  break out the case of 32 bit non-RLE as it can be read directly and 24 bit non-RLE as it can be read
            simply.  If subsampling and ROI's are implemented then selection must be done per pixel for RLE otherwise
            it's possible to miss the repetition count field.   */

        // TODO:  account for TGAHeader.firstColorMapEntryIndex

        // loop over the rows
        // TODO:  this should be destinationROI.height (right?)
        for (y in 0 until size.y) {
            // if the image is flipped top-to-bottom then set the index in intData appropriately
            index = if (header.bottomToTop) size.y - y - 1 else y
            //index = (height - y) - 1;

            /*  account for the width
                TODO:  this doesn't take into account the destination size or bands             */
            index *= size.x

            /*  loop over the columns
                TODO:  this should be destinationROI.width (right?)
                NOTE:  *if* destinations are used the RLE will break as this will cause the repetition count field to be missed.    */
            for (x in 0 until size.x) {
                /*  if the image is compressed (run length encoded) then determine if a pixel should be read or if
                    the current one should be used (using the current one is part of the RLE'ing).  */
                if (header.isCompressed) {
                    // if there is a non-zero run length then there are still compressed pixels
                    if (runLength > 0) {
                        /*  decrement the run length and flag that a pixel should not be read
                            NOTE:  a pixel is only read from the input if the packet was raw. If it was a run length
                            packet then the previous (current) pixel is used.   */
                        runLength--
                        readPixel = isRaw
                    } else /* non-positive run length */ {
                        // read the repetition count field
                        runLength = input.readByte().toUInt()

                        // determine which packet type:  raw or runlength
                        isRaw = runLength hasnt 0x80 // bit 7 == 0 -> raw; bit 7 == 1 -> runlength

                        // if a run length packet then shift to get the number
                        if (!isRaw)
                            runLength -= 0x80
                        /* else -- is raw so there's no need to shift */

                        // the next field is always read (it's the pixel data)
                        readPixel = true
                    }
                }

                // read the next pixel
                // NOTE:  only don't read when in a run length packet
                if (readPixel) {
                    // NOTE:  the alpha must hav a default value since it is not guaranteed to be present for each pixel read
                    val red: Int
                    val green: Int
                    val blue: Int
                    var alpha = 0xFF

                    // read based on the number of bits per pixel
                    when (header.bitsPerPixel) {
                    // 5-5-5 (RGB)
                        15, 16 -> {
                            // read the two bytes
                            val data = input.readShort().toUInt()

                            // get each color component -- each is 5 bits
                            red = ((data ushr 10) and 0x1F) shl 3
                            green = ((data ushr 5) and 0x1F) shl 3
                            blue = (data and 0x1F) shl 3

                            // combine each component into the result
                            pixel = red or (green shl 8) or (blue shl 16)
                        }
                    // true color RGB(A) (8 bits per pixel)
                        24, 32 -> {
                            // read each color component -- the alpha is only read if there are 32 bits per pixel
                            blue = input.readByte().toUInt()
                            green = input.readByte().toUInt()
                            red = input.readByte().toUInt()
                            if (header.bitsPerPixel == 32)
                                alpha = input.readByte().toUInt()
                            /* else -- 24 bits per pixel (i.e. no alpha) */

                            // combine each component into the result
                            pixel = red or (green shl 8) or (blue shl 16) or (alpha shl 24)
                        }
                    // grey scale (R = G = B)
                        else -> {
                            // read the data -- it is either the color map index or the color for each pixel
                            val data = input.readByte().toUInt()

                            /*  if the image is a color mapped image then the resulting pixel is pulled from
                                the color map, otherwise each pixel gets the data   */
                            pixel = when {
                            // the pixel is pulled from the color map CHECK:  do sanity bounds check?
                                header.hasColorMap -> colorMap!![data]
                                else -> {   // no color map
                                    // each color component is set to the color
                                    red = data
                                    green = data
                                    blue = data
                                    // combine each component into the result
                                    red or (green shl 8) or (blue shl 16)
                                }
                            }
                        }
                    }
                }

                // put the pixel in the data array
                intData[index] = pixel

                // advance to the next pixel
                // TODO:  the right-to-left switch
                index++
            }
        }

        return image
    }

    /** Reads and returns an array of color mapped values.  If the image does not contain a color map <code>null</code>
     *  will be returned</p>
     *
     * @param  header the <code>TGAHeader</code> for the image
     * @return the array of <code>int</code> color map values or <code>null</code> if the image does not contain a color map
     * @throws IOException if there is an I/O error while reading the color map     */
    fun readColorMap(header: Header): IntArray? {
        // determine if the image contains a color map.  If not, return null
        if (!header.hasColorMap) return null

        // seek to the start of the color map in the input stream
        inputStream!!.seek(header.colorMapDataOffset.L)

        // get the number of colros in the color map and the number of bits per color map entry
        val numberOfColors = header.colorMapLength
        val bitsPerEntry = header.bitsPerColorMapEntry

        // create the array that will contain the color map data
        // CHECK:  why is tge explicit +1 needed here ?!?
        val colorMap = IntArray(numberOfColors + 1)

        // read each color map entry
        for (i in 0 until numberOfColors) {

            val red: Int
            val green: Int
            val blue: Int

            val input = inputStream!!

            // read based on the number of bits per color map entry
            when (bitsPerEntry) {
            // 5-5-5 (RGB)
                15, 16 -> {
                    // read the two bytes
                    val data = input.readShort().toUInt()

                    // get each color component -- each is 5 bits
                    red = ((data ushr 10) and 0x1F) shl 3
                    green = ((data ushr 5) and 0x1F) shl 3
                    blue = (data and 0x1F) shl 3
                }
            // true color RGB(A) (8 bits per pixel)
                24, 32 -> {
                    // read each color component
                    // CHECK:  is there an alpha?!?
                    blue = input.readByte().toUInt()
                    green = input.readByte().toUInt()
                    red = input.readByte().toUInt()
                }
            // grey scale (R = G = B)
                else -> {
                    val data = input.readByte().toUInt()
                    blue = data
                    green = data
                    red = data
                }
            }
            // combine each component into the result
            colorMap[i] = red or (green shl 8) or (blue shl 16)
        }

        return colorMap
    }

    /** Validate that the specified {@link ImageReadParam} contains only the default values. If non-default values are
     *  present, {@link IOException} is thrown.</p>
     *
     * @param  param the <code>ImageReadParam</code> to be validated
     * @param  head the <code>TGAHeader</code> that contains information about the source image
     * @throws IOException if the <code>ImageReadParam</code> contains non-default values     */
    fun checkImageReadParam(param: ImageReadParam?, header: Header) {
        if (param != null) {
            // get the image height and width from the header for convenience
            val size = header.size

            // ensure that the param contains only the defaults
            val sourceROI = param.sourceRegion
            if (sourceROI != null && (sourceROI.x != 0 || sourceROI.y != 0 || sourceROI.width != size.x || sourceROI.height != size.y))
                throw IOException("The source region of interest is not the default.") // FIXME:  localize

            val destinationROI = param.sourceRegion
            if (destinationROI != null && (destinationROI.x != 0 || destinationROI.y != 0 || destinationROI.width != size.x || destinationROI.height != size.y))
                throw IOException("The destination region of interest is not the default.") // FIXME:  localize

            if (param.sourceXSubsampling != 1 || param.sourceYSubsampling != 1)
                throw IOException("Source sub-sampling is not supported.") // FIXME:  localize
        } /* else -- the ImageReadParam is null so the defaults *are* used */
    }
}