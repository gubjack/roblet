// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.client;


/**
 * This is thrown if calling a method of a remote instance fails for
 * any strange reason - other than marshalling problems or exceptions
 * declared by the method.
 * The reason is attached as {@code cause}.
 * 
 * @see genRob.genControl.client.Slot#obtainProxy(Class)
 * @author Hagen Stanek
 */
public class  UnexpectedException
    extends RuntimeException
{

    /**
     * Initialize an instance.
     * @param cause  the reason
     */
    public  UnexpectedException (Exception cause) {
        super (cause);
    }

}
