// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  Sender
    extends Thread
{
    Sender (TransportContext rTransportContext
            , Queues rQueues, Tube rTube, int iCount
            , Bond rBond)
    {
        super (rTransportContext. mf_rThreadGroup
                , rTransportContext. mf_strPrefix
                    + ">" + iCount);
        setDaemon (true);
        mf_rLogger = rTransportContext. mf_rLogger;
        mf_rQueues = rQueues;
        mf_rTube = rTube;
        mf_rBond = rBond;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;
    private final Tube  mf_rTube;
    private final Bond  mf_rBond;
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
            mf_rQueues. loopSend (mf_rTube);
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
