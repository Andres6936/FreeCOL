/**
 * Copyright (C) 2002-2015  The FreeCol Team
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

package net.sf.freecol.server.generator;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests
{

    public static Test suite( )
    {
        TestSuite suite = new TestSuite( "Test for net.sf.freecol.server.generator" );
        //$JUnit-BEGIN$
        suite.addTestSuite( MapGeneratorTest.class );
        //$JUnit-END$
        return suite;
    }

}
