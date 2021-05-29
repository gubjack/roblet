// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.client;


/**
 * Thrown in cases when a (de-)serialisation fails.
 * @see Slot
 * @author Hagen Stanek
 */
public class  MarshalException
    extends RuntimeException
{

    /**
     * Initialise with a message and causing exception.
     * @param message  text for the user
     * @param exception  causing exception
     */
    public  MarshalException (String message, Exception exception)
    {
        super (message, exception);
    }

}
