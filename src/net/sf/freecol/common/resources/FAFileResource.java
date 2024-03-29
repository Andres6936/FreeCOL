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

package net.sf.freecol.common.resources;

import java.net.URI;
import java.net.URL;


/**
 * A <code>Resource</code> wrapping a <code>FAFile</code>.
 *
 * @see Resource
 * @see FAFile
 */
public class FAFileResource extends Resource
{

    private final FAFile FAFile;


    public FAFileResource( FAFile FAFile )
    {
        this.FAFile = FAFile;
    }


    /**
     * Do not use directly.
     *
     * @param resourceLocator The <code>URI</code> used when loading this
     *      resource.
     */
    public FAFileResource( URI resourceLocator ) throws Exception
    {
        super( resourceLocator );
        URL url = resourceLocator.toURL( );
        FAFile = new FAFile( url.openStream( ) );
    }


    /**
     * Gets the <code>FAFile</code> represented by this resource.
     *
     * @return The <code>FAFile</code> for this resource, or the default
     *     Java FAFile if none found.
     */
    public FAFile getFAFile( )
    {
        return FAFile;
    }
}
