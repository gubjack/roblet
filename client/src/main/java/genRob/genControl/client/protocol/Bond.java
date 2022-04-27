// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;
import  java.lang.ref.WeakReference;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
public class  Bond
{
    private static int  ms_iCount;

    Bond (TransportContext rTransportContext, Queues rQueues
            , ErrorHandler rErrorHandler, Tube rTube
            , SlotCraft rSlotCraft, RunCraft rRunCraft
            , ResourceCraft rResourceCraft
            , InvokeInCraft rInvokeInCraft, InvokeOutCraft rInvokeOutCraft)
    {
        mf_rLogger = rTransportContext. mf_rLogger;

        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "Bond(" + rTube + ") - begin");

        try
        {
            mf_rWeakReference_ErrorHandler
                            = new WeakReference<ErrorHandler> (rErrorHandler);
            mf_rTube = rTube;

            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "create threads");
            synchronized (Bond.class)
            {
                mf_iCount = ms_iCount++;
            }
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "create sender #" + mf_iCount);
            mf_rSender = new Sender (rTransportContext, rQueues
                                    , mf_rTube, mf_iCount, this);
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "create receiver #" + mf_iCount);
            mf_rReceiver = new Receiver (rTransportContext
                                        , rQueues, mf_rTube, mf_iCount, this
                                        , rSlotCraft, rRunCraft
                                        , rResourceCraft
                                        , rInvokeInCraft, rInvokeOutCraft);
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "create tester #" + mf_iCount);
            mf_rTester = new Tester (rTransportContext, rQueues, mf_iCount);

            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "start threads");

            mf_rReceiver. start ();
            mf_rSender. start ();
            mf_rTester. start ();
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "Bond(" + rTube + ") - end");
        }
    }
    private final Logger  mf_rLogger;
    private final WeakReference<ErrorHandler>  mf_rWeakReference_ErrorHandler;
    final Tube  mf_rTube;
    final int  mf_iCount;
    private final Sender  mf_rSender;
    private final Receiver  mf_rReceiver;
    private final Tester  mf_rTester;

    void  _Bond ()
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "_Bond() - begin");

        try
        {
            synchronized (this)
            {
                if (m_bClosed)
                {
                    if (mf_rLogger. transport)
                        mf_rLogger. transport (this, "already closed");
                    return;
                }
                m_bClosed = true;
            }

            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "close tube");
            try
            {
                mf_rTube. close ();
            }
            catch (IOException e)
            {
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this, e);
            }

            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "interrupt threads");
            mf_rSender. interrupt ();
            mf_rReceiver. interrupt ();
            mf_rTester. interrupt ();
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "_Bond() - end");
        }
    }
    private boolean  m_bClosed;

    void  join ()
        throws InterruptedException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "join threads");
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "JOIN sender ...");
            mf_rSender. join ();
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "... joined sender");
        }
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "JOIN receiver ...");
            mf_rReceiver. join ();
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "... joined receiver");
        }
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "JOIN tester ...");
            mf_rTester. join ();
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "... joined tester");
        }
    }

    synchronized void  handleIOException ()
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "handleIOException() - begin");

        try
        {
            if (m_bClosed)
            {
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this, "already closed");
                return;
            }
            if (m_bIOExceptionHandled)
            {
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this, "already handled");
                return;
            }
            ErrorHandler  rErrorHandler
                                    = mf_rWeakReference_ErrorHandler. get ();
            if (rErrorHandler == null)
            {
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this, "Error handler closed");
                return;
            }
            rErrorHandler. handleIOException (this);
            m_bIOExceptionHandled = true;
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "handleIOException() - end");
        }
    }
    private boolean  m_bIOExceptionHandled;

}
