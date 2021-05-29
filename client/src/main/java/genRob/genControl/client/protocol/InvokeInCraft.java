// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  InvokeInCraft
{

    InvokeInCraft (Logger rLogger, TransportContext rTransportContext
                    , Queues rQueues)
    {
        mf_rLogger = rLogger;
        mf_rQueues = rQueues;
        mf_rTransportContext = rTransportContext;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;
    private final TransportContext  mf_rTransportContext;


    void  invoke (Tube.InvokeData rInvokeData)
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this
                    , "invoke (" + rInvokeData + ")");
        InvokeThread  rInvokeThread
            = new InvokeThread (mf_rTransportContext, mf_rQueues, rInvokeData);
        rInvokeThread. start ();
    }

}
