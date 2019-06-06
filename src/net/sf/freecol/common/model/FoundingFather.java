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

package net.sf.freecol.common.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import net.sf.freecol.common.io.FreeColXMLReader;
import net.sf.freecol.common.io.FreeColXMLWriter;

import static net.sf.freecol.common.util.CollectionUtils.*;
import static net.sf.freecol.common.util.StringUtils.*;


/**
 * Represents one FoundingFather to be contained in a Player object.
 * The FoundingFather is able to grant new abilities or bonuses to the
 * player, or to cause certain events.
 */
public class FoundingFather extends FreeColGameObjectType
{

    public static enum FoundingFatherType
    {
        TRADE,
        EXPLORATION,
        MILITARY,
        POLITICAL,
        RELIGIOUS;

        /**
         * Get a message key for this type.
         *
         * @return A message key.
         */
        public String getKey( )
        {
            return getEnumKey( this );
        }
    }

    /** The type of this FoundingFather. */
    private FoundingFatherType type;

    /**
     * The probability of this FoundingFather being offered for selection,
     * across the game ages.
     */
    private final int[] weight = new int[ Specification.NUMBER_OF_AGES ];

    /**
     * Players that want to elect this Founding Father must match one
     * of these scopes.
     */
    private List< Scope > scopes = null;

    /** The events triggered by this Founding Father. */
    private List< Event > events = null;

    /** Holds the upgrades of Units caused by this FoundingFather. */
    private Map< UnitType, UnitType > upgrades = null;

    /** A list of AbstractUnits generated by this FoundingFather. */
    private List< AbstractUnit > units = null;


    /**
     * Create a new Founding Father.
     *
     * @param id The object identifier.
     * @param specification The <code>Specification</code> to refer to.
     */
    public FoundingFather( String id, Specification specification )
    {
        super( id, specification );
    }


    /**
     * Gets the type of this FoundingFather.
     *
     * @return The type of this FoundingFather.
     */
    public FoundingFatherType getType( )
    {
        return type;
    }

    /**
     * Set the type of this FoundingFather.
     *
     * Public for the test suite.
     *
     * @param type A new <code>FoundingFatherType</code>.
     */
    public void setType( FoundingFatherType type )
    {
        this.type = type;
    }

    /**
     * Get a key for the type of this FoundingFather.
     *
     * @return A type key.
     */
    public String getTypeKey( )
    {
        return getTypeKey( type );
    }

    /**
     * Get a message key for the type of a FoundingFather.
     *
     * @param type The <code>FoundingFatherType</code> to make a key for.
     * @return The message key.
     */
    public static String getTypeKey( FoundingFatherType type )
    {
        return "model.foundingFather." + type.getKey( );
    }

    /**
     * Get the weight of this FoundingFather.
     * This is used to select a random FoundingFather.
     *
     * @param age The age ([0, 2])
     * @return The weight of this father in the given age.
     */
    public int getWeight( int age )
    {
        return ( age >= 0 && age < weight.length ) ? weight[ age ] : 0;
    }

    /**
     * Get the events this father triggers.
     *
     * @return A list of <code>Event</code>s.
     */
    public final List< Event > getEvents( )
    {
        return ( events == null ) ? Collections.< Event >emptyList( )
                : events;
    }

    /**
     * Set the events this Founding Father triggers.
     *
     * Public for the test suite.
     *
     * @param newEvents The new events.
     */
    public final void setEvents( final List< Event > newEvents )
    {
        this.events = newEvents;
    }

    /**
     * Add an event.
     *
     * @param event The <code>Event</code> to add.
     */
    private void addEvent( Event event )
    {
        if ( events == null ) events = new ArrayList<>( );
        events.add( event );
    }

    /**
     * Get any scopes on the election of this father.
     *
     * @return A list of <code>Scope</code>s.
     */
    public final List< Scope > getScopes( )
    {
        return ( scopes == null ) ? Collections.< Scope >emptyList( )
                : scopes;
    }

    /**
     * Set the scopes on this Founding Father.
     *
     * Public for the test suite.
     *
     * @param newScopes The new scopes.
     */
    public final void setScopes( final List< Scope > newScopes )
    {
        this.scopes = newScopes;
    }

    /**
     * Add a scope.
     *
     * @param scope The <code>Scope</code> to add.
     */
    private void addScope( Scope scope )
    {
        if ( scopes == null ) scopes = new ArrayList<>( );
        scopes.add( scope );
    }

    /**
     * Get the upgrades triggered by this Founding Father.
     *
     * @return A map of old to new <code>UnitType</code>s.
     */
    public final Map< UnitType, UnitType > getUpgrades( )
    {
        return ( upgrades == null ) ? Collections.< UnitType, UnitType >emptyMap( )
                : upgrades;
    }

    /**
     * Set the upgrades triggered by this Founding Father.
     *
     * Public for the test suite.
     *
     * @param newUpgrades The new upgrades map.
     */
    public final void setUpgrades( final Map< UnitType, UnitType > newUpgrades )
    {
        this.upgrades = newUpgrades;
    }

