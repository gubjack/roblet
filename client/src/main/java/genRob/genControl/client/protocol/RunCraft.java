// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import java.io.IOException;

import org.roblet.Roblet;
import org.roblet.client.Logger;
import org.roblet.client.MarshalException;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
class  RunCraft
{

    RunCraft (Logger rLogger, Queues rQueues)
    {
        mf_rLogger = rLogger;
        mf_rQueues = rQueues;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;


    <RESULT> RESULT  run (int iSlot, Roblet<RESULT> rRoblet)
        throws InterruptedException, MarshalException
                , SlotException, Exception
    {
        int  iCount;
        synchronized (mf_rQueues)
        {
            iCount = m_iCount_run++;
        }
        String  log = null;
        if (mf_rLogger. call)
        {
            log = "run (@" + iSlot + "," + rRoblet + ") - #" + iCount;
            mf_rLogger. call (this, "ENTER " + log);
        }

        try
        {
            return run (log, iCount, iSlot, rRoblet);
        }
        finally
        {
            if (mf_rLogger. call)
                mf_rLogger. call (this, "LEAVE " + log);
        }
    }
    private int  m_iCount_run;
    private <RESULT> RESULT  run (String log, int iCount
                                    , int iSlot, Roblet<RESULT> rRoblet)
        throws InterruptedException, MarshalException
                , SlotException, Exception
    {
        byte[]  abyteRoblet;
        try
        {
            abyteRoblet = Convert. object2bytes (rRoblet);
        }
        catch (IOException e)
        {
            MarshalException  me = new MarshalException (
                                    "Failed to convert roblet into bytes", e);
            if (mf_rLogger. call)
                mf_rLogger. call (this, log + " ??? " + me);
            throw me;
        }

        RunPacket  packet = new RunPacket (mf_rLogger, iCount, iSlot
                                            , abyteRoblet);
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
            throw rException;
        }

        RESULT  rObject;
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


    void  result (Tube.RunResultData rRunResultData)
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this, "result (" + rRunResultData + ")");
        RunPacket  rRunPacket = mf_rQueues. getWaitingPacket (
                                    RunPacket.class, rRunResultData. mf_iCount);
        rRunPacket. setResult (rRunResultData. mf_abyteObject);
    }

    void  exception (Tube.RunExceptionData rRunExceptionData)
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this, "exception (" + rRunExceptionData + ")");
        RunPacket  rRunPacket = mf_rQueues. getWaitingPacket (
                                RunPacket.class, rRunExceptionData. mf_iCount);
        rRunPacket. setException (rRunExceptionData. mf_abyteException);
    }

}
