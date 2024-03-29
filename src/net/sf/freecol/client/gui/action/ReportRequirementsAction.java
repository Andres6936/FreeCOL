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

import java.awt.event.ActionEvent;

import net.sf.freecol.client.FreeColClient;


/**
 * An action for displaying a Requirements Report.
 */
public class ReportRequirementsAction extends FreeColAction
{

    public static final String id = "reportRequirementsAction";


    /**
     * Creates this action.
     *
     * @param freeColClient The <code>FreeColClient</code> for the game.
     */
    public ReportRequirementsAction( FreeColClient freeColClient )
    {
        super( freeColClient, id );
    }


    // Interface ActionListener

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed( ActionEvent ae )
    {
        getGUI( ).showReportRequirementsPanel( );
    }
}
