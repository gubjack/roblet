// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
class  SlotPacket
    extends Packet
{
    SlotPacket (Logger rLogger, int iCount)
    {
        mf_rLogger = rLogger;
        mf_iCount = iCount;
    }
    private final Logger  mf_rLogger;
    private final int  mf_iCount;
    private boolean  m_bAnswered;
    void  setSlot (int iSlot)
    {
        synchronized (this)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                    , "Set result for SlotPacket #" + mf_iCount + " to @" + iSlot);
            m_iSlot = iSlot;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "NOTIFY waiter for SlotPacket #"
                                                + mf_iCount);
            notify ();
        }
    }
    private int  m_iSlot;
    void  cancel (SlotException rSlotException)
    {
        synchronized (this)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "Cancel"
                            + " SlotPacket #" + mf_iCount
                            + " with " + rSlotException);
            m_rSlotException = rSlotException;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "NOTIFY waiter for"
                            + " SlotPacket #" + mf_iCount);
            notify ();
        }
    }
    private SlotException  m_rSlotException;
    int  getSlot ()
        throws InterruptedException, SlotException
    {
        synchronized (this)
        {
            while (! m_bAnswered)    // Schleife gegen Spurious-Wakeups
            {
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this
                            , "WAIT for SlotPacket result or exception"
                                                + " #" + mf_iCount + " ...");
                wait ();
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this
                            , "... finished waiting to SlotPacket"
                                + " result or exception"
                                + " #" + mf_iCount);
            }
        }

        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "SlotPacket" + " #" + mf_iCount + " results in "
                        + (m_rSlotException != null  ?  m_rSlotException
                                    :  "@" + m_iSlot));
        if (m_rSlotException != null)
            throw m_rSlotException;
        return m_iSlot;
    }
    void  send (Tube rTube)
        throws IOException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "WRITE SlotPacket #" + mf_iCount
                                            + " packet ...");
        rTube. writePacketSlot ();
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "... wrote SlotPacket #" + mf_iCount
                                            + " packet");
    }
    <T extends Packet>  boolean  isLike (Class<T> rClass, int iCount)
    {
        return  this. getClass () == rClass;
    }
    public String  toString ()
    {
        return "SlotPacket #" + mf_iCount + ": "
                    + (m_bAnswered  ?  "@" + m_iSlot  :  "unanswered"); 
    }
}
