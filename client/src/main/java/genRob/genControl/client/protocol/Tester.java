// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  Tester
    extends Thread
{
    Tester (TransportContext rTransportContext, Queues rQueues, int iCount)
    {
        super (rTransportContext. mf_rThreadGroup
                , rTransportContext. mf_strPrefix
                    + ":" + iCount);
        setDaemon (true);
        mf_rLogger = rTransportContext. mf_rLogger;
        mf_rQueues = rQueues;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;
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
            for (;;)
            {
                synchronized (this)
                {
                    if (mf_rLogger. transport)
                        mf_rLogger. transport (this, "WAIT 30 seconds ...");
                    wait (30000);
                    if (mf_rLogger. transport)
                        mf_rLogger. transport (this
                                , "... finished waiting 30 seconds");

                    if (mf_rLogger. transport)
                        mf_rLogger. transport (this
                                , "Insert confirm for testing purposes");
                    mf_rQueues. insertConfirm ();
                }
            }
        }
        catch (InterruptedException e)
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "INTERRUPTED");
        }
    }
}
