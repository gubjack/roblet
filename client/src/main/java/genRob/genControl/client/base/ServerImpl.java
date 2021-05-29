// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.base;

import org.roblet.client.Logger;
import org.roblet.client.SlotException;

import  genRob.genControl.client.Server;
import  genRob.genControl.client.Slot;
import  genRob.genControl.client.connect.ConnectContext;
import  genRob.genControl.client.connect.Name;
import  genRob.genControl.client.protocol.Transport;
import  genRob.genControl.client.server.ID;


/**
 * server represenation
 * @author Hagen Stanek
 */
@SuppressWarnings("deprecation")
public class  ServerImpl
    implements Server
{

    /**
     * initialise the server representation
     * @param rConnectContext  context for connecting
     * @param rID  server ID
     * @param rTransport  transport instance
     */
    public  ServerImpl (ConnectContext rConnectContext, ID rID
                        , Transport rTransport)
    {
        mf_rLogger = rConnectContext. mf_rLogger;
        mf_strPrefix = rConnectContext. mf_strPrefix;
        mf_iDelayMS = rConnectContext. mf_rClientContext. mf_iDelayMS;

        mf_rID = rID;
        mf_rTransport = rTransport;

        mf_rName = rConnectContext. mf_rName;

        if (mf_rLogger. base)
            mf_rLogger. base (this
                    , mf_strPrefix
                        + " - ServerImpl (" + mf_rName + "," + mf_rID + ")");
    }
    private final Logger  mf_rLogger;
    private final String  mf_strPrefix;
    private final int  mf_iDelayMS;
    private final ID  mf_rID;
    private final Transport  mf_rTransport;
    private final Name  mf_rName;

    // Object
    protected void  finalize ()
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this, mf_strPrefix + " - finalize ()");
    }

    // Object
    public String  toString ()
    {
        return mf_rName. toString ();
    }


    // Server
    public Slot  getSlot ()
        throws InterruptedException, SlotException
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this
                    , mf_strPrefix + " - getSlot () - begin");

        try
        {
            // Gib Fach zurück
            Slot  rSlot = new SlotImpl (mf_rLogger, mf_iDelayMS
                                        , mf_strPrefix
                                        , mf_rTransport);
            return rSlot;
        }
        finally
        {
            if (mf_rLogger. base)
                mf_rLogger. base (this
                        , mf_strPrefix + " - getSlot () - end");
        }
    }

}
