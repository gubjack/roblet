// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  ResourceCraft
{

    ResourceCraft (Logger rLogger, TransportContext rTransportContext
                    , Queues rQueues)
    {
        mf_rLogger = rLogger;
        mf_rTransportContext = rTransportContext;
        mf_rQueues = rQueues;
    }
    private final Logger  mf_rLogger;
    private final TransportContext  mf_rTransportContext;
    private final Queues  mf_rQueues;


    void  resource (Tube.ResourceData rResourceData)
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this
                    , "resource (" + rResourceData + ")");
        ResourceThread  rResourceThread
            = new ResourceThread (mf_rTransportContext, mf_rQueues
                                    , rResourceData);
        rResourceThread. start ();
    }

}
