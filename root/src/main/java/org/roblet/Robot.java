// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * Provides a context for a roblet allowing access to the roblet server
 * controlled functionality.

<P>
    The accessible functionality can be obtained by calling
    {@link #getUnit(Class)}.
</P>

<P>
    The name <em>robot</em> of this interface is for historical reason
    as the first implementation of a roblet server controlled a autonomous
    mobil robot.
    Only the following years the concept has been generalized.
</P>

 * @see  #getUnit(Class)
 * @see  Roblet
 * @see  Unit
 * @author Hagen Stanek
 */
public interface  Robot
{

    /**
     * Return the implemenation of some {@link Unit} definition as available
     * in the underlying roblet server.

<P>
    Although unit definitions are interfaces they nevertheless result in classes
    to be provided here.
    So in case the implementation of some {@code MyUnit} interface is looked
    for then the parameter would be {@code MyUnit.class}.
</P>

<P>
    The result can be {@code null} for the case that the roblet server cannot
    provide the implementation.
    This should to be tested or at least taken into account.
</P>

<P>
    There is no guarantee that two subsequent calls with the same
    unit definition will result in the same unit implementation.
</P>

     * @param  definition  class of the unit interface definition
     * @return  unit implementation or {@code null} if unavailable
     * @see  Roblet
     */
    public Unit  getUnit (Class definition);

}
