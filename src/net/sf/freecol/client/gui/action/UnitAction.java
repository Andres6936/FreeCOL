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

package net.sf.freecol.client.gui.action;

import net.sf.freecol.client.FreeColClient;


/**
 * Super class for all actions that should be disabled when no unit is
 * selected.
 */
public abstract class UnitAction extends MapboardAction
{


    /**
     * Creates a new <code>UnitAction</code>.
     *
     * @param freeColClient The <code>FreeColClient</code> for the game.
     * @param id The object identifier.
     */
    protected UnitAction( FreeColClient freeColClient, String id )
    {
        super( freeColClient, id );
    }


    // Override FreeColAction

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldBeEnabled( )
    {
        return super.shouldBeEnabled( )
                && getGUI( ).getActiveUnit( ) != null
                && getGUI( ).getActiveUnit( ).getOwner( )
                == getFreeColClient( ).getMyPlayer( );
    }
}
