// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.client;


/**
 * Indicates that a slot is not valid anymore.
 * @see Slot
 * @author Hagen Stanek
 */
public class  SlotException
    extends RuntimeException
{

    /**
     * Initialise with a message.
     * @param message  text for the user
     */
    public  SlotException (String message)
    {
        super (message);
    }

}
