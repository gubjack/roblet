// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  Receiver
    extends Thread
{
    Receiver (TransportContext rTransportContext
            , Queues rQueues, Tube rTube, int iCount, Bond rBond
            , SlotCraft rSlotCraft, RunCraft rRunCraft
            , ResourceCraft rResourceCraft
            , InvokeInCraft rInvokeInCraft, InvokeOutCraft rInvokeOutCraft)
    {
        super (rTransportContext. mf_rThreadGroup
                , rTransportContext. mf_strPrefix
                    + "<" + iCount);
        setDaemon (true);
        mf_rLogger = rTransportContext. mf_rLogger;
        mf_rQueues = rQueues;
        mf_rTube = rTube;
        mf_rBond = rBond;
        mf_rSlotCraft = rSlotCraft;
        mf_rRunCraft = rRunCraft;
        mf_rResourceCraft = rResourceCraft;
        mf_rInvokeInCraft = rInvokeInCraft;
        mf_rInvokeOutCraft = rInvokeOutCraft;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;
    private final Tube  mf_rTube;
    private final Bond  mf_rBond;
    private final SlotCraft  mf_rSlotCraft;
    private final RunCraft  mf_rRunCraft;
    private final ResourceCraft  mf_rResourceCraft;
    private final InvokeInCraft  mf_rInvokeInCraft;
    private final InvokeOutCraft  mf_rInvokeOutCraft;

    public void  run ()
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "START");
        try
        {
            work ();
        }
        catch (RuntimeException e)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "CATCH " + e);
            throw e;
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "END");
        }
    }
    private void  work ()
    {
        try
        {
            Tube.Data  rData = null;
            for (;;)
            {
                if (    (mf_rLogger. transport
                            &&  rData == null)
                    ||  (mf_rLogger. transport
                            &&  ! (rData instanceof Tube.ConfirmData))
                    ||  (mf_rLogger. transport
                        &&  mf_rLogger. confirm))
                    mf_rLogger. transport (this, "READ next packet ...");
                rData = mf_rTube. readPacket ();
                if (    (mf_rLogger. transport
                            &&  ! (rData instanceof Tube.ConfirmData))
                    ||  (mf_rLogger. transport
                        &&  mf_rLogger. confirm))
                    mf_rLogger. transport (this
                            , "... read next packet " + rData);

                if (rData instanceof Tube.ConfirmData)
                {
                    mf_rQueues
                        . confirm (((Tube.ConfirmData) rData). mf_iConfirm);
                    continue;
                }
                mf_rQueues. confirm ();

                if (rData instanceof Tube.SlotResultData)
                {
                    mf_rSlotCraft. result ((Tube.SlotResultData) rData);
                    continue;
                }
                if (rData instanceof Tube.RunResultData)
                {
                    mf_rRunCraft. result ((Tube.RunResultData) rData);
                    continue;
                }
                if (rData instanceof Tube.RunExceptionData)
                {
                    mf_rRunCraft. exception ((Tube.RunExceptionData) rData);
                    continue;
                }
                if (rData instanceof Tube.ResourceData)
                {
                    mf_rResourceCraft. resource ((Tube.ResourceData) rData);
                    continue;
                }
                if (rData instanceof Tube.InvokeData)
                {
                    mf_rInvokeInCraft. invoke ((Tube.InvokeData) rData);
                    continue;
                }
                if (rData instanceof Tube.InvokeResultData)
                {
                    mf_rInvokeOutCraft. result ((Tube.InvokeResultData) rData);
                    continue;
                }
                if (rData instanceof Tube.InvokeExceptionData)
                {
                    mf_rInvokeOutCraft. exception (
                                            (Tube.InvokeExceptionData) rData);
                    continue;
                }
                throw new IOException ("Unexpected packet " + rData);
            }
        }
        catch (IOException e)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, e);
            mf_rBond. handleIOException ();
        }
        catch (InterruptedException e)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "INTERRUPTED");
        }
    }

}
