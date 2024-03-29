/**
 * Copyright (C) 2002-2015   The FreeCol Team
 * <p>
 * This file is part of FreeCol.
 * <p>
 * FreeCol is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * FreeCol is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.freecol.common.io.sza;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;


/**
 * An animation made from images stored in a zip-file.
 */
public final class SimpleZippedAnimation implements Iterable< AnimationEvent >
{

    private static final String ANIMATION_DESCRIPTOR_FILE = "animation.txt";

    private final List< AnimationEvent > events;
    private final int width;
    private final int height;

    /**
     * Creates a new animation from a stream generated by the
     * provided URL.
     *
     * @param url The URL to read a zip-file from. 
     * @throws IOException if the file cannot be opened, or
     *      is invalid.
     */
    public SimpleZippedAnimation( final URL url ) throws IOException
    {
        this( url.openStream( ) );
    }

    /**
     * Creates a new animation from a stream.
     *
     * @param zipStream An <code>InputStream</code> to a zip-file. 
     * @throws IOException if the file cannot be opened, or
     *      is invalid.
     */
    public SimpleZippedAnimation( final InputStream zipStream ) throws IOException
    {
        this( new ZipInputStream( zipStream ) );
    }

    private SimpleZippedAnimation( final List< AnimationEvent > events, final int width, final int height )
    {
        this.events = events;
        this.width = width;
        this.height = height;
    }

    private SimpleZippedAnimation( final ZipInputStream zipStream ) throws IOException
    {
        this.events = new ArrayList<>( );

        /*
         * Preload all files from the archive since we cannot
         * use a ZipFile for reading (as we should support an
         * arbitrary stream).
         */
        final Map< String, BufferedImage > loadingImages = new HashMap<>( );
        final List< String > loadingDescriptor = new LinkedList<>( );
        try
        {
            ZipEntry entry;
            while ( ( entry = zipStream.getNextEntry( ) ) != null )
            {
                if ( ANIMATION_DESCRIPTOR_FILE.equals( entry.getName( ) ) )
                {
                    final BufferedReader in = new BufferedReader( new InputStreamReader( zipStream, "UTF-8" ) );
                    String line;
                    while ( ( line = in.readLine( ) ) != null )
                    {
                        loadingDescriptor.add( line );
                    }
                }
                else
                {
                    loadingImages.put( entry.getName( ), ImageIO.read( zipStream ) );
                }
                zipStream.closeEntry( );
            }
        }
        finally
        {
            try
            {
                zipStream.close( );
            }
            catch ( IOException e ) {}
        }

        if ( loadingDescriptor.isEmpty( ) )
        {
            throw new IOException( "animation.txt is missing from the SZA." );
        }

        int largestWidth = 0;
        int largestHeight = 0;
        for ( String line : loadingDescriptor )
        {
            final int index = line.indexOf( '(' );
            final int index2 = line.indexOf( "ms)" );
            if ( index < 0 || index2 <= index )
            {
                throw new IOException( "animation.txt should use the format: FILNAME (TIMEms)" );
            }
            final String imageName = line.substring( 0, index ).trim( );
            final int durationInMs = Integer.parseInt( line.substring( index + 1, index2 ) );
            final BufferedImage image = loadingImages.get( imageName );
            if ( image == null )
            {
                throw new IOException( "Could not find referenced image: " + imageName );
            }
            events.add( new ImageAnimationEventImpl( image, durationInMs ) );
            if ( image.getWidth( ) > largestWidth )
            {
                largestWidth = image.getWidth( );
            }
            if ( image.getHeight( ) > largestHeight )
            {
                largestHeight = image.getHeight( );
            }
        }
        this.width = largestWidth;
        this.height = largestHeight;
    }

    /**
     * Gets the width of the animation.
     * @return The largest width of all the frames in
     *      this animation.
     */
    public int getWidth( )
    {
        return width;
    }

    /**
     * Gets the height of the animation.
     * @return The largest height of all the frames in
     *      this animation.
     */
    public int getHeight( )
    {
        return height;
    }

    /**
     * Returns all of the animation events.
     * @return An <code>Iterator</code> with all the images
     *      and other resources (support for sound may be
     *      added later).
     */
    @Override
    public Iterator< AnimationEvent > iterator( )
    {
        return Collections.unmodifiableList( events ).iterator( );
    }

    /**
     * Creates a scaled animation based on this object.
     *
     * @param scale The scaling factor (with 1 being normal size,
     *      2 twice the size, 0.5 half the size etc).
     * @return The scaled animation.
     */
    public SimpleZippedAnimation createScaledVersion( float scale )
    {
        final List< AnimationEvent > newEvents = new ArrayList<>( );
        for ( AnimationEvent event : events )
        {
            if ( event instanceof ImageAnimationEvent )
            {
                newEvents.add( ( ( ImageAnimationEventImpl ) event ).createScaledVersion( scale ) );
            }
            else
            {
                newEvents.add( event );
            }
        }
        return new SimpleZippedAnimation( newEvents, ( int ) ( width * scale ), ( int ) ( height * scale ) );
    }

    private static final class ImageAnimationEventImpl implements ImageAnimationEvent
    {
        private static final Component _c = new Component( ) { };

        private final Image image;
        private final int durationInMs;

        private ImageAnimationEventImpl( final Image image,
                                         final int durationInMs )
        {
            this.image = image;
            this.durationInMs = durationInMs;
        }

        @Override
        public Image getImage( )
        {
            return image;
        }

        @Override
        public int getDurationInMs( )
        {
            return durationInMs;
        }

        private ImageAnimationEvent createScaledVersion( float scale )
        {
            final int width = ( int ) ( image.getWidth( null ) * scale );
            final int height = ( int ) ( image.getHeight( null ) * scale );

            BufferedImage scaled = new BufferedImage( width, height,
                                                      BufferedImage.TYPE_INT_ARGB );
            Graphics2D g = scaled.createGraphics( );
            g.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BICUBIC );
            g.drawImage( image, 0, 0, width, height, null );
            g.dispose( );

            return new ImageAnimationEventImpl( scaled, durationInMs );
        }
    }
}
