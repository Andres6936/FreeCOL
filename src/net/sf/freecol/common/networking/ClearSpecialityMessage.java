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

package net.sf.freecol.common.networking;

import net.sf.freecol.common.model.Game;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Unit;
import net.sf.freecol.server.FreeColServer;
import net.sf.freecol.server.model.ServerPlayer;

import org.w3c.dom.Element;


/**
 * The message sent when clearing a unit speciality.
 */
public class ClearSpecialityMessage extends DOMMessage
{

    /** The identifier of the unit to be cleared. */
    private final String unitId;


    /**
     * Create a new <code>ClearSpecialityMessage</code> with the
     * supplied unit.
     *
     * @param unit The <code>Unit</code> to clear.
     */
    public ClearSpecialityMessage( Unit unit )
    {
        super( getXMLElementTagName( ) );

        this.unitId = unit.getId( );
    }

    /**
     * Create a new <code>ClearSpecialityMessage</code> from a
     * supplied element.
     *
     * @param game The <code>Game</code> this message belongs to.
     * @param element The <code>Element</code> to use to create the message.
     */
    public ClearSpecialityMessage( Game game, Element element )
    {
        super( getXMLElementTagName( ) );

        this.unitId = element.getAttribute( "unit" );
    }


    /**
     * Handle a "clearSpeciality"-message.
     *
     * @param server The <code>FreeColServer</code> handling the message.
     * @param player The <code>Player</code> the message applies to.
     * @param connection The <code>Connection</code> message was received on.
     * @return An update containing the cleared unit, or an error
     *     <code>Element</code> on failure.
     */
    public Element handle( FreeColServer server, Player player,
                           Connection connection )
    {
        final ServerPlayer serverPlayer = server.getPlayer( connection );

        Unit unit;
        try
        {
            unit = player.getOurFreeColGameObject( unitId, Unit.class );
        }
        catch ( Exception e )
        {
            return DOMMessage.clientError( e.getMessage( ) );
        }

        // Try to clear.
        return server.getInGameController( )
                .clearSpeciality( serverPlayer, unit );
    }

    /**
     * Convert this ClearSpecialityMessage to XML.
     *
     * @return The XML representation of this message.
     */
    @Override
    public Element toXMLElement( )
    {
        return createMessage( getXMLElementTagName( ),
                              "unit", unitId );
    }

    /**
     * The tag name of the root element representing this object.
     *
     * @return "clearSpeciality".
     */
    public static String getXMLElementTagName( )
    {
        return "clearSpeciality";
    }
}
