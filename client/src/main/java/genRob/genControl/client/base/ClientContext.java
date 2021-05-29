// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.base;

import org.roblet.client.Logger;
import org.roblet.server.unit.net.NetUnit;


/**
 * context information on the client
 * @author Hagen Stanek
 */
public class  ClientContext
{

    /**
     * initialise the clients context
     * @param rLogger  logger
     * @param rThreadGroup  thread group to be used when creating threads
     * @param iDelayMS  delay for debug purposes
     * @param rNetUnit  networking unit in case of usage in a roblet
     */
    public  ClientContext (Logger rLogger
                           , ThreadGroup rThreadGroup
                           , int iDelayMS, NetUnit rNetUnit)
    {
        mf_rLogger = rLogger;
        mf_rThreadGroup = rThreadGroup;
        mf_iDelayMS = iDelayMS;
        mf_rNetUnit = rNetUnit;
    }
    /** logger */
    public final Logger  mf_rLogger;
    /** thread group to be used when creating threads */
    public final ThreadGroup  mf_rThreadGroup;
    /** delay for debug purposes */
    public final int  mf_iDelayMS;
    /** networking unit in case of usage in a roblet */
    public final NetUnit  mf_rNetUnit;


    private int  m_iServer;
    /**
     * provide an internal number for a server representation
     * @return  server number
     */
    public synchronized int  getServerNumber ()
    {
        return m_iServer++;
    }


}
