// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
abstract class  Packet
{
    Packet  next;
    abstract void  send (Tube rTube)
        throws IOException;
    /**
     * @param rClass  derived packet type
     * @param iCount  number of packet in question
     */
    <T extends Packet>  boolean  isLike (Class<T> rClass, int iCount)
    {
        return false;
    }
    /**
     * @param rSlotException  cause for the cancellation
     */
    void  cancel (SlotException rSlotException)
    {
    }
}
