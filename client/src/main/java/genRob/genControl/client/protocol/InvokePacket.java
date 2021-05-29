// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
class  InvokePacket
    extends Packet
{
    InvokePacket (Logger rLogger, int iCount, int iSlot, byte[] abyteInvoke)
    {
        mf_rLogger = rLogger;
        mf_iCount = iCount;
        mf_iSlot = iSlot;
        mf_abyteInvoke = abyteInvoke;
    }
    private final Logger  mf_rLogger;
    final int  mf_iCount;
    private final int  mf_iSlot;
    private final byte[]  mf_abyteInvoke;
    private boolean  m_bAnswered;
    void  setResult (byte[] abyteObject)
    {
        synchronized (this)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "Set result to"
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot
                            + " with " + abyteObject.length + " bytes");
            m_abyteObject = abyteObject;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "NOTIFY waiter to"
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot);
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
                        , "Set exception to"
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot
                            + " with " + abyteException.length + " bytes");
            m_abyteException = abyteException;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "NOTIFY waiter to"
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot);
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
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot
                            + " with " + rSlotException);
            m_rSlotException = rSlotException;
            m_bAnswered = true;
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "NOTIFY waiter for"
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot);
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
                        , "WAIT for result or exception to"
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot
                            + " ...");
                wait ();
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this
                        , "... finished waiting result or exception to"
                            + " InvokePacket #" + mf_iCount
                            + " @" + mf_iSlot);
            }
        }

        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "Result or exception to"
                        + " InvokePacket #" + mf_iCount
                        + " @" + mf_iSlot
                        + " is "
                            + (m_rSlotException != null
                                ?  m_rSlotException
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
            mf_rLogger. transport (this
                    , "WRITE"
                        + " InvokePacket #" + mf_iCount
                        + " @" + mf_iSlot
                        + " to tube ...");
        rTube. writePacketInvoke (mf_iCount, mf_iSlot, mf_abyteInvoke);
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "... wrote"
                        + " InvokePacket #" + mf_iCount
                        + " @" + mf_iSlot
                        + " to tube");
    }
    <T extends Packet>  boolean  isLike (Class<T> rClass, int iCount)
    {
        return  this. getClass () == rClass
          &&    mf_iCount == iCount;
    }
    public String  toString ()
    {
        return "InvokePacket #" + mf_iCount
                + " @" + mf_iSlot
                + ": " + (m_bAnswered  ?  m_abyteObject.length + " bytes"
                                        :  "unanswered"); 
    }

}
