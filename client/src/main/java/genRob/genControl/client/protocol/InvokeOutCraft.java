// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import java.io.IOException;

import org.roblet.client.Logger;
import org.roblet.client.MarshalException;
import org.roblet.client.SlotException;
import org.roblet.protocol.SlotNotActiveException;

import genRob.genControl.client.server.Invoke;


/**
 * @author Hagen Stanek
 */
class  InvokeOutCraft
{

    InvokeOutCraft (Logger rLogger, Queues rQueues)
    {
        mf_rLogger = rLogger;
        mf_rQueues = rQueues;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;


    Object  invoke (int iSlot, Invoke rInvoke)
        throws InterruptedException, MarshalException, SlotException
                , Exception
    {
        int  iCount;
        synchronized (mf_rQueues)
        {
            iCount = m_iCount_invoke++;
        }
        String  log = null;
        if (mf_rLogger. call)
        {
            log = "invoke (@" + iSlot + "," + rInvoke + ") - #" + iCount;
            mf_rLogger. call (this, "ENTER " + log);
        }

        try
        {
            return invoke (log, iCount, iSlot, rInvoke);
        }
        finally
        {
            if (mf_rLogger. call)
                mf_rLogger. call (this, "LEAVE " + log);
        }
    }
    private int  m_iCount_invoke;
    private Object  invoke (String log, int iCount, int iSlot, Invoke rInvoke)
        throws InterruptedException, MarshalException, SlotException
                , Exception
    {
        byte[]  abyteInvoke;
        try
        {
            abyteInvoke = Convert. object2bytes (rInvoke);
        }
        catch (IOException e)
        {
            MarshalException  me = new MarshalException (
                                "Failed to convert invocation into bytes", e);
            if (mf_rLogger. call)
                mf_rLogger. call (this, log + " ??? " + me);
            throw me;
        }

        InvokePacket  packet = new InvokePacket (mf_rLogger, iCount, iSlot
                                                , abyteInvoke);
        mf_rQueues. append (packet);

        byte[]  abyteObject = packet. getResult ();
        if (packet. isException ())
        {
            Exception  rException;
            try
            {
                rException = Convert. bytes2object (abyteObject);
            }
            catch (Exception e)
            {
                MarshalException  me = new MarshalException (
                                "Failed to convert bytes into exception", e);
                if (mf_rLogger. call)
                    mf_rLogger. call (this, log + " ??? " + me);
                throw me;
            }

            if (mf_rLogger. call)
                mf_rLogger. call (this, log + " --- " + rException);

            // Wenn Fach im Server schon verworfen wurde,
            // so wird das hochgereicht, allerdings mit einem neuen Stacktrace
            if (rException instanceof SlotNotActiveException)
                throw new SlotException ("Slot is not active");

            // Andere Ausnahmen werden einfach hochgereicht
            throw rException;
        }

        Object  rObject;
        try
        {
            rObject = Convert. bytes2object (abyteObject);
        }
        catch (Exception e)
        {
            MarshalException  me = new MarshalException (
                                    "Failed to convert bytes into result", e);
            if (mf_rLogger. call)
                mf_rLogger. call (this, log + " ??? " + me);
            throw me;
        }

        if (mf_rLogger. call)
            mf_rLogger. call (this, log + " === " + rObject);
        return rObject;
    }


    void  result (Tube.InvokeResultData rInvokeResultData)
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this
                    , "result (" + rInvokeResultData + ")");
        InvokePacket  rInvokePacket = mf_rQueues. getWaitingPacket (
                            InvokePacket.class, rInvokeResultData. mf_iCount);
        rInvokePacket. setResult (rInvokeResultData. mf_abyteObject);
    }
    void  exception (Tube.InvokeExceptionData rInvokeExceptionData)
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this
                    , "exception (" + rInvokeExceptionData + ")");
        InvokePacket  rInvokePacket = mf_rQueues. getWaitingPacket (
                        InvokePacket.class, rInvokeExceptionData. mf_iCount);
        rInvokePacket. setException (rInvokeExceptionData. mf_abyteException);
    }

}