    /**
     * Add an upgrade.
     *
     * @param fromType The initial <code>UnitType</code>.
     * @param toType The upgraded <code>UnitType</code>.
     */
    private void addUpgrade( UnitType fromType, UnitType toType )
    {
        if ( upgrades == null ) upgrades = new HashMap<>( );
        upgrades.put( fromType, toType );
    }

    /**
     * Get the units this father supplies.
     *
     * @return A list of <code>AbstractUnit</code>s.
     */
    public final List< AbstractUnit > getUnits( )
    {
        return ( units == null ) ? Collections.< AbstractUnit >emptyList( )
                : units;
    }

    /**
     * Set the units supplied by this Founding Father.
     *
     * Public for the test suite.
     *
     * @param newUnits The new units.
     */
    public final void setUnits( final List< AbstractUnit > newUnits )
    {
        this.units = newUnits;
    }

    /**
     * Add a unit.
     *
     * @param unit The <code>AbstractUnit</code> to add.
     */
    private void addUnit( AbstractUnit unit )
    {
        if ( units == null ) units = new ArrayList<>( );
        units.add( unit );
    }

    /**
     * Is this Founding Father available to the given player?
     *
     * Note that this does not cover restrictions due to the Age.
     *
     * @param player The <code>Player</code> to test.
     * @return True if the father is available.
     */
    public boolean isAvailableTo( Player player )
    {
        return ( ! player.isEuropean( ) ) ? false
                : ( scopes == null ) ? true
                : any( scopes, s -> s.appliesTo( player ) );
    }


    // Serialization

    private static final String FROM_ID_TAG = "from-id";
    private static final String TO_ID_TAG = "to-id";
    private static final String TYPE_TAG = "type";
    private static final String UNIT_TAG = "unit";
    private static final String UPGRADE_TAG = "upgrade";
    private static final String WEIGHT_TAG = "weight";


    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeAttributes( FreeColXMLWriter xw ) throws XMLStreamException
    {
        super.writeAttributes( xw );

        xw.writeAttribute( TYPE_TAG, type );

        for ( int i = 0; i < weight.length; i++ )
        {
            xw.writeAttribute( WEIGHT_TAG + ( i + 1 ), weight[ i ] );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeChildren( FreeColXMLWriter xw ) throws XMLStreamException
    {
        super.writeChildren( xw );

        for ( Event event : getEvents( ) ) event.toXML( xw );

        for ( Scope scope : getScopes( ) ) scope.toXML( xw );

        for ( AbstractUnit unit : getUnits( ) )
        {
            xw.writeStartElement( UNIT_TAG );

            xw.writeAttribute( ID_ATTRIBUTE_TAG, unit );

            xw.writeEndElement( );
        }

        if ( upgrades != null )
        {
            for ( Map.Entry< UnitType, UnitType > entry : upgrades.entrySet( ) )
            {
                xw.writeStartElement( UPGRADE_TAG );

                xw.writeAttribute( FROM_ID_TAG, entry.getKey( ).getId( ) );

                xw.writeAttribute( TO_ID_TAG, entry.getValue( ).getId( ) );

                xw.writeEndElement( );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readAttributes( FreeColXMLReader xr ) throws XMLStreamException
    {
        super.readAttributes( xr );

        type = xr.getAttribute( TYPE_TAG, FoundingFatherType.class,
                                null );

        for ( int i = 0; i < weight.length; i++ )
        {
            weight[ i ] = xr.getAttribute( WEIGHT_TAG + ( i + 1 ), 0 );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readChildren( FreeColXMLReader xr ) throws XMLStreamException
    {
        // Clear containers.
        if ( xr.shouldClearContainers( ) )
        {
            events = null;
            scopes = null;
            units = null;
            upgrades = null;
        }

        super.readChildren( xr );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readChild( FreeColXMLReader xr ) throws XMLStreamException
    {
        final Specification spec = getSpecification( );
        final String tag = xr.getLocalName( );

        if ( UPGRADE_TAG.equals( tag ) )
        {
            UnitType fromType = xr.getType( spec, FROM_ID_TAG, UnitType.class,
                                            ( UnitType ) null );
            UnitType toType = xr.getType( spec, TO_ID_TAG, UnitType.class,
                                          ( UnitType ) null );
            addUpgrade( fromType, toType );
            xr.closeTag( UPGRADE_TAG );

        }
        else if ( UNIT_TAG.equals( tag ) )
        {
            addUnit( new AbstractUnit( xr ) );

        }
        else if ( Event.getXMLElementTagName( ).equals( tag ) )
        {
            addEvent( new Event( xr, spec ) );

        }
        else if ( Scope.getXMLElementTagName( ).equals( tag ) )
        {
            addScope( new Scope( xr ) );

        }
        else
        {
            super.readChild( xr );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXMLTagName( ) { return getXMLElementTagName( ); }

    /**
     * Gets the tag name of the root element representing this object.
     *
     * @return "founding-father".
     */
    public static String getXMLElementTagName( )
    {
        return "founding-father";
    }
}
