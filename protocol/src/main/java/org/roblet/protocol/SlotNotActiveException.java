// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.protocol;
 

/**
 * This kind of exception will be generated by a server in case
 * a client demands a function for a slot which is not active.
 
 <P>
    Not active means that the server does not serve the slot (anymore) for
    some reason.
</P>

<P>
    If such an exception reaches the client it may generate another of the
    same type to have a more suitable stacktrace.
</P>

 * @author Hagen Stanek
 */
public class  SlotNotActiveException
    extends RuntimeException
{

    /** Initialize an instance. */
    public  SlotNotActiveException ()
    {
        super ("Slot not active");
    }

}
