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

package net.sf.freecol.client.gui.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

import net.sf.freecol.client.gui.ImageLibrary;


/**
 * Draw the "image.background.FreeColBrightPanel" resource as a tiled
 * background image.  BrightPanel is intended to provide a lighter
 * background than the default panel, and is used for panels that
 * might contain icons and text annotations, for example, many of the
 * subpanels in the ColonyPanel.
 */
public class FreeColBrightPanelUI extends BasicPanelUI
{

    private static final FreeColBrightPanelUI sharedInstance = new FreeColBrightPanelUI( );

    public static ComponentUI createUI( @SuppressWarnings( "unused" ) JComponent c )
    {
        return sharedInstance;
    }

    @Override
    public void paint( java.awt.Graphics g, javax.swing.JComponent c )
    {
        if ( c.isOpaque( ) )
        {
            ImageLibrary.drawTiledImage( "image.background.FreeColBrightPanel", g, c, null );
        }
    }

}
