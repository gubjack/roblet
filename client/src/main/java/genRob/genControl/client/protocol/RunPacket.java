// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
class  RunPacket
    extends Packet
{
    RunPacket (Logger rLogger, int iCount, int iSlot, byte[] abyteRoblet)
    {
        mf_rLogger = rLogger;
        mf_iCount = iCount;
        mf_iSlot = iSlot;
        mf_abyteRoblet = abyteRoblet;
    }
    private final Logger  mf_rLogger;
    final int  mf_iCount;
    private boolean  m_bAnswered;
    private final int  mf_iSlot;
    private final byte[]  mf_abyteRoblet;
    void  setResult (byte[] abyteObject)
    {
        synchronized (this)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "Set result for RunPacket #" + mf_iCount
                            + " with " + abyteObject.length + " bytes");
            m_abyteObject = abyteObject;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "NOTIFY waiter for RunPacket #"
                                                + mf_iCount);
            notify ();
        }
    }
    private byte[]  m_abyteObject;
    void  setException (byte[] abyteException)
    {
        synchronized (this)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "Set exception for RunPacket #" + mf_iCount
                                + " with " + abyteException.length + " bytes");
            m_abyteException = abyteException;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "NOTIFY waiter for RunPacket #"
                                                + mf_iCount);
            notify ();
        }
    }
    private byte[]  m_abyteException;
    boolean  isException ()
    {
        return m_abyteException != null;
    }
    void  cancel (SlotException rSlotException)
    {
        synchronized (this)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "Cancel"
                            + " RunPacket #" + mf_iCount
                            + " with " + rSlotException);
            m_rSlotException = rSlotException;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "NOTIFY waiter for"
                            + " RunPacket #" + mf_iCount);
            notify ();
        }
    }
    private SlotException  m_rSlotException;
    byte[]  getResult ()
        throws InterruptedException, SlotException
    {
        synchronized (this)
        {
            while (! m_bAnswered)    // Schleife gegen Spurious-Wakeups
            {
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this
                            , "WAIT for RunPacket result or exception"
                                                + " #" + mf_iCount + " ...");
                wait ();
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this
                            , "... finished waiting for "
                                + "RunPacket result or exception"
                                + " #" + mf_iCount);
            }
        }

        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "RunPacket" + " #" + mf_iCount + " results in "
                        + (m_rSlotException != null  ?  m_rSlotException
                            :  (m_abyteException != null
                                    ?  m_abyteException.length
                                                            + " exception bytes"
                                    :  m_abyteObject.length + " bytes")));
        if (m_rSlotException != null)
            throw m_rSlotException;
        if (m_abyteException != null)
            return m_abyteException;
        return m_abyteObject;
    }
    void  send (Tube rTube)
        throws IOException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "WRITE RunPacket #" + mf_iCount
                                            + " packet ...");
        rTube. writePacketRun (mf_iCount, mf_iSlot, mf_abyteRoblet);
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "... wrote RunPacket #" + mf_iCount
                                            + " packet");
    }
    <T extends Packet>  boolean  isLike (Class<T> rClass, int iCount)
    {
        return  this. getClass () == rClass
          &&    mf_iCount == iCount;
    }
    public String  toString ()
    {
        return "RunPacket #" + mf_iCount + ": "
                    + (m_bAnswered  ?  m_abyteObject.length + " bytes"
                                    :  "unanswered");
    }
}
