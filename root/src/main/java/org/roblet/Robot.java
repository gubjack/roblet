// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * Provides a <B>context for a roblet</B> allowing access to the roblet server
 * controlled functionality.

<P>
    The accessible functionality can be obtained by calling
    {@link #getUnit(Class)}.
</P>

<P style="font-size:smaller">
    The name <em>robot</em> of this interface is for historical reason
    as the first implementation of a roblet server controlled an autonomous
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
     * Return an instance of an implementation of some {@link Unit} definition
     * as available in the underlying roblet server.

<BLOCKQUOTE>
    Refer to {@link Unit} regarding the terminology used.
</BLOCKQUOTE>

<P>
    Although unit definitions are interfaces they nevertheless result in classes
    to be provided here.
    So in case the instance of an implementation
    of some {@code MyUnit} interface is looked
    for then the parameter would be {@code MyUnit.class}.
</P>
<BLOCKQUOTE><PRE>
MyUnit  myUnit = robot. getUnit (MyUnit.class);
if (myUnit == null)
    // ...
// ...
</PRE></BLOCKQUOTE>

<P>
    The result can be {@code null} for the case that the roblet server cannot
    provide an instance.
    This should be tested or at least taken into account.
</P>

<P>
    There is no guarantee that two subsequent calls with the same
    unit definition will result in the same unit instance being returned.
</P>

     * @param  <U>  unit definition
     *              - an {@code interface} that extends {@link Unit}
     * @param  unit  interface {@code class} of the unit definition
     * @return  unit instance or {@code null} if unavailable
     * @see  Roblet
     */
    public <U extends Unit> U  getUnit (Class<U> unit);

}
