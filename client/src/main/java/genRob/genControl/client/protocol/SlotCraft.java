// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import org.roblet.client.Logger;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
class  SlotCraft
{

    SlotCraft (Logger rLogger, Queues rQueues)
    {
        mf_rLogger = rLogger;
        mf_rQueues = rQueues;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;


    private int  m_iCount_getSlot;
    SlotData  getSlot ()
        throws InterruptedException, SlotException
    {
        int  iCount;
        synchronized (mf_rQueues)
        {
            iCount = m_iCount_getSlot++;
        }
        if (mf_rLogger. call)
            mf_rLogger. call (this, "getSlot () - #" + iCount);

        SlotPacket  packet = new SlotPacket (mf_rLogger, iCount);
        mf_rQueues. append (packet);
        int  iSlot = packet. getSlot ();
        if (mf_rLogger. call)
            mf_rLogger. call (this
                    , "getSlot () - #" + iCount + ": @" + iSlot);

        return new SlotData (iSlot);
    }


    void  result (Tube.SlotResultData rSlotResultData)
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this
                    , "result (" + rSlotResultData + ")");
        SlotPacket  rSlotPacket = mf_rQueues. getWaitingPacket (
                                                        SlotPacket.class, 0);
        rSlotPacket. setSlot (rSlotResultData. mf_iSlot);
    }

}
